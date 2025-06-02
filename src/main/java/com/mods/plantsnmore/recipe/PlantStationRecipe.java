package com.mods.plantsnmore.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mods.plantsnmore.PlantsnMore;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class PlantStationRecipe implements Recipe<SimpleContainer> {
    private final ResourceLocation id;
    private final ItemStack output;
    private final int outputCount;
    private final NonNullList<Ingredient> recipeItems; // Slots 0-8 (3x3 Crafting Grid)
    private final NonNullList<Ingredient> toolRequirements; // Slots 9-11 (Tool Slots)
    private final Map<Integer, Boolean> requiredSlots; // Welche Slots müssen gefüllt sein
    private final Map<Integer, Boolean> requiredToolSlots; // Welche Tool-Slots müssen gefüllt sein

    private final boolean isSingleIngredientRecipe;
    private final Ingredient singleIngredient;

    public PlantStationRecipe(ResourceLocation id, ItemStack output, int outputCount,
                              NonNullList<Ingredient> recipeItems,
                              NonNullList<Ingredient> toolRequirements,
                              Map<Integer, Boolean> requiredSlots,
                              Map<Integer, Boolean> requiredToolSlots) {
        this.id = id;
        this.output = output;
        this.outputCount = outputCount;
        this.recipeItems = recipeItems;
        this.toolRequirements = toolRequirements;
        this.requiredSlots = requiredSlots;
        this.requiredToolSlots = requiredToolSlots;

        // Korrigierte Single-Ingredient Detection
        int ingredientCount = 0;
        Ingredient foundIngredient = null;

        // Prüfe ob ingredients Array verwendet wurde (typisch für single ingredient)
        if (requiredSlots.isEmpty()) {
            // Kein shaped pattern, schaue nach dem ersten nicht-leeren Ingredient
            for (int i = 0; i < 9; i++) {
                if (!recipeItems.get(i).isEmpty()) {
                    ingredientCount++;
                    foundIngredient = recipeItems.get(i);
                }
            }
            this.isSingleIngredientRecipe = (ingredientCount == 1);
        } else {
            // Shaped pattern vorhanden
            this.isSingleIngredientRecipe = (requiredSlots.size() == 1);
            if (isSingleIngredientRecipe) {
                int slotIndex = requiredSlots.keySet().iterator().next();
                foundIngredient = recipeItems.get(slotIndex);
            }
        }

        this.singleIngredient = foundIngredient;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        // Keine Client-Side Einschränkung - das kann Probleme verursachen
        return performRecipeValidation(pContainer, pLevel);
    }

    private boolean performRecipeValidation(SimpleContainer pContainer, Level pLevel) {
        if (pLevel != null && !pLevel.isClientSide()) {
            System.out.println("=== Recipe Validation für " + this.getId() + " ===");
            System.out.println("Single Ingredient Recipe: " + isSingleIngredientRecipe);
            for (int i = 0; i < 12; i++) {
                ItemStack item = pContainer.getItem(i);
                if (!item.isEmpty()) {
                    System.out.println("Slot " + i + ": " + item.getItem().toString() + " x" + item.getCount());
                }
            }
        }

        // Korrigierte Tool Validation
        for (Map.Entry<Integer, Boolean> entry : requiredToolSlots.entrySet()) {
            if (entry.getValue()) {
                int toolSlotIndex = entry.getKey();
                int containerSlot = toolSlotIndex + 9; // Tool slots sind 9-11 im Container
                ItemStack toolItem = pContainer.getItem(containerSlot);

                if (toolItem.isEmpty() || !toolRequirements.get(toolSlotIndex).test(toolItem)) {
                    System.out.println("Tool validation failed for tool slot " + toolSlotIndex + " (container slot " + containerSlot + ")");
                    return false;
                }
            }
        }

        // Single-Ingredient Rezepte
        if (isSingleIngredientRecipe && singleIngredient != null) {
            return validateSingleIngredient(pContainer);
        }

        // Multi-Ingredient Rezepte
        return validateShapedPattern(pContainer);
    }

    private boolean validateSingleIngredient(SimpleContainer pContainer) {
        int matchingSlots = 0;
        int totalItems = 0;

        // Prüfe alle Crafting Slots (0-8)
        for (int i = 0; i < 9; i++) {
            ItemStack slotItem = pContainer.getItem(i);
            if (!slotItem.isEmpty()) {
                totalItems++;
                if (singleIngredient.test(slotItem)) {
                    matchingSlots++;
                } else {
                    // Falsches Item gefunden
                    System.out.println("Wrong item in slot " + i + ": " + slotItem);
                    return false;
                }
            }
        }

        boolean isValid = matchingSlots == 1 && totalItems == 1;
        System.out.println("Single ingredient validation: matching=" + matchingSlots + ", total=" + totalItems + ", valid=" + isValid);
        return isValid;
    }

    private boolean validateShapedPattern(SimpleContainer pContainer) {
        // Prüfe jede Position im 3x3 Grid
        for (int i = 0; i < 9; i++) {
            ItemStack slotItem = pContainer.getItem(i);
            Ingredient requiredIngredient = recipeItems.get(i);

            if (!requiredIngredient.isEmpty()) {
                // Dieser Slot benötigt ein spezifisches Item
                if (slotItem.isEmpty() || !requiredIngredient.test(slotItem)) {
                    System.out.println("Shaped pattern failed at slot " + i + ": expected " + requiredIngredient + ", got " + slotItem);
                    return false;
                }
            } else {
                // Dieser Slot sollte leer sein
                if (!slotItem.isEmpty()) {
                    System.out.println("Shaped pattern failed at slot " + i + ": expected empty, got " + slotItem);
                    return false;
                }
            }
        }

        System.out.println("Shaped pattern validation successful");
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    public boolean isSingleIngredientRecipe() {
        return isSingleIngredientRecipe;
    }

    public Ingredient getSingleIngredient() {
        return singleIngredient;
    }

    public NonNullList<Ingredient> getToolRequirements() {
        return toolRequirements;
    }

    public Map<Integer, Boolean> getRequiredSlots() {
        return requiredSlots;
    }

    public Map<Integer, Boolean> getRequiredToolSlots() {
        return requiredToolSlots;
    }

    public int getOutputCount() {
        return outputCount;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer) {
        return new ItemStack(output.getItem(), outputCount);
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth >= 3 && pHeight >= 3;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(output.getItem(), outputCount);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    // Override für bessere Debugging
    @Override
    public String toString() {
        return "PlantStationRecipe{" +
                "id=" + id +
                ", singleIngredient=" + isSingleIngredientRecipe +
                ", toolsRequired=" + requiredToolSlots.size() +
                '}';
    }

    public static class Type implements RecipeType<PlantStationRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "plant_station_craft";
    }

    public static class Serializer implements RecipeSerializer<PlantStationRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(PlantsnMore.MOD_ID,"plant_station_craft");

        @Override
        public PlantStationRecipe fromJson(ResourceLocation id, JsonObject json) {
            System.out.println("Loading recipe: " + id);

            // Output Item und Count
            JsonObject outputJson = GsonHelper.getAsJsonObject(json, "output");
            ItemStack output = ShapedRecipe.itemStackFromJson(outputJson);
            int outputCount = GsonHelper.getAsInt(outputJson, "count", 1);

            // Crafting Pattern (3x3 Grid)
            NonNullList<Ingredient> recipeItems = NonNullList.withSize(9, Ingredient.EMPTY);
            Map<Integer, Boolean> requiredSlots = new HashMap<>();

            if (json.has("pattern")) {
                System.out.println("Processing shaped recipe with pattern");
                // Shaped Recipe mit Pattern
                JsonArray pattern = GsonHelper.getAsJsonArray(json, "pattern");
                JsonObject key = GsonHelper.getAsJsonObject(json, "key");

                for (int row = 0; row < Math.min(3, pattern.size()); row++) {
                    String rowString = pattern.get(row).getAsString();
                    for (int col = 0; col < Math.min(3, rowString.length()); col++) {
                        char c = rowString.charAt(col);
                        int slotIndex = row * 3 + col;

                        if (c != ' ' && key.has(String.valueOf(c))) {
                            recipeItems.set(slotIndex, Ingredient.fromJson(key.get(String.valueOf(c))));
                            requiredSlots.put(slotIndex, true);
                            System.out.println("Set slot " + slotIndex + " to " + key.get(String.valueOf(c)));
                        }
                    }
                }
            } else if (json.has("ingredients")) {
                System.out.println("Processing single ingredient recipe");
                JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");

                // Für Single-Ingredient: setze nur das erste Element, keine requiredSlots
                if (ingredients.size() == 1) {
                    recipeItems.set(0, Ingredient.fromJson(ingredients.get(0)));
                    // KEINE requiredSlots setzen für single ingredient!
                    System.out.println("Set single ingredient in slot 0: " + ingredients.get(0));
                } else {
                    // Multi-ingredient shapeless
                    for (int i = 0; i < Math.min(9, ingredients.size()); i++) {
                        if (!ingredients.get(i).isJsonNull()) {
                            recipeItems.set(i, Ingredient.fromJson(ingredients.get(i)));
                            requiredSlots.put(i, true);
                            System.out.println("Set ingredient slot " + i + " to " + ingredients.get(i));
                        }
                    }
                }
            }

            // Tool Requirements (unverändert, aber korrigierte Indizierung)
            NonNullList<Ingredient> toolRequirements = NonNullList.withSize(3, Ingredient.EMPTY);
            Map<Integer, Boolean> requiredToolSlots = new HashMap<>();

            if (json.has("tools")) {
                System.out.println("Processing tool requirements");
                JsonArray tools = GsonHelper.getAsJsonArray(json, "tools");
                for (int i = 0; i < Math.min(3, tools.size()); i++) {
                    if (!tools.get(i).isJsonNull()) {
                        toolRequirements.set(i, Ingredient.fromJson(tools.get(i)));
                        requiredToolSlots.put(i, true); // i ist korrekt (0, 1, 2 für die 3 Tool-Slots)
                        System.out.println("Set tool slot " + i + " to " + tools.get(i));
                    }
                }
            }

            PlantStationRecipe recipe = new PlantStationRecipe(id, output, outputCount, recipeItems, toolRequirements,
                    requiredSlots, requiredToolSlots);
            System.out.println("Created recipe: " + recipe);
            return recipe;
        }

        @Override
        public PlantStationRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            // Output
            ItemStack output = buf.readItem();
            int outputCount = buf.readInt();

            // Recipe Items (9 slots)
            NonNullList<Ingredient> recipeItems = NonNullList.withSize(9, Ingredient.EMPTY);
            Map<Integer, Boolean> requiredSlots = new HashMap<>();

            int recipeSlotCount = buf.readInt();
            for (int i = 0; i < recipeSlotCount; i++) {
                int slotIndex = buf.readInt();
                recipeItems.set(slotIndex, Ingredient.fromNetwork(buf));
                requiredSlots.put(slotIndex, true);
            }

            // Tool Requirements (3 slots)
            NonNullList<Ingredient> toolRequirements = NonNullList.withSize(3, Ingredient.EMPTY);
            Map<Integer, Boolean> requiredToolSlots = new HashMap<>();

            int toolSlotCount = buf.readInt();
            for (int i = 0; i < toolSlotCount; i++) {
                int slotIndex = buf.readInt();
                toolRequirements.set(slotIndex, Ingredient.fromNetwork(buf));
                requiredToolSlots.put(slotIndex, true);
            }

            return new PlantStationRecipe(id, output, outputCount, recipeItems, toolRequirements,
                    requiredSlots, requiredToolSlots);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, PlantStationRecipe recipe) {
            // Output
            buf.writeItem(recipe.getResultItem());
            buf.writeInt(recipe.getOutputCount());

            // Recipe Items - nur die nicht-leeren
            int nonEmptySlots = 0;
            for (int i = 0; i < 9; i++) {
                if (!recipe.recipeItems.get(i).isEmpty()) {
                    nonEmptySlots++;
                }
            }

            buf.writeInt(nonEmptySlots);
            for (int i = 0; i < 9; i++) {
                if (!recipe.recipeItems.get(i).isEmpty()) {
                    buf.writeInt(i);
                    recipe.recipeItems.get(i).toNetwork(buf);
                }
            }

            // Tool Requirements
            buf.writeInt(recipe.requiredToolSlots.size());
            for (Map.Entry<Integer, Boolean> entry : recipe.requiredToolSlots.entrySet()) {
                if (entry.getValue()) {
                    buf.writeInt(entry.getKey());
                    recipe.toolRequirements.get(entry.getKey()).toNetwork(buf);
                }
            }
        }

        @Override
        public RecipeSerializer<?> setRegistryName(ResourceLocation name) {
            return INSTANCE;
        }

        @Nullable
        @Override
        public ResourceLocation getRegistryName() {
            return ID;
        }

        @Override
        public Class<RecipeSerializer<?>> getRegistryType() {
            return Serializer.castClass(RecipeSerializer.class);
        }

        private static <G> Class<G> castClass(Class<?> cls) {
            return (Class<G>) cls;
        }
    }
}