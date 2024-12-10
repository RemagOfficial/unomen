package com.remag.unomen.items;

import com.remag.unomen.Unomen;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Unomen.MODID);

        @SuppressWarnings("unchecked")
        public static final DeferredItem<Item>[] SPLASH_OMINOUS_BOTTLES = new DeferredItem[5];

        static {
            for (int i = 4; i >= 0; i--) {
                int finalI = i;
                SPLASH_OMINOUS_BOTTLES[i] = ITEMS.register("splash_ominous_bottle" + (i + 1),
                        () -> new SplashPotionItem(new Item.Properties().stacksTo(1).component(DataComponents.POTION_CONTENTS,
                                new PotionContents(Holder.direct(new Potion(new MobEffectInstance(MobEffects.BAD_OMEN, 120000, finalI))))).component(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, finalI)));
            }
        }
        public static void register(IEventBus eventBus) {
            ITEMS.register(eventBus);
        }
    }

