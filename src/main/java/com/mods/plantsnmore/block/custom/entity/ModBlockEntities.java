package com.mods.plantsnmore.block.custom.entity;

import com.mods.plantsnmore.PlantsnMore;
import com.mods.plantsnmore.block.ModBlocks;
import com.mods.plantsnmore.block.custom.entity.custom.CoconutTreeBlockEntity;
import com.mods.plantsnmore.block.custom.entity.custom.PlantStationBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public  static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, PlantsnMore.MOD_ID);


    public static final RegistryObject<BlockEntityType<PlantStationBlockEntity>> PLANT_STATION_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("plant_station_block_entity", () ->
                    BlockEntityType.Builder.of(PlantStationBlockEntity::new,
                            ModBlocks.PLANT_STATION_OAK.get(),
                            ModBlocks.PLANT_STATION_DARK_OAK.get(),
                            ModBlocks.PLANT_STATION_BIRCH.get(),
                            ModBlocks.PLANT_STATION_JUNGLE.get(),
                            ModBlocks.PLANT_STATION_SPRUCE.get(),
                            ModBlocks.PLANT_STATION_ACACIA.get(),
                            ModBlocks.PLANT_STATION_CRIMSON.get(),
                            ModBlocks.PLANT_STATION_WARPED.get()
                    ).build(null));


    public static final RegistryObject<BlockEntityType<CoconutTreeBlockEntity>> COCONUTS =
            BLOCK_ENTITIES.register("coconuts", () ->
                    BlockEntityType.Builder.of(CoconutTreeBlockEntity::new,
                            ModBlocks.COCONUT_TREE.get()
                    ).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
