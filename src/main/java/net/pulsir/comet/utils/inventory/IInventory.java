package net.pulsir.comet.utils.inventory;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface IInventory {

    Component title();
    int size();
    boolean fill();
    ItemStack filledItem();
    Map<ItemStack, Integer> items();
    Inventory getInventory(Player player);
}