package com.mods.plantsnmore.world.feature;

import com.mods.plantsnmore.PlantsnMore;
import com.mods.plantsnmore.world.feature.gen.ModOreGeneration;
import com.mods.plantsnmore.world.feature.gen.ModTreeGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PlantsnMore.MOD_ID)
public class ModWorldEvents {
    @SubscribeEvent
    public static void biomeLoadingEvent(final BiomeLoadingEvent event) {
        ModOreGeneration.generateOres(event);
        ModTreeGeneration.generatePalmTrees(event); // Hinzugefügt
    }
}