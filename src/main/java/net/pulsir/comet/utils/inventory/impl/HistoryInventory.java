package net.pulsir.comet.utils.inventory.impl;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.pulsir.comet.Comet;
import net.pulsir.comet.utils.inventory.IInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

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
        Map<ItemStack, Integer> itemStacks = new HashMap<>();
        if (Comet.getInstance().getConfiguration().getConfiguration().getConfigurationSection("history-inventory.items") == null) return itemStacks;
        for (final String items : Objects.requireNonNull(Comet.getInstance().getConfiguration().getConfiguration().getConfigurationSection("history-inventory.items")).getKeys(false)) {
            ItemStack itemStack = new ItemStack(Material.valueOf(Comet.getInstance().getConfiguration().getConfiguration().getString("history-inventory.items." + items + ".item")));
            Comet.getInstance().getConfiguration().getConfiguration().getStringList("history-inventory.items." + items + ".enchantment").forEach(enchant ->
                    itemStack.addUnsafeEnchantment(Objects.requireNonNull(Enchantment.getByName(enchant.split(":")[0])), Integer.parseInt(enchant.split(":")[1])));
            ItemMeta meta = itemStack.getItemMeta();
            meta.getPersistentDataContainer().set(Comet.getInstance().getHistoryButton(), PersistentDataType.STRING,
                    Objects.requireNonNull(Comet.getInstance().getConfiguration().getConfiguration().getString("history-inventory.items." + items + ".ban-type")));
            meta.displayName(Comet.getInstance().getMessage().getMessage(Comet.getInstance().getConfiguration().getConfiguration().getString("history-inventory.items." + items + ".name")).decoration(TextDecoration.ITALIC, false));
            List<Component> lore = new ArrayList<>();
            Comet.getInstance().getConfiguration().getConfiguration().getStringList("history-inventory.items." + items + ".lore").forEach(line
                    -> lore.add(Comet.getInstance().getMessage().getMessage(line)));
            meta.lore(lore);
            itemStack.setItemMeta(meta);

            itemStacks.put(itemStack, Comet.getInstance().getConfiguration().getConfiguration().getInt("history-inventory.items." + items + ".slot"));
        }

        return itemStacks;
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
