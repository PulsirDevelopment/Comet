package net.pulsir.comet.command;

import net.pulsir.comet.Comet;
import net.pulsir.comet.utils.inventory.impl.HistoryInventory;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HistoryCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) return false;
        if (!(sender.hasPermission("comet.command.history"))) {
            sender.sendMessage(Objects.requireNonNull(Comet.getInstance().getLanguage().getConfiguration().getString("NO-PERMISSIONS")));
            return false;
        }

        if (args.length == 0) {
            getUsage(sender);
            return false;
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

            HistoryInventory historyInventory = new HistoryInventory();
            player.openInventory(historyInventory.getInventory(player));
        }

        return true;
    }

    private void getUsage(CommandSender sender){
        for (final String lines : Comet.getInstance().getLanguage().getConfiguration().getStringList("USAGE")) {
            sender.sendMessage(Comet.getInstance().getMessage().getMessage(lines));
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> players = new ArrayList<>();
            for (final Player player : Bukkit.getOnlinePlayers()) players.add(player.getName());
            return players;
        }
        return new ArrayList<>();
    }
}
