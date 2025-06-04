package com.mods.plantsnmore;

import com.mods.plantsnmore.block.ModBlocks;
import com.mods.plantsnmore.block.custom.entity.ModBlockEntities;
import com.mods.plantsnmore.item.ModItems;
import com.mods.plantsnmore.recipe.ModRecipes;
import com.mods.plantsnmore.screen.ModMenuTypes;
import com.mods.plantsnmore.screen.PlantStationScreen;
import com.mods.plantsnmore.world.feature.ModConfiguredFeatures;
import com.mods.plantsnmore.world.feature.tree.ModTreeDecorators;
import com.mods.plantsnmore.world.feature.tree.ModTrunkPlacers;
import com.mods.plantsnmore.world.feature.tree.ModFoliagePlacers;
import com.mods.plantsnmore.world.feature.ModPlacedFeatures;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PlantsnMore.MOD_ID)
public class PlantsnMore {
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "plantsnmore";

    public PlantsnMore() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::clientSetup);

        // Register configured features and placed features
        ModConfiguredFeatures.register(eventBus);
        ModPlacedFeatures.register(eventBus); // ADD THIS LINE

        ModItems.register(eventBus);
        ModBlocks.register(eventBus);

        ModBlockEntities.register(eventBus);
        ModMenuTypes.register(eventBus);

        ModRecipes.register(eventBus);

        ModTrunkPlacers.register(eventBus);
        ModFoliagePlacers.register(eventBus);
        ModTreeDecorators.register(eventBus);


        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.MONST_DEL_AGE0.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.MONST_DEL_AGE1.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.MONST_DEL_AGE2.get(), RenderType.translucent());

        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PLANT_STATION_OAK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PLANT_STATION_DARK_OAK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PLANT_STATION_JUNGLE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PLANT_STATION_SPRUCE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PLANT_STATION_ACACIA.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PLANT_STATION_CRIMSON.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PLANT_STATION_WARPED.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PLANT_STATION_BIRCH.get(), RenderType.translucent());

        ItemBlockRenderTypes.setRenderLayer(ModBlocks.COCONUT_TREE.get(), RenderType.translucent());

        ItemBlockRenderTypes.setRenderLayer(ModBlocks.COCO_PALM_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.COCO_PALM_LEAVES.get(), RenderType.cutout());

        MenuScreens.register(ModMenuTypes.PLANT_STATION_MENU.get(), PlantStationScreen::new);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
        });
    }
}