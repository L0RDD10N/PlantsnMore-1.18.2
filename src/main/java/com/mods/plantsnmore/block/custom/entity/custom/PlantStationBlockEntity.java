package com.mods.plantsnmore.block.custom.entity.custom;

import com.mods.plantsnmore.block.custom.entity.ModBlockEntities;
import com.mods.plantsnmore.recipe.PlantStationRecipe;
import com.mods.plantsnmore.screen.PlantStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class PlantStationBlockEntity extends BlockEntity implements MenuProvider {
    private int previousResultCount = 0;
    private boolean isUpdatingPreview = false;

    private final ItemStackHandler itemHandler = new ItemStackHandler(13) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (slot >= 0 && slot <= 11) {
                updateRecipePreview();
            }
            if (slot == 12 && !isUpdatingPreview) {
                handleResultSlotChange();
            }
        }
    };

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public PlantStationBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.PLANT_STATION_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Plant Station");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new PlantStationMenu(i, inventory, this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        updateRecipePreview();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, PlantStationBlockEntity pBlockEntity) {
    }

    private void updateRecipePreview() {
        if (level == null || isUpdatingPreview) return;

        Optional<PlantStationRecipe> recipe = getCurrentRecipe();

        if (recipe.isPresent() && hasRequiredTools(recipe.get())) {
            int possibleCrafts = calculatePossibleCrafts(recipe.get());

            if (possibleCrafts > 0) {
                int maxCrafts = Math.min(possibleCrafts, 7);
                int resultCount = maxCrafts * recipe.get().getOutputCount();

                ItemStack previewResult = new ItemStack(
                        recipe.get().getResultItem().getItem(),
                        resultCount
                );

                ItemStack currentResult = itemHandler.getStackInSlot(12);
                if (currentResult.isEmpty() ||
                        (currentResult.getItem() == previewResult.getItem())) {

                    isUpdatingPreview = true;
                    itemHandler.setStackInSlot(12, previewResult);
                    previousResultCount = resultCount;
                    isUpdatingPreview = false;

                }
            } else {
                isUpdatingPreview = true;
                itemHandler.setStackInSlot(12, ItemStack.EMPTY);
                previousResultCount = 0;
                isUpdatingPreview = false;
            }
        } else {
            isUpdatingPreview = true;
            itemHandler.setStackInSlot(12, ItemStack.EMPTY);
            previousResultCount = 0;
            isUpdatingPreview = false;
        }
    }

    private int calculatePossibleCrafts(PlantStationRecipe recipe) {
        NonNullList<Ingredient> recipeItems = recipe.getIngredients();

        int nonEmptyIngredients = 0;
        Ingredient singleIngredient = null;
        for (Ingredient ingredient : recipeItems) {
            if (!ingredient.isEmpty()) {
                nonEmptyIngredients++;
                singleIngredient = ingredient;
            }
        }

        if (nonEmptyIngredients == 1) {
            int totalFound = 0;

            for (int slot = 0; slot <= 8; slot++) {
                ItemStack itemInSlot = itemHandler.getStackInSlot(slot);
                if (!itemInSlot.isEmpty()) {
                    if (singleIngredient.test(itemInSlot)) {
                        totalFound += itemInSlot.getCount();
                    }
                }
            }

            return totalFound;
        } else {
            Map<Integer, Boolean> requiredSlots = recipe.getRequiredSlots();
            int minPossibleCrafts = Integer.MAX_VALUE;

            for (Map.Entry<Integer, Boolean> entry : requiredSlots.entrySet()) {
                if (entry.getValue()) {
                    int slotIndex = entry.getKey();
                    ItemStack itemInSlot = itemHandler.getStackInSlot(slotIndex);

                    if (itemInSlot.isEmpty() || !recipeItems.get(slotIndex).test(itemInSlot)) {
                        return 0;
                    }

                    minPossibleCrafts = Math.min(minPossibleCrafts, itemInSlot.getCount());
                }
            }
            return minPossibleCrafts == Integer.MAX_VALUE ? 0 : minPossibleCrafts;
        }
    }

    private void handleResultSlotChange() {
        ItemStack currentResult = itemHandler.getStackInSlot(12);
        int currentCount = currentResult.getCount();

        Optional<PlantStationRecipe> recipeOpt = getCurrentRecipe();

        if (currentCount < previousResultCount || (previousResultCount > 0 && currentCount == 0)) {
            if (recipeOpt.isPresent()) {
                int takenItems = previousResultCount - currentCount;
                PlantStationRecipe recipe = recipeOpt.get();
                performPartialCrafting(recipe, takenItems);
            } else {
                previousResultCount = 0;
            }
        }
    }

    private void performPartialCrafting(PlantStationRecipe recipe, int craftAmount) {
        if (craftAmount <= 0) return;

        NonNullList<Ingredient> recipeItems = recipe.getIngredients();

        int nonEmptyIngredients = 0;
        Ingredient singleIngredient = null;
        for (Ingredient ingredient : recipeItems) {
            if (!ingredient.isEmpty()) {
                nonEmptyIngredients++;
                singleIngredient = ingredient;
            }
        }
        int outputCount = recipe.getOutputCount();
        int craftsPerformed = craftAmount / outputCount;
        if (nonEmptyIngredients == 1) {
            int remainingToConsume = craftsPerformed;

            for (int slot = 0; slot <= 8 && remainingToConsume > 0; slot++) {
                ItemStack itemInSlot = itemHandler.getStackInSlot(slot);

                if (!itemInSlot.isEmpty() && singleIngredient.test(itemInSlot)) {
                    int consumeFromThisSlot = Math.min(remainingToConsume, itemInSlot.getCount());
                    itemInSlot.shrink(consumeFromThisSlot);
                    remainingToConsume -= consumeFromThisSlot;
                }
            }
        } else {
            Map<Integer, Boolean> requiredSlots = recipe.getRequiredSlots();

            for (Map.Entry<Integer, Boolean> entry : requiredSlots.entrySet()) {
                if (entry.getValue()) {
                    int slotIndex = entry.getKey();
                    ItemStack itemInSlot = itemHandler.getStackInSlot(slotIndex);

                    if (!itemInSlot.isEmpty() && itemInSlot.getCount() >= craftsPerformed) {
                        itemInSlot.shrink(craftsPerformed);
                    } else if (!itemInSlot.isEmpty()) {
                        itemInSlot.shrink(itemInSlot.getCount());
                    }
                }
            }
        }
        damageTools(recipe, craftsPerformed);
        updateRecipePreview();
    }

    private void damageTools(PlantStationRecipe recipe, int craftsPerformed) {
        Map<Integer, Boolean> requiredToolSlots = recipe.getRequiredToolSlots();

        for (Map.Entry<Integer, Boolean> entry : requiredToolSlots.entrySet()) {
            if (entry.getValue()) {
                int toolSlotIndex = entry.getKey() + 9;
                ItemStack toolInSlot = itemHandler.getStackInSlot(toolSlotIndex);

                if (!toolInSlot.isEmpty() && toolInSlot.isDamageableItem()) {

                    for (int i = 0; i < craftsPerformed; i++) {
                        toolInSlot.hurt(1, new Random(), null);

                        if (toolInSlot.getDamageValue() >= toolInSlot.getMaxDamage()) {
                            itemHandler.setStackInSlot(toolSlotIndex, ItemStack.EMPTY);
                            break;
                        }
                    }
                }
            }
        }
    }

    private Optional<PlantStationRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        return level.getRecipeManager()
                .getRecipeFor(PlantStationRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean hasRequiredTools(PlantStationRecipe recipe) {
        Map<Integer, Boolean> requiredToolSlots = recipe.getRequiredToolSlots();
        NonNullList<Ingredient> toolRequirements = recipe.getToolRequirements();

        for (Map.Entry<Integer, Boolean> entry : requiredToolSlots.entrySet()) {
            if (entry.getValue()) {
                int toolSlotIndex = entry.getKey() + 9;
                ItemStack toolInSlot = itemHandler.getStackInSlot(toolSlotIndex);

                if (!toolRequirements.get(entry.getKey()).test(toolInSlot)) {
                    return false;
                }
            }
        }

        return true;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }
}