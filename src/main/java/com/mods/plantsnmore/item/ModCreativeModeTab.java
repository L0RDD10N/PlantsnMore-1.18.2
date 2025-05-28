package com.mods.plantsnmore.item;

import com.mods.plantsnmore.block.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {

    public static final CreativeModeTab PLANTS_TAB = new CreativeModeTab("plantstab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MONST_DEL_VAR.get());
        }
    };

    public static final CreativeModeTab SEEDS_TAB = new CreativeModeTab("seedstab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MONST_DEL_VAR_SEEDS.get());
        }
    };

    public static final CreativeModeTab POTS_TAB = new CreativeModeTab("potstab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModBlocks.DECO_POT.get());
        }
    };

    public static final CreativeModeTab FOOD_TAB = new CreativeModeTab("foodtab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.MONST_DEL_FRUIT.get());
        }
    };


}
