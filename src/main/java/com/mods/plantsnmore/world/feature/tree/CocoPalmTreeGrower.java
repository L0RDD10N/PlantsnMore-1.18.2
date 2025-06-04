package com.mods.plantsnmore.world.feature.tree;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import com.mods.plantsnmore.world.feature.ModConfiguredFeatures;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CocoPalmTreeGrower extends AbstractTreeGrower {

    @Override
    protected @Nullable Holder<ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean hasFlowers) {

        int choice = random.nextInt(25);
        if (choice < 3) {
            return ModConfiguredFeatures.COCO_PALM_SMALL.getHolder().orElse(null);
        } else if (choice < 6) {
            return ModConfiguredFeatures.COCO_PALM_MEDIUM.getHolder().orElse(null);
        } else if (choice < 9) {
            return ModConfiguredFeatures.COCO_PALM_LARGE.getHolder().orElse(null);
        } else if (choice < 10){
            return ModConfiguredFeatures.COCO_PALM_ROYAL.getHolder().orElse(null);
        } else if (choice < 13){
            return ModConfiguredFeatures.CURVED_PALM_SMALL_LIGHT.getHolder().orElse(null);
        } else if (choice < 16){
            return ModConfiguredFeatures.CURVED_PALM_MEDIUM_NORMAL.getHolder().orElse(null);
        } else if (choice < 19){
            return ModConfiguredFeatures.CURVED_PALM_LARGE_STRONG_EAST.getHolder().orElse(null);
        } else if (choice < 22){
            return ModConfiguredFeatures.CURVED_PALM_LARGE_STRONG_WEST.getHolder().orElse(null);
        } else if (choice < 23){
            return ModConfiguredFeatures.CURVED_PALM_ROYAL_EXTREME.getHolder().orElse(null);
        } else{
            return ModConfiguredFeatures.CURVED_PALM_WINDSWEPT.getHolder().orElse(null);
        }
    }
}