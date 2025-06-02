package com.mods.plantsnmore.world.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class CoconutTreeDecorator extends TreeDecorator {

    public static final Codec<CoconutTreeDecorator> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(
                Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter((decorator) -> decorator.probability),
                Codec.intRange(1, 16).fieldOf("exclusion_radius_xz").forGetter((decorator) -> decorator.exclusionRadiusXZ),
                Codec.intRange(0, 16).fieldOf("exclusion_radius_y").forGetter((decorator) -> decorator.exclusionRadiusY),
                BlockStateProvider.CODEC.fieldOf("coconut_provider").forGetter((decorator) -> decorator.coconutProvider),
                Codec.intRange(1, 16).fieldOf("required_empty_blocks").forGetter((decorator) -> decorator.requiredEmptyBlocks),
                Direction.CODEC.listOf().fieldOf("directions").forGetter((decorator) -> decorator.directions)
        ).apply(instance, CoconutTreeDecorator::new);
    });

    private final float probability;
    private final int exclusionRadiusXZ;
    private final int exclusionRadiusY;
    private final BlockStateProvider coconutProvider;
    private final int requiredEmptyBlocks;
    private final List<Direction> directions;

    public CoconutTreeDecorator(float probability, int exclusionRadiusXZ, int exclusionRadiusY,
                                BlockStateProvider coconutProvider, int requiredEmptyBlocks, List<Direction> directions) {
        this.probability = probability;
        this.exclusionRadiusXZ = exclusionRadiusXZ;
        this.exclusionRadiusY = exclusionRadiusY;
        this.coconutProvider = coconutProvider;
        this.requiredEmptyBlocks = requiredEmptyBlocks;
        this.directions = directions;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreeDecorators.COCONUT_DECORATOR.get();
    }

    @Override
    public void place(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                      Random random, List<BlockPos> logPositions, List<BlockPos> leafPositions) {

        if (logPositions.isEmpty()) return;

        // Finde die Krone der Palme (oberste Log-Position)
        BlockPos trunkTop = logPositions.get(logPositions.size() - 1);

        // Platziere Kokosnüsse nur nahe dem Stamm der Palme, nicht bei allen Blättern
        placeCoconutsAroundTrunk(level, blockSetter, random, trunkTop);
    }

    private void placeCoconutsAroundTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                          Random random, BlockPos trunkTop) {

        // Anzahl der Kokosnüsse basierend auf Wahrscheinlichkeit
        int coconutCount = 0;
        for (int i = 0; i < 6; i++) { // Maximal 6 Kokosnüsse
            if (random.nextFloat() < this.probability) {
                coconutCount++;
            }
        }

        if (coconutCount == 0) return;

        // Platziere Kokosnüsse in einem Kreis um den Stamm herum
        for (int i = 0; i < coconutCount; i++) {
            // Zufällige Position um den Stamm herum
            int offsetX = random.nextInt(3) - 1; // -1, 0, oder 1
            int offsetZ = random.nextInt(3) - 1; // -1, 0, oder 1

            // Stelle sicher, dass wir nicht direkt auf dem Stamm sind
            if (offsetX == 0 && offsetZ == 0) {
                offsetX = random.nextBoolean() ? 1 : -1;
            }

            // Kokosnüsse hängen 1-2 Blöcke unter der Krone
            int hangDistance = 1 + random.nextInt(2);
            BlockPos coconutPos = trunkTop.offset(offsetX, -hangDistance, offsetZ);

            // Prüfe ob die Position frei ist und es sich um Luft handelt
            if (isValidCoconutPosition(level, coconutPos)) {
                BlockState coconutState = this.coconutProvider.getState(random, coconutPos);
                blockSetter.accept(coconutPos, coconutState);

                // Manchmal hängen Kokosnüsse in Gruppen
                if (random.nextFloat() < 0.3f) {
                    BlockPos belowPos = coconutPos.below();
                    if (isValidCoconutPosition(level, belowPos)) {
                        blockSetter.accept(belowPos, coconutState);
                    }
                }
            }
        }
    }

    private boolean isValidCoconutPosition(LevelSimulatedReader level, BlockPos pos) {
        // Prüfe ob die Position für eine Kokosnuss geeignet ist
        if (!Feature.isAir(level, pos)) {
            return false;
        }

        // Stelle sicher, dass darunter genug Platz ist (für hängende Kokosnüsse)
        for (int i = 1; i <= this.requiredEmptyBlocks; i++) {
            BlockPos checkPos = pos.below(i);
            if (!Feature.isAir(level, checkPos)) {
                // Es ist ok wenn der Boden erreicht wird, aber nicht andere Blöcke
                return i > 1; // Mindestens 1 Block Platz sollte vorhanden sein
            }
        }

        return true;
    }
}