package com.mods.plantsnmore.world.feature;

import com.mods.plantsnmore.PlantsnMore;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
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

    // ========================= TREE PLACED FEATURES =========================

    // Tropical Palm Group - Mixed variety of palms
    public static final RegistryObject<PlacedFeature> TROPICAL_PALM_GROUP_PLACED = PLACED_FEATURES.register("tropical_palm_group_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.TROPICAL_PALM_GROUP.getHolder().get(),
                    List.of(
                            CountPlacement.of(2), // 2 groups per chunk
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                            BiomeFilter.biome()
                    )));

    // Coconut Palm Grove - Focused on coconut-bearing trees
    public static final RegistryObject<PlacedFeature> COCONUT_PALM_GROVE_PLACED = PLACED_FEATURES.register("coconut_palm_grove_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.COCONUT_PALM_GROVE.getHolder().get(),
                    List.of(
                            CountPlacement.of(1), // 1 grove per chunk
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                            BiomeFilter.biome()
                    )));

    // Beach Palm Cluster - Windswept and curved palms for coastal areas
    public static final RegistryObject<PlacedFeature> BEACH_PALM_CLUSTER_PLACED = PLACED_FEATURES.register("beach_palm_cluster_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.BEACH_PALM_CLUSTER.getHolder().get(),
                    List.of(
                            CountPlacement.of(3), // 3 clusters per chunk
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                            BiomeFilter.biome()
                    )));

    // Windswept Palm Group - Heavily curved palms for windy areas
    public static final RegistryObject<PlacedFeature> WINDSWEPT_PALM_GROUP_PLACED = PLACED_FEATURES.register("windswept_palm_group_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.WINDSWEPT_PALM_GROUP.getHolder().get(),
                    List.of(
                            CountPlacement.of(2), // 2 groups per chunk
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                            BiomeFilter.biome()
                    )));

    // Desert Oasis Palms - Rare, majestic palms for desert oases
    public static final RegistryObject<PlacedFeature> DESERT_OASIS_PALMS_PLACED = PLACED_FEATURES.register("desert_oasis_palms_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.DESERT_OASIS_PALMS.getHolder().get(),
                    List.of(
                            CountPlacement.of(1), // 1 oasis per chunk (rare)
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                            BiomeFilter.biome(),
                            RarityFilter.onAverageOnceEvery(10) // Make it rare
                    )));

    // Alternative version that was in your original code
    public static final RegistryObject<PlacedFeature> TROPICAL_PALM_GROUP_COMMON = PLACED_FEATURES.register("tropical_palm_group_common",
            () -> new PlacedFeature(ModConfiguredFeatures.TROPICAL_PALM_GROUP.getHolder().get(),
                    List.of(
                            CountPlacement.of(2), // 2 groups per chunk
                            InSquarePlacement.spread(),
                            PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                            BiomeFilter.biome()
                    )));

    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }
}