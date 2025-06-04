package com.mods.plantsnmore.world.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
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
                BlockStateProvider.CODEC.fieldOf("provider").forGetter((decorator) -> decorator.provider),
                Codec.intRange(1, 127).fieldOf("required_empty_blocks").forGetter((decorator) -> decorator.requiredEmptyBlocks),
                Direction.CODEC.listOf().fieldOf("directions").forGetter((decorator) -> decorator.directions)
        ).apply(instance, CoconutTreeDecorator::new);
    });

    private final float probability;
    private final int exclusionRadiusXZ;
    private final int exclusionRadiusY;
    private final BlockStateProvider provider;
    private final int requiredEmptyBlocks;
    private final List<Direction> directions;

    public CoconutTreeDecorator(float probability, int exclusionRadiusXZ, int exclusionRadiusY,
                                BlockStateProvider provider, int requiredEmptyBlocks, List<Direction> directions) {
        this.probability = probability;
        this.exclusionRadiusXZ = exclusionRadiusXZ;
        this.exclusionRadiusY = exclusionRadiusY;
        this.provider = provider;
        this.requiredEmptyBlocks = requiredEmptyBlocks;
        this.directions = directions;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreeDecorators.COCONUT_TREE_DECORATOR.get();
    }

    @Override
    public void place(LevelSimulatedReader level, BiConsumer<BlockPos, net.minecraft.world.level.block.state.BlockState> blockSetter,
                      Random random, List<BlockPos> logPositions, List<BlockPos> leafPositions) {

        // FIXED: Always try to place coconuts, don't skip based on probability here
        // The probability check should be per coconut, not for the entire decorator
        if (logPositions.isEmpty()) {
            return;
        }

        // Find the top of the trunk (highest log position)
        BlockPos topLog = logPositions.stream()
                .max((pos1, pos2) -> Integer.compare(pos1.getY(), pos2.getY()))
                .orElse(null);

        if (topLog == null) return;

        // FIXED: Also consider positions 1-2 blocks below the top for better coconut placement
        BlockPos primaryCoconutPos = topLog;
        BlockPos secondaryCoconutPos = topLog.below();

        // Place coconuts near the top of the palm
        for (Direction direction : this.directions) {
            if (direction == Direction.DOWN) {
                // Place coconuts hanging below the trunk top
                for (int i = 1; i <= 3; i++) {
                    BlockPos coconutPos = topLog.relative(direction, i);

                    // FIXED: Simplified space checking - just check if the position is air
                    if (level.isStateAtPosition(coconutPos, (state) -> state.isAir()) && random.nextFloat() < 0.5f) {
                        blockSetter.accept(coconutPos, this.provider.getState(random, coconutPos));
                    }
                }
            }
        }
    }
}