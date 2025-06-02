package com.mods.plantsnmore.world.feature.gen;

import com.mods.plantsnmore.world.feature.ModConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModTreeGeneration {

    @SubscribeEvent
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        generatePalmTrees(event);
    }

    public static void generatePalmTrees(final BiomeLoadingEvent event) {
        ResourceKey<Biome> biomeKey = ResourceKey.create(
                net.minecraft.core.Registry.BIOME_REGISTRY,
                event.getName()
        );

        // Standard Strand-Palmen in Beach-Biomen
        if (isBeachBiome(event, biomeKey)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModConfiguredFeatures.COCO_PALM_BEACH_PLACED.getHolder().get());

            // Seltener: Gekrümmte Palmen für mehr Variation
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModConfiguredFeatures.CURVED_PALM_BEACH_PLACED.getHolder().get());
        }

        // Sturm-gebeutelte Palmen an windigen Küsten
        if (isStormyCoastBiome(event, biomeKey)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModConfiguredFeatures.STORM_PALM_COAST_PLACED.getHolder().get());
        }

        // Seltene Oasen-Palmen in Wüsten
        if (isDesertBiome(event, biomeKey)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModConfiguredFeatures.COCO_PALM_OASIS_PLACED.getHolder().get());
        }

        // Häufigere tropische Palmen in Dschungel-Biomen
        if (isTropicalBiome(event, biomeKey)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,
                    ModConfiguredFeatures.COCO_PALM_TROPICAL_PLACED.getHolder().get());
        }
    }

    private static boolean isBeachBiome(BiomeLoadingEvent event, ResourceKey<Biome> biomeKey) {
        Biome.BiomeCategory category = event.getCategory();
        String biomeName = event.getName().toString();

        // Vanilla Beach-Biome
        if (biomeKey.equals(Biomes.BEACH) ||
                biomeKey.equals(Biomes.WARM_OCEAN) ||
                biomeKey.equals(Biomes.LUKEWARM_OCEAN)) {
            return true;
        }

        // Kategorie-basierte Erkennung
        if (category == Biome.BiomeCategory.BEACH) {
            return true;
        }

        // String-basierte Erkennung für Mod-Biome
        return biomeName.contains("beach") ||
                biomeName.contains("shore") ||
                biomeName.contains("coast") ||
                (biomeName.contains("ocean") && event.getClimate().temperature > 0.5f);
    }

    private static boolean isStormyCoastBiome(BiomeLoadingEvent event, ResourceKey<Biome> biomeKey) {
        String biomeName = event.getName().toString();

        // Vanilla windige Küsten-Biome
        if (biomeKey.equals(Biomes.STONY_SHORE) ||
                biomeKey.equals(Biomes.WINDSWEPT_HILLS) ||
                biomeKey.equals(Biomes.WINDSWEPT_FOREST) ||
                biomeKey.equals(Biomes.WINDSWEPT_GRAVELLY_HILLS)) {
            return true;
        }

        // String-basierte Erkennung
        return biomeName.contains("windswept") ||
                biomeName.contains("windy") ||
                biomeName.contains("stormy") ||
                (biomeName.contains("cliff") && event.getClimate().temperature > 0.3f);
    }

    private static boolean isDesertBiome(BiomeLoadingEvent event, ResourceKey<Biome> biomeKey) {
        Biome.BiomeCategory category = event.getCategory();
        String biomeName = event.getName().toString();

        // Vanilla Wüsten-Biome
        if (biomeKey.equals(Biomes.DESERT) ||
                biomeKey.equals(Biomes.WARM_OCEAN)) {
            return true;
        }

        // Kategorie-basierte Erkennung
        if (category == Biome.BiomeCategory.DESERT) {
            return true;
        }

        // String-basierte Erkennung für warme, trockene Biome
        return biomeName.contains("desert") ||
                (biomeName.contains("sand") && event.getClimate().temperature > 0.8f) ||
                (category == Biome.BiomeCategory.MESA && event.getClimate().downfall < 0.3f);
    }

    private static boolean isTropicalBiome(BiomeLoadingEvent event, ResourceKey<Biome> biomeKey) {
        Biome.BiomeCategory category = event.getCategory();
        String biomeName = event.getName().toString();
        Biome.ClimateSettings climate = event.getClimate();

        // Vanilla tropische Biome
        if (biomeKey.equals(Biomes.JUNGLE) ||
                biomeKey.equals(Biomes.SPARSE_JUNGLE) ||
                biomeKey.equals(Biomes.BAMBOO_JUNGLE) ||
                biomeKey.equals(Biomes.SAVANNA) ||
                biomeKey.equals(Biomes.SAVANNA_PLATEAU) ||
                biomeKey.equals(Biomes.WINDSWEPT_SAVANNA)) {
            return true;
        }

        // Kategorie-basierte Erkennung mit Temperaturcheck
        if (category == Biome.BiomeCategory.JUNGLE && climate.temperature > 0.7f) {
            return true;
        }

        if (category == Biome.BiomeCategory.SAVANNA &&
                climate.temperature > 0.8f && climate.downfall > 0.3f) {
            return true;
        }

        // String-basierte Erkennung für warme, feuchte Biome
        return (biomeName.contains("jungle") ||
                biomeName.contains("tropical") ||
                biomeName.contains("rainforest") ||
                (biomeName.contains("savanna") && climate.temperature > 0.8f)) &&
                climate.temperature > 0.7f && climate.downfall > 0.2f;
    }
}