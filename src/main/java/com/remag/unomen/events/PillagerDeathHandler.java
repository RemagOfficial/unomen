package com.remag.unomen.events;

import com.remag.unomen.config.Config;
import com.remag.unomen.items.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

public class PillagerDeathHandler {

    public PillagerDeathHandler() {
        NeoForge.EVENT_BUS.register(this);
    }


    /**
     * When a Pillager Captain dies, apply Bad Omen to the player if they had it.
     * If the player already has Bad Omen, increase the level.
     * The maximum level of Bad Omen is 5.
     * The duration of Bad Omen is 36,000 ticks, which is 1,800 seconds or 30 minutes.
     */
    @SubscribeEvent
    public void onPillagerDeath(LivingDeathEvent event) {
        if (Config.ENABLE_SPLASH_POTIONS.get()) return;
        if (event.getEntity() instanceof Pillager pillager && pillager.isCaptain() && event.getSource().getEntity() instanceof Player player) {
            int badOmenLevel = player.hasEffect(MobEffects.BAD_OMEN) ?
                    // If the player already has Bad Omen, increase the level
                    (Objects.requireNonNull(player.getEffect(MobEffects.BAD_OMEN)).getAmplifier() < 4 ?
                            Objects.requireNonNull(player.getEffect(MobEffects.BAD_OMEN)).getAmplifier() + 1 : 4) : 0;
            // Apply Bad Omen to the player
            player.addEffect(new MobEffectInstance(MobEffects.BAD_OMEN, 36000, badOmenLevel));
        }
    }

    /**
     * When a Pillager dies, remove any Ominous Bottle drops.
     * This is done to prevent the player from getting the Ominous Bottle.
     */
    @SubscribeEvent
    public void onPillagerDrops(LivingDropsEvent event) {
        if (Config.ENABLE_SPLASH_POTIONS.get()) {
            System.out.println("splash potions enabled");
            if (event.getEntity() instanceof Pillager && event.getSource().getEntity() != null) {
                System.out.println("pillager killed");
                int randomNumber = (int) (Math.random() * 4) + 1;
                String drops = event.getDrops().toString();
                if (drops.contains("Ominous Bottle")) {
                    System.out.println("ominous bottle in drops" + drops);
                    event.getDrops().add(new ItemEntity(event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), new ItemStack(ModItems.SPLASH_OMINOUS_BOTTLES[randomNumber].asItem())));
                    System.out.println("splash ominous bottle added" + event.getDrops());
                }
            }
        }
        if (event.getEntity() instanceof Pillager && event.getSource().getEntity() != null) {
            // Remove any Ominous Bottle drops
            event.getDrops().removeIf(drop -> drop.getItem().is(Items.OMINOUS_BOTTLE));
        }
    }
}

