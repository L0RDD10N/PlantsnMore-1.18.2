package com.mods.plantsnmore.world.feature.tree;

import com.mods.plantsnmore.PlantsnMore;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModTreeDecorators {
    public static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS =
            DeferredRegister.create(ForgeRegistries.TREE_DECORATOR_TYPES, PlantsnMore.MOD_ID);

    public static final RegistryObject<TreeDecoratorType<CoconutTreeDecorator>> COCONUT_TREE_DECORATOR =
            TREE_DECORATORS.register("coconut_tree_decorator",
                    () -> new TreeDecoratorType<>(CoconutTreeDecorator.CODEC));

    public static void register(IEventBus eventBus) {
        TREE_DECORATORS.register(eventBus);
    }
}