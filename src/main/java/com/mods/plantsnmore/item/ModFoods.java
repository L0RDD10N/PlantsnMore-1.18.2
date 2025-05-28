package com.mods.plantsnmore.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {

    public static final FoodProperties MONST_DEL_FRUIT_EDIBLE = (new FoodProperties.Builder()).fast().nutrition(2).saturationMod(0.2F).build();

    public static final FoodProperties MONST_DEL_FRUIT = (new FoodProperties.Builder()).fast().nutrition(4).saturationMod(0.4F).effect(new MobEffectInstance(MobEffects.POISON, 300, 0), 1.0F).build();
}
