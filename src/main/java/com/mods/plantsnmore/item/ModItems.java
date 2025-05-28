package com.mods.plantsnmore.item;

import com.mods.plantsnmore.PlantsnMore;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public  static  final  DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PlantsnMore.MOD_ID);


    public static final RegistryObject<Item> MONST_DEL_SEEDS = ITEMS.register("monst_del_seeds",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.SEEDS_TAB)));

    public static final RegistryObject<Item> MONST_DEL_VAR_SEEDS = ITEMS.register("monst_del_var_seeds",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.SEEDS_TAB)));

    public static final RegistryObject<Item> MONST_DEL_VAR = ITEMS.register("monst_del_var",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.PLANTS_TAB)));

    public static final RegistryObject<Item> MONST_DEL_FRUIT = ITEMS.register("monst_del_fruit",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.FOOD_TAB).food(ModFoods.MONST_DEL_FRUIT)));

    public static final RegistryObject<Item> MONST_DEL_VAR_FRUIT = ITEMS.register("monst_del_var_fruit",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.FOOD_TAB).food(ModFoods.MONST_DEL_FRUIT)));

    public static final RegistryObject<Item> MONST_DEL_FRUIT_EDIBLE = ITEMS.register("monst_del_fruit_edible",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.FOOD_TAB).food(ModFoods.MONST_DEL_FRUIT_EDIBLE)));

    public  static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
