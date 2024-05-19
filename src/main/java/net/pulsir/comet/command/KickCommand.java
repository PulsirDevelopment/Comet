package net.pulsir.comet.command;

import net.pulsir.comet.Comet;
import net.pulsir.comet.punishment.Punishment;
import net.pulsir.comet.punishment.type.PunishmentType;
import net.pulsir.comet.utils.calendar.ICalendar;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class KickCommand implements CommandExecutor, TabCompleter {

    private boolean isSilent = false;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender.hasPermission("comet.command.kick"))) {
            sender.sendMessage(Objects.requireNonNull(Comet.getInstance().getLanguage().getConfiguration().getString("NO-PERMISSIONS")));
            return false;
        }

        if (args.length == 0) {
            getUsage(sender);
            return false;
        } else {
            Player player = Bukkit.getPlayer(args[0]);
            if (args.length == 1) {
                getUsage(sender);
            } else {
                String reason = "";

                for (int i = 1; i < args.length; i++) {
                    if (args[i].equalsIgnoreCase("-s")) {
                        isSilent = true;
                    } else {
                        if (reason.isEmpty()) {
                            reason = args[i];
                        } else {
                            reason = reason + " " + args[i];
                        }
                    }
                }

                if (player == null || !player.isOnline()) {
                    sender.sendMessage(Comet.getInstance().getMessage().getMessage(Objects.requireNonNull(Comet.getInstance()
                                    .getLanguage().getConfiguration().getString("PLAYER-OFFLINE"))
                            .replace("{player}", args[1])));
                    return false;
                }

                String rawMessage = "";

                for (final String messageFormat : Comet.getInstance().getLanguage().getConfiguration().getStringList("FORMAT.KICK")) {
                    if (rawMessage.isEmpty()) {
                        rawMessage = messageFormat;
                    } else {
                        rawMessage = rawMessage + "\n" + messageFormat;
                    }
                }

                Date date = new Date();
                ICalendar calendar = new ICalendar(date);
                calendar.add(Objects.requireNonNull(Comet.getInstance().getConfiguration().getConfiguration().getString("default-warn-expiry.type")),
                        Comet.getInstance().getConfiguration().getConfiguration().getInt("default-warn-expiry.amount"));

                if (Comet.getInstance().getPunishmentManger().getPunishments().get(player.getUniqueId()) == null) {
                    List<Punishment> punishments = new ArrayList<>(List.of(new Punishment(player.getUniqueId(), PunishmentType.KICK, date,
                            calendar.date(),
                            reason,
                            sender.getName())));
                    Comet.getInstance().getPunishmentManger().getPunishments().put(player.getUniqueId(), punishments);
                } else {
                    Comet.getInstance().getPunishmentManger().getPunishments().get(player.getUniqueId()).add(
                            new Punishment(player.getUniqueId(), PunishmentType.KICK, date,
                                    calendar.date(),
                                    reason,
                                    sender.getName())
                    );
                }

                player.kick(Comet.getInstance().getMessage().getMessage(rawMessage
                        .replace("{staff}", sender.getName())
                        .replace("{reason}", reason)));

                if (!isSilent) {
                    Bukkit.broadcast(Comet.getInstance().getMessage().getMessage(Objects.requireNonNull(Comet.getInstance().getLanguage()
                                    .getConfiguration().getString("SUCCESS.KICK-PUBLIC")).replace("{player}", player.getName())
                            .replace("{staff}", sender.getName())
                            .replace("{reason}", reason)));
                } else {
                    for (final Player staff : Bukkit.getOnlinePlayers()) {
                        if (staff.hasPermission("comet.staff")) {
                            staff.sendMessage(Comet.getInstance().getMessage().getMessage(Objects.requireNonNull(Comet.getInstance().getLanguage()
                                            .getConfiguration().getString("SUCCESS.KICK-SILENT")).replace("{player}", player.getName())
                                    .replace("{staff}", sender.getName())
                                    .replace("{reason}", reason)));
                        }
                    }
                }
            }
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
        } else if (args.length == 2) {
            return Objects.requireNonNull(Comet.getInstance().getConfiguration().getConfiguration().getStringList("default-reason.kick"));
        }

        return new ArrayList<>();
    }
}
