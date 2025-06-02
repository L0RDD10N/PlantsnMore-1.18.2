package com.mods.plantsnmore.recipe;

import com.mods.plantsnmore.PlantsnMore;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PlantsnMore.MOD_ID);

    public static final RegistryObject<RecipeSerializer<PlantStationRecipe>> PLANT_STATION_SERIALIZER =
            SERIALIZERS.register("plant_station_craft", () -> PlantStationRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
