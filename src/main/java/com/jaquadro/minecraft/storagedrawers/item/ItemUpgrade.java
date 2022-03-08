package com.jaquadro.minecraft.storagedrawers.item;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemUpgrade extends Item
{
    private static int nextGroupId = 0;

    private boolean allowMultiple;
    private int groupId;

    public ItemUpgrade (Item.Properties properties) {
        this(properties, getNextGroupId());
    }

    protected ItemUpgrade (Item.Properties properties, int groupId) {
        super(properties);
        this.groupId = groupId;
    }

    protected static int getNextGroupId () {
        int groupId = nextGroupId;
        nextGroupId += 1;
        return groupId;
    }

    public int getUpgradeGroup() {
        return groupId;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText (@Nonnull ItemStack itemStack, @Nullable Level world, List<Component> list, TooltipFlag advanced) {
        list.add(new TextComponent("").append(getDescription()).withStyle(ChatFormatting.GRAY));
    }

    @Environment(EnvType.CLIENT)
    public Component getDescription() {
        return new TranslatableComponent(this.getDescriptionId() + ".desc");
    }

    public void setAllowMultiple (boolean allow) {
        allowMultiple = allow;
    }

    public boolean getAllowMultiple () {
        return allowMultiple;
    }
}
