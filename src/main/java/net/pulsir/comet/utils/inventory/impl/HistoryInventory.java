package net.pulsir.comet.utils.inventory.impl;

import net.kyori.adventure.text.Component;
import net.pulsir.comet.Comet;
import net.pulsir.comet.utils.inventory.IInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class HistoryInventory implements IInventory {

    @Override
    public Component title() {
        return Comet.getInstance().getMessage().getMessage(Comet.getInstance().getConfiguration()
                .getConfiguration().getString("history-inventory.title"));
    }

    @Override
    public int size() {
        return Comet.getInstance().getConfiguration().getConfiguration()
                .getInt("history-inventory.size");
    }

    @Override
    public boolean fill() {
        return Comet.getInstance().getConfiguration().getConfiguration()
                .getBoolean("history-inventory.fill");
    }

    @Override
    public ItemStack filledItem() {
        return new ItemStack(Material.valueOf(Comet.getInstance().getConfiguration()
                .getConfiguration().getString("history-inventory.filled-item")));
    }

    @Override
    public Map<ItemStack, Integer> items() {
        return null;
    }

    @Override
    public Inventory getInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, size(), title());
        Map<ItemStack, Integer> items = items();

        for (ItemStack itemStack : items.keySet()) {
            inventory.setItem(items.get(itemStack), itemStack);
        }
        if (fill()) {
            for (int i = 0; i < inventory.getSize(); i++) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, filledItem());
                }
            }
        }

        return inventory;
    }
}
