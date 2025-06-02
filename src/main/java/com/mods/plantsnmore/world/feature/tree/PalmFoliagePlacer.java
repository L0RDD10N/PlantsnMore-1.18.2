package com.mods.plantsnmore.world.feature.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.material.Fluids;

import java.util.Random;
import java.util.function.BiConsumer;

public class PalmFoliagePlacer extends FoliagePlacer {

    public static final Codec<PalmFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return foliagePlacerParts(instance).and(
                instance.group(
                        Codec.intRange(3, 20).fieldOf("frond_length").forGetter((placer) -> placer.frondLength),
                        Codec.intRange(4, 16).fieldOf("frond_count").forGetter((placer) -> placer.frondCount),
                        Codec.intRange(0, 3).fieldOf("density").forGetter((placer) -> placer.density)
                )
        ).apply(instance, PalmFoliagePlacer::new);
    });

    private final int frondLength;
    private final int frondCount;
    private final int density; // 0 = sparse, 3 = very dense

    public PalmFoliagePlacer(IntProvider radius, IntProvider offset, int frondLength, int frondCount, int density) {
        super(radius, offset);
        this.frondLength = frondLength;
        this.frondCount = frondCount;
        this.density = density;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacers.PALM_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                 Random random, TreeConfiguration config, int maxFreeTreeHeight,
                                 FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {

        BlockPos center = attachment.pos();

        // Create stable crown center
        createCrownCenter(level, blockSetter, random, config, center);

        // Create symmetric palm fronds
        createSymmetricPalmFronds(level, blockSetter, random, config, center);
    }

    private void createCrownCenter(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                   Random random, TreeConfiguration config, BlockPos center) {
        // Central leaves around the trunk for stability
        tryPlaceLeaf(level, blockSetter, random, config, center);

        // Small ring around center
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos ringPos = center.relative(dir);
            if (random.nextFloat() < 0.7f) {
                tryPlaceLeaf(level, blockSetter, random, config, ringPos);
            }
        }

        // Layer above for fuller crown
        BlockPos above = center.above();
        if (random.nextFloat() < 0.6f) {
            tryPlaceLeaf(level, blockSetter, random, config, above);
        }
    }

    private void createSymmetricPalmFronds(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                           Random random, TreeConfiguration config, BlockPos center) {

        // Calculate optimal angle distribution for symmetric arrangement
        float angleStep = 360.0f / frondCount;
        float baseAngleOffset = random.nextFloat() * angleStep; // Randomize starting angle

        for (int i = 0; i < frondCount; i++) {
            float angle = baseAngleOffset + (i * angleStep) + (random.nextFloat() * 10.0f - 5.0f); // Small random variation
            double radians = Math.toRadians(angle);

            // Calculate direction vector
            double dirX = Math.cos(radians);
            double dirZ = Math.sin(radians);

            // Create frond with variable length
            int frondLen = calculateFrondLength(random);
            createPalmFrond(level, blockSetter, random, config, center, dirX, dirZ, frondLen, i);
        }
    }

    private int calculateFrondLength(Random random) {
        // Base length with controlled variation
        int variation = Math.max(1, frondLength / 5);
        int minLength = Math.max(3, frondLength - variation);
        int maxLength = frondLength + variation;
        return minLength + random.nextInt(maxLength - minLength + 1);
    }

    private void createPalmFrond(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                 Random random, TreeConfiguration config, BlockPos center,
                                 double dirX, double dirZ, int length, int frondIndex) {

        // Create main frond spine
        for (int i = 1; i <= length; i++) {
            double progress = (double) i / length;

            // Position with natural drooping curve
            int x = (int) Math.round(dirX * i);
            int z = (int) Math.round(dirZ * i);
            int y = calculateVerticalOffset(i, length, progress);

            BlockPos mainPos = center.offset(x, y, z);

            // Place main frond leaf
            if (isValidLeafPosition(level, mainPos)) {
                tryPlaceLeaf(level, blockSetter, random, config, mainPos);
            }

            // Create side leaflets for realistic palm frond appearance
            if (i > 1 && shouldCreateSideLeaflets(i, length, progress, random)) {
                createSideLeaflets(level, blockSetter, random, config, mainPos, dirX, dirZ, progress, i);
            }
        }
    }

    private int calculateVerticalOffset(int distance, int totalLength, double progress) {
        // Keep the first few blocks straight
        if (distance <= 2) {
            return 0;
        }

        // Calculate natural drooping curve
        // Use a gentler curve that doesn't drop too aggressively
        double adjustedProgress = Math.max(0, progress - 0.3); // Start drooping at 30% length
        if (adjustedProgress <= 0) {
            return 0;
        }

        // Quadratic curve for natural look, limited maximum drop
        double dropFactor = adjustedProgress * adjustedProgress;
        int maxDrop = Math.min(4, Math.max(1, totalLength / 4)); // Limit excessive dropping
        return -(int) Math.round(dropFactor * maxDrop);
    }

    private boolean shouldCreateSideLeaflets(int distance, int totalLength, double progress, Random random) {
        // Don't create side leaflets at the very tip or very base
        if (distance >= totalLength - 1 || distance <= 2) {
            return false;
        }

        // Higher chance in the middle sections
        if (progress >= 0.3 && progress <= 0.8) {
            return true;
        }

        // Lower chance at extremes
        return random.nextFloat() < 0.6f;
    }

    private void createSideLeaflets(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                    Random random, TreeConfiguration config, BlockPos mainPos,
                                    double dirX, double dirZ, double progress, int distance) {

        // Calculate perpendicular directions for side leaflets
        double perpX = -dirZ;
        double perpZ = dirX;

        // Calculate leaflet spread based on position and density
        int maxSpread = calculateLeafletSpread(progress, distance);

        for (int side = -maxSpread; side <= maxSpread; side++) {
            if (side == 0) continue; // Skip center (main frond)

            float placementChance = calculateLeafletPlacementChance(Math.abs(side), maxSpread, progress);

            if (random.nextFloat() < placementChance) {
                // Calculate position with slight forward bias for natural look
                double offsetX = perpX * side + dirX * 0.1 * side; // Slight forward angle
                double offsetZ = perpZ * side + dirZ * 0.1 * side;

                BlockPos leafletPos = mainPos.offset(
                        (int) Math.round(offsetX),
                        0,
                        (int) Math.round(offsetZ)
                );

                if (isValidLeafPosition(level, leafletPos)) {
                    tryPlaceLeaf(level, blockSetter, random, config, leafletPos);
                }

                // Occasionally add a second layer for density
                if (density >= 2 && random.nextFloat() < 0.3f) {
                    BlockPos upperLeaflet = leafletPos.above();
                    if (isValidLeafPosition(level, upperLeaflet)) {
                        tryPlaceLeaf(level, blockSetter, random, config, upperLeaflet);
                    }
                }
            }
        }
    }

    private int calculateLeafletSpread(double progress, int distance) {
        // Leaflets are widest in the middle section of the frond
        double spreadFactor;
        if (progress < 0.3) {
            // Narrow at base
            spreadFactor = progress / 0.3 * 0.5;
        } else if (progress > 0.8) {
            // Narrow at tip
            spreadFactor = (1.0 - progress) / 0.2 * 0.5;
        } else {
            // Full width in middle
            spreadFactor = 1.0;
        }

        int baseSpread = 1 + density;
        return Math.max(1, (int) Math.round(baseSpread * spreadFactor));
    }

    private float calculateLeafletPlacementChance(int sideDistance, int maxSpread, double progress) {
        // Base chance increases with density setting
        float baseChance = 0.4f + (density * 0.15f);

        // Reduce chance with distance from main frond
        float distanceReduction = 1.0f - ((float) sideDistance / (maxSpread + 1)) * 0.5f;

        // Maintain good coverage in middle sections
        float progressBonus = 1.0f;
        if (progress >= 0.3 && progress <= 0.7) {
            progressBonus = 1.2f;
        }

        return Math.min(0.9f, baseChance * distanceReduction * progressBonus);
    }

    private boolean isValidLeafPosition(LevelSimulatedReader level, BlockPos pos) {
        // Check if position is suitable for leaves
        return level.isStateAtPosition(pos, state ->
                state.isAir() ||
                        state.canBeReplaced(Fluids.EMPTY) ||
                        state.getMaterial().isReplaceable()
        );
    }

    @Override
    public int foliageHeight(Random random, int height, TreeConfiguration config) {
        // Height based on palm size - more conservative to prevent excessive dropping
        return Math.max(3, frondLength / 3 + 2);
    }

    @Override
    protected boolean shouldSkipLocation(Random random, int localX, int localY, int localZ, int range, boolean large) {
        // Palms don't have round crowns - we override the default logic
        return false;
    }
}