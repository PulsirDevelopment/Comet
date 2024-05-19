package net.pulsir.comet.command;

import net.pulsir.comet.utils.inventory.impl.HistoryInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HistoryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) return false;
        HistoryInventory historyInventory = new HistoryInventory();
        player.openInventory(historyInventory.getInventory(player));

        return true;
    }
}
