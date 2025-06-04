package com.mods.plantsnmore.world.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.Random;
import java.util.function.BiConsumer;

public class PalmFoliagePlacer extends FoliagePlacer {
    public static final Codec<PalmFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return foliagePlacerParts(instance).and(instance.group(
                Codec.intRange(1, 16).fieldOf("frond_length_min").forGetter((placer) -> placer.frondLengthMin),
                Codec.intRange(1, 16).fieldOf("frond_length_max").forGetter((placer) -> placer.frondLengthMax),
                Codec.intRange(1, 4).fieldOf("density").forGetter((placer) -> placer.density)
        )).apply(instance, PalmFoliagePlacer::new);
    });

    private final int frondLengthMin;
    private final int frondLengthMax;
    private final int density;
    private static final BooleanProperty PERSISTENT = LeavesBlock.PERSISTENT;

    public PalmFoliagePlacer(IntProvider radius, IntProvider offset, int frondLengthMin, int frondLengthMax, int density) {
        super(radius, offset);
        this.frondLengthMin = frondLengthMin;
        this.frondLengthMax = frondLengthMax;
        this.density = density;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacers.PALM_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> foliageSetter, Random random,
                                 TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment,
                                 int foliageHeight, int foliageRadius, int offset) {

        BlockPos centerPos = attachment.pos();

        // Create palm fronds in multiple directions - more natural distribution
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

        // Number of fronds based on density - more realistic palm tree
        int baseFramonds = Math.min(8, 4 + density);
        int numFronds = baseFramonds + random.nextInt(3);

        // Create main fronds first (cardinal directions)
        for (int i = 0; i < 4; i++) {
            Direction direction = directions[i];
            int frondLength = frondLengthMin + random.nextInt(Math.max(1, frondLengthMax - frondLengthMin + 1));

            // Add some length variation for natural look
            frondLength += random.nextInt(3) - 1;
            frondLength = Math.max(frondLengthMin, Math.min(frondLengthMax, frondLength));

            createPalmFrond(level, foliageSetter, random, config, centerPos, direction, frondLength, true);
        }

        // Create additional fronds for denser palms (diagonal and varied directions)
        for (int frondIndex = 4; frondIndex < numFronds; frondIndex++) {
            // Safe array access - use modulo with positive result
            int dirIndex = Math.abs(frondIndex + random.nextInt(4)) % 4;
            Direction baseDirection = directions[dirIndex];

            // Create intermediate directions for more natural spread
            Direction actualDirection = baseDirection;
            if (random.nextFloat() < 0.4f) {
                // Rotate slightly for more natural distribution
                int rotationSteps = random.nextInt(3) - 1; // -1, 0, or 1
                actualDirection = rotateDirection(baseDirection, rotationSteps);
            }

            int frondLength = frondLengthMin + random.nextInt(Math.max(1, frondLengthMax - frondLengthMin + 1));
            // Secondary fronds are typically shorter
            frondLength = Math.max(frondLengthMin, frondLength - 1 - random.nextInt(2));

            createPalmFrond(level, foliageSetter, random, config, centerPos, actualDirection, frondLength, false);
        }

        // Add some small leaves near the center for fullness
        createCenterFoliage(level, foliageSetter, random, config, centerPos);
    }

    private BlockState getPersistentLeafState(Random random, TreeConfiguration config, BlockPos pos) {
        BlockState leafState = config.foliageProvider.getState(random, pos);
        if (leafState.hasProperty(PERSISTENT)) {
            return leafState.setValue(PERSISTENT, true);
        }
        return leafState;
    }

    private Direction rotateDirection(Direction direction, int steps) {
        if (steps == 0) return direction;

        Direction[] horizontalDirections = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

        for (int i = 0; i < horizontalDirections.length; i++) {
            if (horizontalDirections[i] == direction) {
                int newIndex = (i + steps) % 4;
                if (newIndex < 0) newIndex += 4; // Handle negative modulo
                return horizontalDirections[newIndex];
            }
        }
        return direction;
    }

    private void createCenterFoliage(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> foliageSetter,
                                     Random random, TreeConfiguration config, BlockPos centerPos) {
        // Add small leaves around the center for a fuller look
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (random.nextFloat() < 0.6f) {
                BlockPos leafPos = centerPos.relative(dir);
                if (!this.shouldSkipLocation(random, leafPos.getX(), leafPos.getY(), leafPos.getZ(),
                        leafPos.getY(), false)) {
                    // FIXED: Only place if the position is suitable for leaves
                    if (canPlaceLeafAt(level, leafPos)) {
                        foliageSetter.accept(leafPos, getPersistentLeafState(random, config, leafPos));
                    }
                }
            }
        }

        // Sometimes add leaves above center
        if (random.nextFloat() < 0.3f) {
            BlockPos abovePos = centerPos.above();
            if (canPlaceLeafAt(level, abovePos)) {
                    foliageSetter.accept(abovePos, getPersistentLeafState(random, config, abovePos));
            }
        }
    }

    // FIXED: Add proper leaf placement validation
    private boolean canPlaceLeafAt(LevelSimulatedReader level, BlockPos pos) {
        return level.isStateAtPosition(pos, state ->
                state.isAir() ||
                        state.is(BlockTags.REPLACEABLE_PLANTS) ||
                        state.is(BlockTags.LEAVES) ||
                        state.getMaterial().isReplaceable()
        );
    }

    private void createPalmFrond(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> foliageSetter, Random random,
                                 TreeConfiguration config, BlockPos startPos, Direction direction, int length, boolean isMainFrond) {

        BlockPos currentPos = startPos;
        int actualLength = Math.max(2, length);

        // First segment - base of frond (always place these)
        for (int i = 1; i <= Math.min(2, actualLength); i++) {
            currentPos = startPos.relative(direction, i);
            if (canPlaceLeafAt(level, currentPos)) {
                foliageSetter.accept(currentPos, getPersistentLeafState(random, config, currentPos));
            }

            // Add some small side leaves at the base
            if (i == 1 && random.nextFloat() < 0.4f) {
                Direction leftDir = direction.getCounterClockWise();
                Direction rightDir = direction.getClockWise();

                if (random.nextBoolean()) {
                    BlockPos sidePos = currentPos.relative(leftDir);
                    if (canPlaceLeafAt(level, sidePos)) {

                        foliageSetter.accept(sidePos, getPersistentLeafState(random, config, sidePos));
                    }
                }
                if (random.nextBoolean()) {
                    BlockPos sidePos = currentPos.relative(rightDir);
                    if (canPlaceLeafAt(level, sidePos)) {
                        foliageSetter.accept(sidePos, getPersistentLeafState(random, config, sidePos));
                    }
                }
            }
        }

        // Middle segment - main frond with curve and side leaves
        if (actualLength > 2) {
            Direction leftDir = direction.getCounterClockWise();
            Direction rightDir = direction.getClockWise();
            boolean curveLeft = random.nextBoolean();
            int curveStartPoint = actualLength / 3 + 1;

            for (int i = 3; i <= actualLength; i++) {
                // Add progressive curve for natural droop
                if (i >= curveStartPoint && random.nextFloat() < 0.3f + (i - curveStartPoint) * 0.1f) {
                    Direction curveDir = curveLeft ? leftDir : rightDir;
                    currentPos = currentPos.relative(curveDir);
                }

                // Continue outward
                currentPos = currentPos.relative(direction);

                // Progressive droop - more droop towards the end
                float droopChance = Math.max(0.1f, (i - actualLength / 2f) / actualLength * 0.4f);
                if (i > actualLength / 2 && random.nextFloat() < droopChance) {
                    currentPos = currentPos.below();
                }

                // Place main frond leaf with validation
                if (!this.shouldSkipLocation(random, currentPos.getX(), currentPos.getY(), currentPos.getZ(),
                        currentPos.getY(), false) && canPlaceLeafAt(level, currentPos)) {
                    foliageSetter.accept(currentPos, getPersistentLeafState(random, config, currentPos));
                }

                // Add side leaves with higher density for main fronds
                float sideLeafChance = isMainFrond ? 0.4f : 0.25f;
                if (random.nextFloat() < sideLeafChance) {
                    Direction sideDir = random.nextBoolean() ? leftDir : rightDir;
                    BlockPos sidePos = currentPos.relative(sideDir);

                    if (!this.shouldSkipLocation(random, sidePos.getX(), sidePos.getY(), sidePos.getZ(),
                            sidePos.getY(), false) && canPlaceLeafAt(level, sidePos)) {
                        foliageSetter.accept(sidePos, getPersistentLeafState(random, config, sidePos));
                    }
                }

                // Occasionally add leaves on both sides for fuller look
                if (isMainFrond && i <= actualLength - 2 && random.nextFloat() < 0.2f) {
                    BlockPos leftPos = currentPos.relative(leftDir);
                    BlockPos rightPos = currentPos.relative(rightDir);

                    if (random.nextBoolean() && canPlaceLeafAt(level, leftPos)) {
                            foliageSetter.accept(leftPos, getPersistentLeafState(random, config, leftPos));
                    }
                    if (random.nextBoolean() && canPlaceLeafAt(level, rightPos)) {
                            foliageSetter.accept(rightPos, getPersistentLeafState(random, config, rightPos));
                    }
                }
            }
        }

        // End segment - natural droop with decreasing density
        if (actualLength > 3 && random.nextFloat() < 0.8f) {
            int droopLength = 1 + random.nextInt(Math.min(3, actualLength / 3));

            for (int i = 0; i < droopLength; i++) {
                currentPos = currentPos.below();

                // Decreasing chance of leaves as we droop further
                float leafChance = 0.8f - (i * 0.2f);
                if (random.nextFloat() < leafChance && canPlaceLeafAt(level, currentPos)) {
                    foliageSetter.accept(currentPos, getPersistentLeafState(random, config, currentPos));
                }

                // Sometimes add a final side leaf
                if (i == droopLength - 1 && random.nextFloat() < 0.3f) {
                    Direction finalDir = random.nextBoolean() ?
                            direction.getCounterClockWise() : direction.getClockWise();
                    BlockPos finalPos = currentPos.relative(finalDir);
                    if (canPlaceLeafAt(level, finalPos)) {
                        foliageSetter.accept(finalPos, getPersistentLeafState(random, config, finalPos));
                    }
                }
            }
        }
    }

    @Override
    public int foliageHeight(Random random, int height, TreeConfiguration config) {
        return 4 + random.nextInt(2); // Slightly more height variation
    }

    @Override
    protected boolean shouldSkipLocation(Random random, int localX, int localY, int localZ, int relativeY, boolean giantTrunk) {
        // FIXED: Much more lenient skipping for palm trees - they should be quite full
        // Reduced skip chance significantly to prevent leaves from being removed
        return random.nextFloat() < 0.01f;
    }
}