package com.mods.plantsnmore.world.feature.tree;

import com.mods.plantsnmore.PlantsnMore;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Constructor;

public class ModTrunkPlacers {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS =
            DeferredRegister.create(Registry.TRUNK_PLACER_TYPE_REGISTRY, PlantsnMore.MOD_ID);

    public static final RegistryObject<TrunkPlacerType<CurvedPalmTrunkPlacer>> CURVED_PALM_TRUNK_PLACER =
            TRUNK_PLACERS.register("curved_palm_trunk_placer",
                    () -> createTrunkPlacerType(CurvedPalmTrunkPlacer.CODEC));

    @SuppressWarnings("unchecked")
    private static <P extends net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer> TrunkPlacerType<P> createTrunkPlacerType(Codec<P> codec) {
        try {
            Constructor<TrunkPlacerType> constructor = TrunkPlacerType.class.getDeclaredConstructor(Codec.class);
            constructor.setAccessible(true);
            return (TrunkPlacerType<P>) constructor.newInstance(codec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create TrunkPlacerType", e);
        }
    }

    public static void register(IEventBus eventBus) {
        TRUNK_PLACERS.register(eventBus);
    }
}