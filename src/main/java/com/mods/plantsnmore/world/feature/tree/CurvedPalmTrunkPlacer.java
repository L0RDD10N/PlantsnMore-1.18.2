package com.mods.plantsnmore.world.feature.tree;

import com.google.common.collect.ImmutableList;
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
        return trunkPlacerParts(instance).and(
                instance.group(
                        Codec.floatRange(0.0f, 2.0f).fieldOf("curve_strength").forGetter(placer -> placer.curveStrength),
                        Codec.intRange(-1, 3).fieldOf("curve_direction").forGetter(placer -> placer.curveDirection), // -1 for random
                        Codec.floatRange(0.0f, 1.0f).fieldOf("randomness").forGetter(placer -> placer.randomness)
                )
        ).apply(instance, CurvedPalmTrunkPlacer::new);
    });

    private final float curveStrength; // 0.0 = gerade, 2.0 = stark gekrümmt
    private final int curveDirection; // -1 = random, 0-3 für Hauptrichtungen (N,S,E,W)
    private final float randomness; // Zufällige Variation

    public CurvedPalmTrunkPlacer(int baseHeight, int heightRandA, int heightRandB,
                                 float curveStrength, int curveDirection, float randomness) {
        super(baseHeight, heightRandA, heightRandB);
        this.curveStrength = curveStrength;
        this.curveDirection = curveDirection;
        this.randomness = randomness;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModTrunkPlacers.CURVED_PALM_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level,
                                                            BiConsumer<BlockPos, BlockState> blockSetter,
                                                            Random random, int height, BlockPos startPos,
                                                            TreeConfiguration config) {

        Direction curveDir = getCurveDirection(random);
        setDirtAt(level, blockSetter, random, startPos.below(), config);

        // Aktuelle Position für den Stamm
        double currentX = startPos.getX();
        double currentZ = startPos.getZ();

        // Curve-Parameter mit etwas mehr Variation
        double totalCurve = curveStrength + (random.nextFloat() - 0.5f) * randomness;

        BlockPos lastPos = startPos;

        for (int i = 0; i < height; i++) {
            double progress = (double) i / height;

            // Berechne Krümmung (verschiedene Kurven-Arten je nach Stärke)
            double curveOffset = calculateCurveOffset(progress, totalCurve, height);

            // Anwenden der Krümmung in die gewählte Richtung
            double offsetX = 0;
            double offsetZ = 0;

            switch (curveDir) {
                case NORTH: offsetZ = -curveOffset; break;
                case SOUTH: offsetZ = curveOffset; break;
                case EAST: offsetX = curveOffset; break;
                case WEST: offsetX = -curveOffset; break;
                default: break; // Fallback für unerwartete Richtungen
            }

            // Kleine zufällige Variation für natürlicheren Look
            if (randomness > 0) {
                offsetX += (random.nextFloat() - 0.5f) * randomness * 0.5;
                offsetZ += (random.nextFloat() - 0.5f) * randomness * 0.5;
            }

            BlockPos trunkPos = new BlockPos(
                    startPos.getX() + (int) Math.round(offsetX),
                    startPos.getY() + i,
                    startPos.getZ() + (int) Math.round(offsetZ)
            );

            // Validiere Position (verhindere zu extreme Abweichungen)
            if (Math.abs(trunkPos.getX() - startPos.getX()) > height ||
                    Math.abs(trunkPos.getZ() - startPos.getZ()) > height) {
                // Fallback zur letzten gültigen Position
                trunkPos = new BlockPos(lastPos.getX(), startPos.getY() + i, lastPos.getZ());
            }

            // Platziere Stamm-Block
            placeLog(level, blockSetter, random, trunkPos, config);

            // Fülle Lücken zwischen den Positionen wenn nötig
            if (i > 0) {
                fillGapsBetweenPositions(level, blockSetter, random, lastPos, trunkPos, config);
            }

            lastPos = trunkPos;
        }

        // Rückgabe der Krone-Position - etwas höher für bessere Blätter-Platzierung
        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(lastPos.above(), 0, false));
    }

    private Direction getCurveDirection(Random random) {
        Direction[] directions = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

        if (curveDirection >= 0 && curveDirection < directions.length) {
            return directions[curveDirection];
        }

        // Random direction (-1 oder ungültiger Wert)
        return directions[random.nextInt(directions.length)];
    }

    private double calculateCurveOffset(double progress, double totalCurve, int height) {
        // Verschiedene Kurven-Arten basierend auf curve_strength
        if (curveStrength < 0.5f) {
            // Leichte Biegung - linear
            return progress * totalCurve;
        } else if (curveStrength < 1.0f) {
            // Mittlere Biegung - quadratisch (beschleunigt am Ende)
            return Math.pow(progress, 1.5) * totalCurve;
        } else if (curveStrength < 1.5f) {
            // Starke Biegung - S-Kurve
            return calculateSCurve(progress) * totalCurve;
        } else {
            // Sehr starke Biegung - exponentiell mit Wellenbewegung
            return (Math.pow(progress, 0.7) - 0.3 * Math.sin(progress * Math.PI)) * totalCurve;
        }
    }

    private double calculateSCurve(double progress) {
        // Smooth S-curve using sigmoid function
        double x = (progress - 0.5) * 6; // Scale to make it more S-shaped
        return 1.0 / (1.0 + Math.exp(-x)) - 0.5; // Center around 0
    }

    private void fillGapsBetweenPositions(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                                          Random random, BlockPos from, BlockPos to, TreeConfiguration config) {
        // Berechne Distanz zwischen Positionen
        int deltaX = to.getX() - from.getX();
        int deltaY = to.getY() - from.getY();
        int deltaZ = to.getZ() - from.getZ();

        // Fülle Lücken wenn die horizontale Distanz zu groß ist
        int maxDistance = Math.max(Math.abs(deltaX), Math.abs(deltaZ));
        if (maxDistance > 1) {
            // Interpoliere zwischen den Positionen
            for (int step = 1; step < maxDistance; step++) {
                double progress = (double) step / maxDistance;

                int interpX = from.getX() + (int) Math.round(deltaX * progress);
                int interpY = from.getY() + (int) Math.round(deltaY * progress);
                int interpZ = from.getZ() + (int) Math.round(deltaZ * progress);

                BlockPos interpPos = new BlockPos(interpX, interpY, interpZ);
                placeLog(level, blockSetter, random, interpPos, config);
            }
        }
    }

    // Getter methods for codec access
    public float getCurveStrength() {
        return curveStrength;
    }

    public int getCurveDirection() {
        return curveDirection;
    }

    public float getRandomness() {
        return randomness;
    }
}