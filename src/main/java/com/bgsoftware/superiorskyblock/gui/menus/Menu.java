package com.bgsoftware.superiorskyblock.gui.menus;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.gui.buttons.Button;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class Menu {

    protected Player player;

    protected Inventory inventory;

    protected Map<Integer, Button> buttons;

    protected String title;
    protected int rows;

    public Menu(Player player, String title, int rows) {
        this.player = player;
        buttons = new HashMap<>();

        this.title = title;
        this.rows = rows;
    }

    protected void create(String title, int rows) {
        inventory = Bukkit.createInventory(null, rows * 9, title);
        SuperiorSkyblockPlugin.getPlugin().getMenuHandler().getMenus().put(inventory, this);
    }

    public void open() {
        update();
        player.openInventory(inventory);
    }

    protected void update() {
        inventory.clear();
        buttons.forEach((key, button) -> inventory.setItem(key, button.getItem()));
    }

    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getClickedInventory() == null)
            return;

        if (!event.getClickedInventory().equals(inventory))
            return;

        Button button = buttons.get(event.getSlot());
        if (button == null)
            return;

        if (button.getAction() != null)
            button.getAction().accept((Player) event.getWhoClicked(), event.getClick());

        button.sendCommands(player, player.getName());

        update();
    }

    protected int coordsToSlot(int x, int y) {
        return y * 9 + x;
    }

    @SuppressWarnings("all")
    protected int coordsToSlot(String position) {
        String[] split = position.replace(" ", "").split(",");
        return coordsToSlot(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
    }

    public void setButton(int slot, Button button) {
        buttons.put(slot, button);
    }

    public void setButton(int x, int y, Button button) {
        setButton(coordsToSlot(x, y), button);
    }

    public void removeButton(int slot) {
        buttons.remove(slot);
    }

    public void removeButton(int x, int y) {
        removeButton(coordsToSlot(x, y));
    }

}