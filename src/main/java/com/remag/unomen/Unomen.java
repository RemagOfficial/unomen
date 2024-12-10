package com.remag.unomen;

import com.remag.unomen.config.Config;
import com.remag.unomen.events.PillagerDeathHandler;
import com.remag.unomen.items.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.item.*;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.awt.*;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Unomen.MODID)
public class Unomen
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "unomen";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Unomen(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        new PillagerDeathHandler();

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Unomen) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        ModItems.register(modEventBus);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            if (Config.ENABLE_SPLASH_POTIONS.get()) {
                // create a stack of the Ominous Bottle
                ItemStack ominousBottleStack = new ItemStack(Items.OMINOUS_BOTTLE);
                // add components to the ominous bottle stack
                ominousBottleStack.set(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, 4);

                // add all items from ModItems to the INGREDIENTS tab using a loop
                for (Holder<Item> item : ModItems.ITEMS.getEntries()) {
                    event.insertAfter(ominousBottleStack, new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
                }
            }
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {}
    }
}
