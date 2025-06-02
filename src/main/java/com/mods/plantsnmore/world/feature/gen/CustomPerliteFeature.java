package com.mods.plantsnmore.world.feature.gen;

import com.mods.plantsnmore.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

public class CustomPerliteFeature extends Feature<NoneFeatureConfiguration> {

    public CustomPerliteFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        Random random = context.random();

        // Suche nach Lava in der Nähe (innerhalb von 8 Blöcken)
        boolean lavaFound = false;
        for (int x = -8; x <= 8; x++) {
            for (int z = -8; z <= 8; z++) {
                for (int y = -3; y <= 3; y++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    if (level.getBlockState(checkPos).getBlock() == Blocks.LAVA) {
                        lavaFound = true;
                        break;
                    }
                }
                if (lavaFound) break;
            }
            if (lavaFound) break;
        }

        if (!lavaFound) {
            return false;
        }

        // Ersetze Sand mit Perlite in einem kleinen Bereich
        int placed = 0;
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                for (int y = -1; y <= 1; y++) {
                    BlockPos placePos = pos.offset(x, y, z);

                    if (level.getBlockState(placePos).getBlock() == Blocks.SAND &&
                            random.nextFloat() < 0.7f) { // 70% Chance

                        level.setBlock(placePos, ModBlocks.PERLITE.get().defaultBlockState(), 2);
                        placed++;
                    }
                }
            }
        }

        return placed > 0;
    }
}