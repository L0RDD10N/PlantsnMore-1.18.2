package com.mods.plantsnmore.world.feature;

import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModOrePlacement {
    public static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    public static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }

    public static List<PlacementModifier> rareOrePlacement(int p_195350_, PlacementModifier p_195351_) {
        return orePlacement(RarityFilter.onAverageOnceEvery(p_195350_), p_195351_);
    }

    // Für Worm Casting - spezielle oberflächennahe Platzierung
    public static List<PlacementModifier> wormCastingPlacement(int count, PlacementModifier heightModifier) {
        return List.of(
                CountPlacement.of(count),
                InSquarePlacement.spread(),
                heightModifier,
                SurfaceWaterDepthFilter.forMaxDepth(0), // Nur an der Oberfläche, nicht unter Wasser
                BiomeFilter.biome()
        );
    }

    // Für Worm Casting - oberflächennah
    public static List<PlacementModifier> surfaceOrePlacement(int count, PlacementModifier heightModifier) {
        return List.of(
                CountPlacement.of(count),
                InSquarePlacement.spread(),
                heightModifier,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN,
                        BlockPredicate.solid(), 32), // Einfacher: sucht nach solidem Block
                RandomOffsetPlacement.vertical(net.minecraft.util.valueproviders.UniformInt.of(-2, 0)), // 1-2 Blöcke unter Oberfläche
                BiomeFilter.biome()
        );
    }

    // Für Perlite in Wüsten
    public static List<PlacementModifier> desertOrePlacement(int count, PlacementModifier heightModifier) {
        return List.of(
                CountPlacement.of(count),
                InSquarePlacement.spread(),
                heightModifier,
                BiomeFilter.biome()
        );
    }
}