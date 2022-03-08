package com.jaquadro.minecraft.storagedrawers.item;

import com.jaquadro.minecraft.storagedrawers.config.CommonConfig;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ItemUpgradeStorage extends ItemUpgrade
{
    private static int storageGroupId;
    static {
        storageGroupId = ItemUpgrade.getNextGroupId();
    }

    public final EnumUpgradeStorage level;

    public ItemUpgradeStorage (EnumUpgradeStorage level, Item.Properties properties) {
        this(level, properties, storageGroupId);
    }

    protected ItemUpgradeStorage (EnumUpgradeStorage level, Item.Properties properties, int groupId) {
        super(properties, groupId);

        setAllowMultiple(true);
        this.level = level;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Component getDescription() {
        int mult = CommonConfig.UPGRADES.getLevelMult(level.getLevel());
        return new TranslatableComponent("item.storagedrawers.storage_upgrade.desc", mult);
    }
}
