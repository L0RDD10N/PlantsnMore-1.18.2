package com.mods.plantsnmore.world.feature.tree;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import com.mods.plantsnmore.world.feature.ModConfiguredFeatures;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CocoPalmTreeGrower extends AbstractTreeGrower {

    @SuppressWarnings("unchecked")
    @Override
    protected @Nullable Holder<ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean hasFlowers) {
        // Zufällige Auswahl zwischen verschiedenen Palmengrößen
        int choice = random.nextInt(10);
        if (choice < 4) {
            return (Holder<ConfiguredFeature<?, ?>>) (Holder<?>) ModConfiguredFeatures.COCO_PALM_SMALL.getHolder().get();
        } else if (choice < 7) {
            return (Holder<ConfiguredFeature<?, ?>>) (Holder<?>) ModConfiguredFeatures.COCO_PALM_MEDIUM.getHolder().get();
        } else if (choice < 9) {
            return (Holder<ConfiguredFeature<?, ?>>) (Holder<?>) ModConfiguredFeatures.COCO_PALM_LARGE.getHolder().get();
        } else {
            return (Holder<ConfiguredFeature<?, ?>>) (Holder<?>) ModConfiguredFeatures.COCO_PALM_ROYAL.getHolder().get();
        }
    }
}