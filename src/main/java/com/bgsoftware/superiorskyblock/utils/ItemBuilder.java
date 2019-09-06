package com.bgsoftware.superiorskyblock.utils;

import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.utils.legacy.Materials;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public final class ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private String textureValue = "";

    public ItemBuilder(ItemStack itemStack){
        this(itemStack.getType(), itemStack.getDurability());
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material type){
        this(type, 0);
    }

    public ItemBuilder(Material type, int damage){
        itemStack = new ItemStack(type, 1, (short) damage);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder asSkullOf(SuperiorPlayer superiorPlayer){
        if(itemStack.getType() == Materials.PLAYER_HEAD.toBukkitType())
            textureValue = superiorPlayer == null ? HeadUtil.getNullPlayerTexture() :  superiorPlayer.getTextureValue();
        return this;
    }

    public ItemBuilder asSkullOf(String textureValue){
        if(itemStack.getType() == Materials.PLAYER_HEAD.toBukkitType())
            this.textureValue = textureValue;
        return this;
    }

    public ItemBuilder withName(String name){
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }

    public ItemBuilder replaceName(String regex, String replace){
        if(itemMeta.hasDisplayName())
            withName(itemMeta.getDisplayName().replace(regex, replace));
        return this;
    }

    public ItemBuilder withLore(String... lore){
        return withLore(Arrays.asList(lore));
    }

    public ItemBuilder withLore(List<String> lore){
        List<String> loreList = new ArrayList<>();

        for(String line : lore){
            loreList.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        itemMeta.setLore(loreList);
        return this;
    }

    public ItemBuilder replaceLore(String regex, String replace){
        if(!itemMeta.hasLore())
            return this;

        List<String> loreList = new ArrayList<>();

        for(String line : itemMeta.getLore()){
            loreList.add(line.replace(regex, replace));
        }

        withLore(loreList);
        return this;
    }

    public ItemBuilder replaceLoreWithLines(String regex, String... lines){
        if(!itemMeta.hasLore())
            return this;

        List<String> loreList = new ArrayList<>();

        for(String line : itemMeta.getLore()){
            if(line.contains(regex)){
                loreList.addAll(Arrays.asList(lines));
            }
            else{
                loreList.add(line);
            }
        }

        withLore(loreList);
        return this;
    }

    public ItemBuilder replaceAll(String regex, String replace){
        replaceName(regex, replace);
        replaceLore(regex, replace);
        return this;
    }

    public ItemBuilder withEnchant(Enchantment enchant, int level){
        itemMeta.addEnchant(enchant, level, true);
        return this;
    }

    public ItemBuilder withFlags(ItemFlag... itemFlags){
        itemMeta.addItemFlags(itemFlags);
        return this;
    }

    public ItemStack build(){
        itemStack.setItemMeta(itemMeta);
        return textureValue.isEmpty() ? itemStack : HeadUtil.getPlayerHead(itemStack, textureValue);
    }

}
