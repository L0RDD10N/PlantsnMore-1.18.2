package com.mods.plantsnmore.world.feature;

import com.mods.plantsnmore.PlantsnMore;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModPlacedFeatures {

    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, PlantsnMore.MOD_ID);

    // ========================= ORE PLACED FEATURES =========================

    public static final RegistryObject<PlacedFeature> WORM_CASTING_PLACED = PLACED_FEATURES.register("worm_casting_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.WORM_CASTING.getHolder().get(),
                    List.of(
                            CountPlacement.of(8), // 8 veins per chunk
                            InSquarePlacement.spread(),
                            HeightRangePlacement.uniform(
                                    net.minecraft.world.level.levelgen.VerticalAnchor.absolute(0),
                                    net.minecraft.world.level.levelgen.VerticalAnchor.absolute(64)
                            ),
                            BiomeFilter.biome()
                    )));

    public static final RegistryObject<PlacedFeature> PERLITE_PLACED = PLACED_FEATURES.register("perlite_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.PERLITE.getHolder().get(),
                    List.of(
                            CountPlacement.of(6), // 6 veins per chunk
                            InSquarePlacement.spread(),
                            HeightRangePlacement.uniform(
                                    net.minecraft.world.level.levelgen.VerticalAnchor.absolute(0),
                                    net.minecraft.world.level.levelgen.VerticalAnchor.absolute(80)
                            ),
                            BiomeFilter.biome()
                    )));

    // ========================= PALM TREE PLACED FEATURES =========================

    // Beach palm trees - mixed sizes for natural variety
    public static final RegistryObject<PlacedFeature> COCO_PALM_BEACH_PLACED = PLACED_FEATURES.register("coco_palm_beach_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.COCO_PALM_MEDIUM.getHolder().get(),
                    List.of(
                            RarityFilter.onAverageOnceEvery(3), // Every 3rd chunk on average
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                            BiomeFilter.biome()
                    )));

    // Small palm groves - multiple small palms
    public static final RegistryObject<PlacedFeature> COCO_PALM_SMALL_GROVE_PLACED = PLACED_FEATURES.register("coco_palm_small_grove_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.COCO_PALM_SMALL.getHolder().get(),
                    List.of(
                            CountPlacement.of(2), // 2 trees per placement
                            InSquarePlacement.spread(),
                            SurfaceWaterDepthFilter.forMaxDepth(0), // Only on land
                            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                            BiomeFilter.biome()
                    )));

    // Large royal palms - rare and majestic
    public static final RegistryObject<PlacedFeature> COCO_PALM_ROYAL_PLACED = PLACED_FEATURES.register("coco_palm_royal_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.COCO_PALM_ROYAL.getHolder().get(),
                    List.of(
                            RarityFilter.onAverageOnceEvery(8), // Very rare
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                            BiomeFilter.biome()
                    )));

    // Curved palms for variety
    public static final RegistryObject<PlacedFeature> CURVED_PALM_MIXED_PLACED = PLACED_FEATURES.register("curved_palm_mixed_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.CURVED_PALM_MEDIUM_NORMAL.getHolder().get(),
                    List.of(
                            RarityFilter.onAverageOnceEvery(4), // Moderately rare
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                            BiomeFilter.biome()
                    )));

    // Windswept palms for coastal areas
    public static final RegistryObject<PlacedFeature> CURVED_PALM_WINDSWEPT_PLACED = PLACED_FEATURES.register("curved_palm_windswept_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.CURVED_PALM_WINDSWEPT.getHolder().get(),
                    List.of(
                            RarityFilter.onAverageOnceEvery(6),
                            InSquarePlacement.spread(),
                            SurfaceWaterDepthFilter.forMaxDepth(1), // Near water
                            PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                            BiomeFilter.biome()
                    )));

    // Extreme curved palms - very rare showcase trees
    public static final RegistryObject<PlacedFeature> CURVED_PALM_EXTREME_PLACED = PLACED_FEATURES.register("curved_palm_extreme_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.CURVED_PALM_ROYAL_EXTREME.getHolder().get(),
                    List.of(
                            RarityFilter.onAverageOnceEvery(12), // Extremely rare
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                            BiomeFilter.biome()
                    )));

    // ========================= SPECIALIZED BIOME PLACED FEATURES =========================

    // Curved beach palms - for variety on beaches
    public static final RegistryObject<PlacedFeature> CURVED_PALM_BEACH_PLACED = PLACED_FEATURES.register("curved_palm_beach_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.CURVED_PALM_SMALL_LIGHT.getHolder().get(),
                    List.of(
                            RarityFilter.onAverageOnceEvery(5), // Less common than straight palms
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                            BiomeFilter.biome()
                    )));

    // Storm palms for windy coastal areas
    public static final RegistryObject<PlacedFeature> STORM_PALM_COAST_PLACED = PLACED_FEATURES.register("storm_palm_coast_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.CURVED_PALM_WINDSWEPT.getHolder().get(),
                    List.of(
                            RarityFilter.onAverageOnceEvery(4), // Fairly common in stormy areas
                            InSquarePlacement.spread(),
                            SurfaceWaterDepthFilter.forMaxDepth(2), // Near coastlines
                            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                            BiomeFilter.biome()
                    )));

    // Oasis palms for desert areas - rare but clustered
    public static final RegistryObject<PlacedFeature> COCO_PALM_OASIS_PLACED = PLACED_FEATURES.register("coco_palm_oasis_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.COCO_PALM_LARGE.getHolder().get(),
                    List.of(
                            RarityFilter.onAverageOnceEvery(15), // Very rare oases
                            CountPlacement.of(3), // 3 palms per oasis for clustering effect
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                            BiomeFilter.biome()
                    )));

    // Tropical jungle palms - frequent in warm, humid areas
    public static final RegistryObject<PlacedFeature> COCO_PALM_TROPICAL_PLACED = PLACED_FEATURES.register("coco_palm_tropical_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.COCO_PALM_ROYAL.getHolder().get(),
                    List.of(
                            RarityFilter.onAverageOnceEvery(2), // More frequent in tropical areas
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                            BiomeFilter.biome()
                    )));

    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }
}