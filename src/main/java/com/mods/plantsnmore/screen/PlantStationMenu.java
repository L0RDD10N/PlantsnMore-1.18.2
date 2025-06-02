package com.mods.plantsnmore.screen;

import com.mods.plantsnmore.block.ModBlocks;
import com.mods.plantsnmore.block.custom.entity.custom.PlantStationBlockEntity;
import com.mods.plantsnmore.screen.slot.ModResultSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PlantStationMenu extends AbstractContainerMenu {
    private final PlantStationBlockEntity blockEntity;
    private final Level level;

    public PlantStationMenu(int p_38852_, Inventory inv, FriendlyByteBuf extraData) {
        this(p_38852_, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()));
    }
    public PlantStationMenu(int p_38852_, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.PLANT_STATION_MENU.get(), p_38852_);
        checkContainerSize(inv, 13);
        this.blockEntity = (PlantStationBlockEntity) entity;
        this.level = inv.player.level;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        this.blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 45, 17));
            this.addSlot(new SlotItemHandler(handler, 1, 63, 17));
            this.addSlot(new SlotItemHandler(handler, 2, 81, 17));
            this.addSlot(new SlotItemHandler(handler, 3, 45, 35));
            this.addSlot(new SlotItemHandler(handler, 4, 63, 35));
            this.addSlot(new SlotItemHandler(handler, 5, 81, 35));
            this.addSlot(new SlotItemHandler(handler, 6, 45, 53));
            this.addSlot(new SlotItemHandler(handler, 7, 63, 53));
            this.addSlot(new SlotItemHandler(handler, 8, 81, 53));
            this.addSlot(new SlotItemHandler(handler, 9, 15, 17));
            this.addSlot(new SlotItemHandler(handler, 10, 15, 35));
            this.addSlot(new SlotItemHandler(handler, 11, 15, 53));
            this.addSlot(new ModResultSlot(handler, 12, 139, 36));
        });
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    private static final int TE_INVENTORY_SLOT_COUNT = 13;  // Anzahl deiner Slots

    @Override
    public ItemStack quickMoveStack(Player p_38859_, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // Von Player zu Tile
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // Von Tile zu Player
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(p_38859_, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        Level level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();
        Block blockAtPos = level.getBlockState(pos).getBlock();

        // Prüfe, ob noch gültig (z.B. Nähe)
        return super.stillValid(ContainerLevelAccess.create(level, pos), pPlayer, blockAtPos);
    }

    private void addPlayerInventory(Inventory inv) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(inv, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inv) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 144));
        }
    }
}