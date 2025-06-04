package com.mods.plantsnmore.world.feature.tree;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class CurvedPalmTrunkPlacer extends TrunkPlacer {
    public static final Codec<CurvedPalmTrunkPlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return trunkPlacerParts(instance).and(instance.group(
                Codec.floatRange(0.1F, 3.0F).fieldOf("bend_strength").forGetter((placer) -> placer.bendStrength),
                Codec.intRange(-1, 5).fieldOf("bend_direction").forGetter((placer) -> placer.bendDirection),
                Codec.floatRange(0.0F, 1.0F).fieldOf("randomness").forGetter((placer) -> placer.randomness)
        )).apply(instance, CurvedPalmTrunkPlacer::new);
    });

    private final float bendStrength;
    private final int bendDirection; // -1 = random, 0 = North, 1 = South, 2 = East, 3 = West
    private final float randomness;

    public CurvedPalmTrunkPlacer(int baseHeight, int heightRandA, int heightRandB,
                                 float bendStrength, int bendDirection, float randomness) {
        super(baseHeight, heightRandA, heightRandB);
        this.bendStrength = bendStrength;
        this.bendDirection = bendDirection;
        this.randomness = randomness;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacers.CURVED_PALM_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                                            Random random, int height, BlockPos startPos, TreeConfiguration config) {

        List<FoliagePlacer.FoliageAttachment> foliageAttachments = Lists.newArrayList();

        // Determine bend direction with better randomization
        Direction bendDir = getBendDirection(random);

        // Add secondary curve direction for more natural S-curves
        Direction secondaryDir = null;
        if (bendStrength > 1.5f && random.nextFloat() < 0.6f) {
            secondaryDir = getPerpendicularDirection(bendDir, random);
        }

        double currentX = 0.0;
        double currentZ = 0.0;
        BlockPos currentPos = startPos;

        // Track curve intensity for natural tapering
        double maxCurveReached = 0.0;

        // Place the trunk with improved natural curve
        for (int i = 0; i < height; i++) {
            // Calculate curve progression with more natural curves
            double progress = (double) i / (double) height;

            // Primary curve - starts gentle, peaks in middle-upper section, then continues
            double primaryCurve = calculateNaturalCurve(progress, bendStrength);

            // Track maximum curve for coco wood placement
            maxCurveReached = Math.max(maxCurveReached, Math.abs(primaryCurve));

            // Add natural randomness with noise-like variation
            if (random.nextFloat() < randomness) {
                double noiseVariation = (random.nextGaussian() * 0.15) * bendStrength;
                primaryCurve += noiseVariation;
            }

            // Apply micro-adjustments for more organic growth
            if (i > 2 && random.nextFloat() < 0.3f) {
                primaryCurve += Math.sin(i * 0.8) * 0.1 * bendStrength;
            }

            // FIXED: Apply directional movement correctly by updating the variables
            currentX += getDirectionalMovementX(bendDir, primaryCurve);
            currentZ += getDirectionalMovementZ(bendDir, primaryCurve);

            // Apply secondary curve for S-shape if present
            if (secondaryDir != null && i > height * 0.4) {
                double secondaryCurve = Math.sin((progress - 0.4) * Math.PI * 1.5) * bendStrength * 0.4;
                currentX += getDirectionalMovementX(secondaryDir, secondaryCurve);
                currentZ += getDirectionalMovementZ(secondaryDir, secondaryCurve);
            }

            // Apply wind-like sway for very tall palms
            if (height > 8 && i > height * 0.6) {
                double swayIntensity = (progress - 0.6) * 0.3 * bendStrength;
                double swayX = Math.sin(i * 0.5) * swayIntensity;
                double swayZ = Math.cos(i * 0.7) * swayIntensity;
                currentX += swayX;
                currentZ += swayZ;
            }

            // Calculate final block position
            int blockX = (int) Math.round(currentX);
            int blockZ = (int) Math.round(currentZ);
            currentPos = startPos.offset(blockX, i, blockZ);

            // Determine block type based on position and curve intensity
            BlockState trunkBlock = getTrunkBlockForPosition(i, height, progress, maxCurveReached, config, random);

            // Place the trunk block
            blockSetter.accept(currentPos, trunkBlock);

            // Add foliage attachments for crown area with better distribution
            if (shouldAddFoliageAttachment(i, height, progress)) {
                foliageAttachments.add(new FoliagePlacer.FoliageAttachment(currentPos.above(), 0, false));
            }

            // Add supporting structure for heavily curved sections
            addSupportingBlocks(level, blockSetter, currentPos, progress, maxCurveReached, config, random);
        }

        // Always add the final crown position
        foliageAttachments.add(new FoliagePlacer.FoliageAttachment(currentPos.above(), 0, false));

        return foliageAttachments;
    }

    private Direction getBendDirection(Random random) {
        if (bendDirection == -1) {
            return Direction.Plane.HORIZONTAL.getRandomDirection(random);
        } else {
            // Ensure valid direction mapping
            Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
            int dirIndex = Math.max(0, Math.min(3, bendDirection));
            return directions[dirIndex];
        }
    }

    private Direction getPerpendicularDirection(Direction primary, Random random) {
        switch (primary) {
            case NORTH:
            case SOUTH:
                return random.nextBoolean() ? Direction.EAST : Direction.WEST;
            case EAST:
            case WEST:
                return random.nextBoolean() ? Direction.NORTH : Direction.SOUTH;
            default:
                return Direction.EAST;
        }
    }

    private double calculateNaturalCurve(double progress, float strength) {
        // Natural palm curve: gentle start, stronger middle, continuing to top
        double baseCurve = Math.pow(progress, 1.5) * strength;

        // Add natural growth variation
        double growthVariation = Math.sin(progress * Math.PI * 0.8) * 0.2;

        return baseCurve + (growthVariation * strength);
    }

    // FIXED: Separate methods for X and Z movement
    private double getDirectionalMovementX(Direction direction, double intensity) {
        switch (direction) {
            case EAST:
                return intensity;
            case WEST:
                return -intensity;
            default:
                return 0.0;
        }
    }

    private double getDirectionalMovementZ(Direction direction, double intensity) {
        switch (direction) {
            case SOUTH:
                return intensity;
            case NORTH:
                return -intensity;
            default:
                return 0.0;
        }
    }

    private BlockState getTrunkBlockForPosition(int currentHeight, int totalHeight, double progress,
                                                double maxCurve, TreeConfiguration config, Random random) {

        // Use coco palm wood for curved sections and upper trunk
        boolean useCocoPalmWood = false;

        // Upper portion of trunk (crown area)
        if (progress > 0.7) {
            useCocoPalmWood = true;
        }

        // Heavily curved sections
        else if (maxCurve > 1.0 && progress > 0.4) {
            useCocoPalmWood = true;
        }

        // Very curved sections always use coco wood
        else if (maxCurve > 2.0 && progress > 0.2) {
            useCocoPalmWood = true;
        }

        // Random transition for natural look
        else if (progress > 0.5 && random.nextFloat() < 0.3) {
            useCocoPalmWood = true;
        }

        if (useCocoPalmWood) {
            // Assuming you have a coco palm wood block - replace with your actual block
            // return ModBlocks.COCO_PALM_WOOD.get().defaultBlockState();
            // For now, using the regular trunk provider
            return config.trunkProvider.getState(random, new BlockPos(0, currentHeight, 0));
        }

        return config.trunkProvider.getState(random, new BlockPos(0, currentHeight, 0));
    }

    private boolean shouldAddFoliageAttachment(int currentHeight, int totalHeight, double progress) {
        // Add foliage attachments in the crown area with better distribution
        if (progress >= 0.75) return true;  // Upper quarter always
        if (progress >= 0.6 && currentHeight % 2 == 0) return true;  // Every other block in upper section
        return false;
    }

    private void addSupportingBlocks(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                     BlockPos trunkPos, double progress, double maxCurve,
                                     TreeConfiguration config, Random random) {

        // Add occasional supporting bark or root-like structures for very curved palms
        if (maxCurve > 2.0 && progress < 0.3 && random.nextFloat() < 0.15) {
            // Add small supporting blocks at the base for stability
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                if (random.nextFloat() < 0.4) {
                    BlockPos supportPos = trunkPos.relative(dir);
                    // Only place if position is reasonable
                    if (Math.abs(supportPos.getX() - trunkPos.getX()) <= 1 &&
                            Math.abs(supportPos.getZ() - trunkPos.getZ()) <= 1) {
                        blockSetter.accept(supportPos, config.trunkProvider.getState(random, supportPos));
                    }
                }
            }
        }
    }
}