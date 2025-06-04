package com.mods.plantsnmore.block;

import com.mods.plantsnmore.PlantsnMore;
import com.mods.plantsnmore.block.custom.CoconutTreeBlock;
import com.mods.plantsnmore.block.custom.ModFlammableRotatedPillarBlock;
import com.mods.plantsnmore.block.custom.PlantStationBlock;
import com.mods.plantsnmore.item.ModCreativeModeTab;
import com.mods.plantsnmore.item.ModItems;
import com.mods.plantsnmore.world.feature.tree.CocoPalmTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PlantsnMore.MOD_ID);


    public static final RegistryObject<Block> DECO_POT = registerBlock("deco_pot",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.FLOWER_POT)), ModCreativeModeTab.POTS_TAB);

    public static final RegistryObject<Block> AEROID_MIX = registerBlock("aeroid_mix",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)), ModCreativeModeTab.MISC_TAB);
    public static final RegistryObject<Block> PERLITE = registerBlock("perlite",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SAND)), ModCreativeModeTab.MISC_TAB);
    public static final RegistryObject<Block> BIOCHAR = registerBlock("biochar",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)), ModCreativeModeTab.MISC_TAB);
    public static final RegistryObject<Block> COCO_FIBER = registerBlock("coco_fiber",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)), ModCreativeModeTab.MISC_TAB);
    public static final RegistryObject<Block> WORM_CASTING = registerBlock("worm_casting",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)), ModCreativeModeTab.MISC_TAB);

    public static final RegistryObject<Block> PLANT_STATION_OAK = registerBlock("plant_station_oak",
            () -> new PlantStationBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).noOcclusion()), ModCreativeModeTab.MISC_TAB);
    public static final RegistryObject<Block> PLANT_STATION_DARK_OAK = registerBlock("plant_station_dark_oak",
            () -> new PlantStationBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).noOcclusion()), ModCreativeModeTab.MISC_TAB);
    public static final RegistryObject<Block> PLANT_STATION_BIRCH = registerBlock("plant_station_birch",
            () -> new PlantStationBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).noOcclusion()), ModCreativeModeTab.MISC_TAB);
    public static final RegistryObject<Block> PLANT_STATION_ACACIA = registerBlock("plant_station_acacia",
            () -> new PlantStationBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).noOcclusion()), ModCreativeModeTab.MISC_TAB);
    public static final RegistryObject<Block> PLANT_STATION_CRIMSON = registerBlock("plant_station_crimson",
            () -> new PlantStationBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).noOcclusion()), ModCreativeModeTab.MISC_TAB);
    public static final RegistryObject<Block> PLANT_STATION_WARPED = registerBlock("plant_station_warped",
            () -> new PlantStationBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).noOcclusion()), ModCreativeModeTab.MISC_TAB);
    public static final RegistryObject<Block> PLANT_STATION_JUNGLE = registerBlock("plant_station_jungle",
            () -> new PlantStationBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).noOcclusion()), ModCreativeModeTab.MISC_TAB);
    public static final RegistryObject<Block> PLANT_STATION_SPRUCE = registerBlock("plant_station_spruce",
            () -> new PlantStationBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).noOcclusion()), ModCreativeModeTab.MISC_TAB);

    public static final RegistryObject<Block> COCONUT_TREE = registerBlock("coconut_tree",
            () -> new CoconutTreeBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).noOcclusion()), ModCreativeModeTab.MISC_TAB);


    public static final RegistryObject<Block> COCO_PALM_LOG = registerBlock("coco_palm_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)), ModCreativeModeTab.MISC_TAB);
    public static final RegistryObject<Block> COCO_PALM_WOOD = registerBlock("coco_palm_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)), ModCreativeModeTab.MISC_TAB);
    public static final RegistryObject<Block> STRIPPED_COCO_PALM_LOG = registerBlock("stripped_coco_palm_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)), ModCreativeModeTab.MISC_TAB);
    public static final RegistryObject<Block> STRIPPED_COCO_PALM_WOOD = registerBlock("stripped_coco_palm_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)), ModCreativeModeTab.MISC_TAB);



    public static final RegistryObject<Block> MONST_DEL_AGE0 = registerBlock("monst_del_age0",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)), ModCreativeModeTab.PLANTS_TAB);
    public static final RegistryObject<Block> MONST_DEL_AGE1 = registerBlock("monst_del_age1",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)), ModCreativeModeTab.PLANTS_TAB);
    public static final RegistryObject<Block> MONST_DEL_AGE2 = registerBlock("monst_del_age2",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)), ModCreativeModeTab.PLANTS_TAB);



    public static final RegistryObject<Block> COCO_PALM_PLANKS = registerBlock("coco_palm_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            }, ModCreativeModeTab.MISC_TAB);


    public static final RegistryObject<Block> COCO_PALM_LEAVES = registerBlock("coco_palm_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 30;
                }
            }, ModCreativeModeTab.MISC_TAB);


    public static final RegistryObject<Block> COCO_PALM_SAPLING = registerBlock("coco_palm_sapling",
            () -> new SaplingBlock(new CocoPalmTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)), ModCreativeModeTab.PLANTS_TAB);


    private static <T extends Block> RegistryObject<T> registerBlockWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }


    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)));
    }




    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}