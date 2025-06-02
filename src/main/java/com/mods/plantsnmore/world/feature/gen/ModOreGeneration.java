package com.mods.plantsnmore.world.feature.gen;

import com.mods.plantsnmore.world.feature.ModPlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;

public class ModOreGeneration {

    public static void generateOres(final BiomeLoadingEvent event) {
        List<Holder<PlacedFeature>> base =
                event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);

        // Worm Casting - in allen Biomen mit Grass Blocks
        if (isGrassyBiome(event)) {
            base.add(ModPlacedFeatures.WORM_CASTING_PLACED.getHolder().get());
        }

        // Perlite - in Wüsten und warmen, trockenen Biomen
        if (isDesertLikeBiome(event)) {
            base.add(ModPlacedFeatures.PERLITE_PLACED.getHolder().get());
        }
    }

    private static boolean isGrassyBiome(BiomeLoadingEvent event) {
        // Prüfe auf Biome mit Grass Blocks
        Biome.BiomeCategory category = event.getCategory();
        String biomeName = event.getName().toString();

        return category == Biome.BiomeCategory.PLAINS ||
                category == Biome.BiomeCategory.FOREST ||
                category == Biome.BiomeCategory.EXTREME_HILLS ||
                category == Biome.BiomeCategory.TAIGA ||
                category == Biome.BiomeCategory.SWAMP ||
                category == Biome.BiomeCategory.RIVER ||
                category == Biome.BiomeCategory.SAVANNA ||
                category == Biome.BiomeCategory.JUNGLE ||
                category == Biome.BiomeCategory.BEACH ||
                biomeName.contains("meadow") ||
                biomeName.contains("grove") ||
                biomeName.contains("slope");
    }

    private static boolean isDesertLikeBiome(BiomeLoadingEvent event) {
        // Wüsten und ähnliche Biome
        Biome.BiomeCategory category = event.getCategory();
        String biomeName = event.getName().toString();

        return category == Biome.BiomeCategory.DESERT ||
                category == Biome.BiomeCategory.MESA ||
                biomeName.contains("badlands") ||
                biomeName.contains("desert") ||
                (category == Biome.BiomeCategory.SAVANNA && event.getClimate().temperature > 1.0f);
    }
}