package com.mods.plantsnmore.world.feature;

import com.mods.plantsnmore.PlantsnMore;
import com.mods.plantsnmore.block.ModBlocks;
import com.mods.plantsnmore.world.feature.tree.CoconutTreeDecorator;
import com.mods.plantsnmore.world.feature.tree.PalmFoliagePlacer;
import com.mods.plantsnmore.world.feature.tree.CurvedPalmTrunkPlacer;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModConfiguredFeatures {

    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, PlantsnMore.MOD_ID);

    // FIXED: Create supplier functions instead of accessing .get() during static initialization
    private static List<OreConfiguration.TargetBlockState> getOverworldWormCastingTargets() {
        return List.of(
                OreConfiguration.target(new BlockMatchTest(Blocks.DIRT), ModBlocks.WORM_CASTING.get().defaultBlockState()),
                OreConfiguration.target(new BlockMatchTest(Blocks.COARSE_DIRT), ModBlocks.WORM_CASTING.get().defaultBlockState()),
                OreConfiguration.target(new BlockMatchTest(Blocks.ROOTED_DIRT), ModBlocks.WORM_CASTING.get().defaultBlockState())
        );
    }

    public static final RegistryObject<ConfiguredFeature<?, ?>> WORM_CASTING = CONFIGURED_FEATURES.register("worm_casting",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(getOverworldWormCastingTargets(), 7)));

    // FIXED: Same fix for Perlite
    private static List<OreConfiguration.TargetBlockState> getOverworldPerliteTargets() {
        return List.of(
                OreConfiguration.target(new BlockMatchTest(Blocks.SAND), ModBlocks.PERLITE.get().defaultBlockState()),
                OreConfiguration.target(new BlockMatchTest(Blocks.SANDSTONE), ModBlocks.PERLITE.get().defaultBlockState()),
                OreConfiguration.target(new BlockMatchTest(Blocks.RED_SAND), ModBlocks.PERLITE.get().defaultBlockState()),
                OreConfiguration.target(new BlockMatchTest(Blocks.RED_SANDSTONE), ModBlocks.PERLITE.get().defaultBlockState())
        );
    }

    public static final RegistryObject<ConfiguredFeature<?, ?>> PERLITE = CONFIGURED_FEATURES.register("perlite",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(getOverworldPerliteTargets(), 6)));

    // ========================= STRAIGHT PALM TREES =========================

    // Kleine Kokospalme (4-6 Blöcke hoch) - keine Kokosnüsse
    public static final RegistryObject<ConfiguredFeature<?, ?>> COCO_PALM_SMALL = CONFIGURED_FEATURES.register("coco_palm_small",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LOG.get()),
                    new StraightTrunkPlacer(4, 2, 0), // 4-6 Blöcke hoch
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LEAVES.get()),
                    new PalmFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 4, 6, 1), // density = 1 für bessere Stabilität
                    new TwoLayersFeatureSize(1, 0, 1))
                    .ignoreVines() // Verhindert das Droppen von Blättern
                    .build()));

    // Mittelgroße Kokospalme (6-9 Blöcke hoch) - keine Kokosnüsse
    public static final RegistryObject<ConfiguredFeature<?, ?>> COCO_PALM_MEDIUM = CONFIGURED_FEATURES.register("coco_palm_medium",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LOG.get()),
                    new StraightTrunkPlacer(6, 3, 0), // 6-9 Blöcke hoch
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LEAVES.get()),
                    new PalmFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), 6, 8, 2), // density = 2
                    new TwoLayersFeatureSize(1, 0, 2))
                    .ignoreVines()
                    .build()));

    // Große Kokospalme (8-12 Blöcke hoch) - mit Kokosnüssen
    public static final RegistryObject<ConfiguredFeature<?, ?>> COCO_PALM_LARGE = CONFIGURED_FEATURES.register("coco_palm_large",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LOG.get()),
                    new StraightTrunkPlacer(8, 4, 0), // 8-12 Blöcke hoch
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LEAVES.get()),
                    new PalmFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), 8, 10, 2), // längere Wedel
                    new TwoLayersFeatureSize(2, 0, 2))
                    .decorators(List.of(
                            new CoconutTreeDecorator(0.8f, 2, 1,
                                    BlockStateProvider.simple(Blocks.BROWN_WOOL),
                                    1, List.of(Direction.DOWN))))
                    .ignoreVines()
                    .build()));

    // Königspalme - sehr groß und imposant (10-16 Blöcke hoch) - viele Kokosnüsse
    public static final RegistryObject<ConfiguredFeature<?, ?>> COCO_PALM_ROYAL = CONFIGURED_FEATURES.register("coco_palm_royal",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LOG.get()),
                    new StraightTrunkPlacer(10, 6, 0), // 10-16 Blöcke hoch
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LEAVES.get()),
                    new PalmFoliagePlacer(ConstantInt.of(5), ConstantInt.of(1), 10, 12, 3), // density = 3 für volle Krone
                    new TwoLayersFeatureSize(3, 1, 3))
                    .decorators(List.of(
                            new CoconutTreeDecorator(0.9f, 3, 2,
                                    BlockStateProvider.simple(Blocks.BROWN_WOOL),
                                    1, List.of(Direction.DOWN))))
                    .ignoreVines()
                    .build()));

    // ========================= CURVED PALM TREES =========================

    // Kleine gekrümmte Palme - leichte Krümmung
    public static final RegistryObject<ConfiguredFeature<?, ?>> CURVED_PALM_SMALL_LIGHT = CONFIGURED_FEATURES.register("curved_palm_small_light",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LOG.get()),
                    new CurvedPalmTrunkPlacer(4, 2, 0,
                            0.3f, -1, 0.2f), // leichte Krümmung, zufällige Richtung
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LEAVES.get()),
                    new PalmFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 4, 6, 1),
                    new TwoLayersFeatureSize(1, 0, 1))
                    .ignoreVines()
                    .build()));

    // Mittelgroße gekrümmte Palme - mittlere Krümmung
    public static final RegistryObject<ConfiguredFeature<?, ?>> CURVED_PALM_MEDIUM_NORMAL = CONFIGURED_FEATURES.register("curved_palm_medium_normal",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LOG.get()),
                    new CurvedPalmTrunkPlacer(6, 3, 0,
                            0.7f, -1, 0.3f), // mittlere Krümmung
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LEAVES.get()),
                    new PalmFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), 6, 8, 2),
                    new TwoLayersFeatureSize(1, 0, 2))
                    .ignoreVines()
                    .build()));

    // Große gekrümmte Palme - starke Krümmung nach Osten
    public static final RegistryObject<ConfiguredFeature<?, ?>> CURVED_PALM_LARGE_STRONG_EAST = CONFIGURED_FEATURES.register("curved_palm_large_strong_east",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LOG.get()),
                    new CurvedPalmTrunkPlacer(8, 4, 0,
                            1.2f, 2, 0.1f), // starke Krümmung nach Osten (Direction.EAST = 2)
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LEAVES.get()),
                    new PalmFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), 8, 10, 2),
                    new TwoLayersFeatureSize(2, 0, 2))
                    .decorators(List.of(
                            new CoconutTreeDecorator(0.7f, 2, 1,
                                    BlockStateProvider.simple(Blocks.BROWN_WOOL),
                                    1, List.of(Direction.DOWN))))
                    .ignoreVines()
                    .build()));

    // Große gekrümmte Palme - starke Krümmung nach Westen
    public static final RegistryObject<ConfiguredFeature<?, ?>> CURVED_PALM_LARGE_STRONG_WEST = CONFIGURED_FEATURES.register("curved_palm_large_strong_west",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LOG.get()),
                    new CurvedPalmTrunkPlacer(8, 4, 0,
                            1.2f, 3, 0.1f), // starke Krümmung nach Westen (Direction.WEST = 3)
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LEAVES.get()),
                    new PalmFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), 8, 10, 2),
                    new TwoLayersFeatureSize(2, 0, 2))
                    .decorators(List.of(
                            new CoconutTreeDecorator(0.7f, 2, 1,
                                    BlockStateProvider.simple(Blocks.BROWN_WOOL),
                                    1, List.of(Direction.DOWN))))
                    .ignoreVines()
                    .build()));

    // Extreme gekrümmte Königspalme - sehr starke S-Kurve
    public static final RegistryObject<ConfiguredFeature<?, ?>> CURVED_PALM_ROYAL_EXTREME = CONFIGURED_FEATURES.register("curved_palm_royal_extreme",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LOG.get()),
                    new CurvedPalmTrunkPlacer(12, 4, 0,
                            1.8f, -1, 0.4f), // extreme Krümmung mit hoher Variation
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LEAVES.get()),
                    new PalmFoliagePlacer(ConstantInt.of(5), ConstantInt.of(1), 10, 14, 3),
                    new TwoLayersFeatureSize(3, 1, 3))
                    .decorators(List.of(
                            new CoconutTreeDecorator(0.9f, 4, 2,
                                    BlockStateProvider.simple(Blocks.BROWN_WOOL),
                                    1, List.of(Direction.DOWN))))
                    .ignoreVines()
                    .build()));

    // Windgebeugte Palme - mittlere Größe, starke Krümmung nach Süden (wie vom Nordwind gebeugt)
    public static final RegistryObject<ConfiguredFeature<?, ?>> CURVED_PALM_WINDSWEPT = CONFIGURED_FEATURES.register("curved_palm_windswept",
            () -> new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LOG.get()),
                    new CurvedPalmTrunkPlacer(7, 2, 0,
                            1.0f, 1, 0.15f), // Krümmung nach Süden (Direction.SOUTH = 1)
                    BlockStateProvider.simple(ModBlocks.COCO_PALM_LEAVES.get()),
                    new PalmFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), 7, 9, 2),
                    new TwoLayersFeatureSize(2, 0, 2))
                    .ignoreVines()
                    .build()));

    // ========================= PALM GROUPS =========================
    // NOTE: These groups will be referenced by placed features, not inline

    public static void register(IEventBus eventBus) {
        CONFIGURED_FEATURES.register(eventBus);
    }
}