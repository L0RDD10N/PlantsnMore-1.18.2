package com.mods.plantsnmore.world.feature.gen;

import com.mods.plantsnmore.world.feature.ModConfiguredFeatures;
import com.mods.plantsnmore.world.feature.ModPlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;
import java.util.Set;

public class ModTreeGeneration {

    // Biomes where palm trees should generate
    private static final Set<ResourceKey<Biome>> TROPICAL_BIOMES = Set.of(
            Biomes.BEACH,
            Biomes.DESERT,
            Biomes.WARM_OCEAN,
            Biomes.LUKEWARM_OCEAN,
            Biomes.JUNGLE,
            Biomes.BAMBOO_JUNGLE,
            Biomes.SAVANNA,
            Biomes.SAVANNA_PLATEAU
    );

    private static final Set<ResourceKey<Biome>> BEACH_BIOMES = Set.of(
            Biomes.BEACH,
            Biomes.WARM_OCEAN,
            Biomes.LUKEWARM_OCEAN
    );

    private static final Set<ResourceKey<Biome>> DESERT_BIOMES = Set.of(
            Biomes.DESERT
    );

    public static void generateTrees(BiomeLoadingEvent event) {
        ResourceKey<Biome> biomeKey = ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());

        if (biomeKey == null) return;

        // Generate palm trees in tropical biomes
        if (TROPICAL_BIOMES.contains(biomeKey)) {
            generateTropicalPalms(event);
        }

        // Special generation for beach biomes
        if (BEACH_BIOMES.contains(biomeKey)) {
            generateBeachPalms(event);
        }

        // Special generation for desert biomes
        if (DESERT_BIOMES.contains(biomeKey)) {
            generateDesertPalms(event);
        }
    }

    private static void generateTropicalPalms(BiomeLoadingEvent event) {
        // Add various palm groups to tropical biomes
        List<Holder<PlacedFeature>> vegDecoration = event.getGeneration().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION);

        // Add palm groups with different rarities
        vegDecoration.add(ModPlacedFeatures.TROPICAL_PALM_GROUP_COMMON.getHolder().get());
        vegDecoration.add(ModPlacedFeatures.TROPICAL_PALM_GROUP_PLACED.getHolder().get());
        vegDecoration.add(ModPlacedFeatures.COCONUT_PALM_GROVE_PLACED.getHolder().get());
    }

    private static void generateBeachPalms(BiomeLoadingEvent event) {
        // Beach-specific palm generation
        List<Holder<PlacedFeature>> vegDecoration = event.getGeneration().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION);

        vegDecoration.add(ModPlacedFeatures.BEACH_PALM_CLUSTER_PLACED.getHolder().get());
        vegDecoration.add(ModPlacedFeatures.WINDSWEPT_PALM_GROUP_PLACED.getHolder().get());
    }

    private static void generateDesertPalms(BiomeLoadingEvent event) {
        // Desert oasis palm generation
        List<Holder<PlacedFeature>> vegDecoration = event.getGeneration().getFeatures(GenerationStep.Decoration.VEGETAL_DECORATION);

        vegDecoration.add(ModPlacedFeatures.DESERT_OASIS_PALMS_PLACED.getHolder().get());
    }
}