package com.mods.plantsnmore.world.feature;

import com.mods.plantsnmore.PlantsnMore;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ModPlacedFeatures {

    // Use DeferredRegister instead of PlacementUtils.register()
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, PlantsnMore.MOD_ID);

    // Worm Casting - oberflächennah, 1-2 Blöcke unter Grass Blocks
    public static final RegistryObject<PlacedFeature> WORM_CASTING_PLACED = PLACED_FEATURES.register("worm_casting_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.WORM_CASTING.getHolder().get(),
                    ModOrePlacement.wormCastingPlacement(15, // Viele Versuche
                            HeightRangePlacement.triangle(VerticalAnchor.absolute(50), VerticalAnchor.absolute(90))))); // Fokus auf Oberflächennähe

    // Perlite - in Wüsten, verstärkt bei Lava
    public static final RegistryObject<PlacedFeature> PERLITE_PLACED = PLACED_FEATURES.register("perlite_placed",
            () -> new PlacedFeature(ModConfiguredFeatures.PERLITE.getHolder().get(),
                    ModOrePlacement.desertOrePlacement(12, // Viele Versuche in Wüsten
                            HeightRangePlacement.uniform(VerticalAnchor.absolute(40), VerticalAnchor.absolute(100)))));

    // Register method for the event bus
    public static void register(IEventBus eventBus) {
        PLACED_FEATURES.register(eventBus);
    }
}