package com.remag.unomen.datagen;

import com.remag.unomen.Unomen;
import com.remag.unomen.items.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Unomen.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModItems.ITEMS.getEntries().forEach(item -> {
            String itemName = item.getId().getPath();
            singleTexture(itemName,
                    mcLoc("item/generated"),
                    "layer0",
                    modLoc("item/splash_ominous_bottle"));
        });
    }
}
