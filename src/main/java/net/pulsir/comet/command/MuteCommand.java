package net.pulsir.comet.command;

import net.pulsir.comet.Comet;
import net.pulsir.comet.punishment.Punishment;
import net.pulsir.comet.punishment.type.PunishmentType;
import net.pulsir.comet.utils.calendar.ICalendar;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MuteCommand implements CommandExecutor {

    private boolean isSilent = false;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender.hasPermission("comet.command.mute"))) {
            sender.sendMessage(Objects.requireNonNull(Comet.getInstance().getLanguage().getConfiguration().getString("NO-PERMISSIONS")));
            return false;
        }

        if (args.length == 0) {
            getUsage(sender);
            return false;
        } else {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
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

                Date date = new Date();
                ICalendar calendar = new ICalendar(date);
                calendar.add("days", 9999999);

                if (Comet.getInstance().getPunishmentManger().getPunishments().get(player.getUniqueId()) == null) {
                    List<Punishment> punishments = new ArrayList<>(List.of(new Punishment(player.getUniqueId(), PunishmentType.MUTE, date,
                            calendar.getDate(),
                            reason,
                            sender.getName())));
                    Comet.getInstance().getPunishmentManger().getPunishments().put(player.getUniqueId(), punishments);
                } else {
                    Comet.getInstance().getPunishmentManger().getPunishments().get(player.getUniqueId()).add(
                            new Punishment(player.getUniqueId(), PunishmentType.MUTE, date,
                                    calendar.getDate(),
                                    reason,
                                    sender.getName())
                    );
                }

                if (!isSilent) {
                    Bukkit.broadcast(Comet.getInstance().getMessage().getMessage(Objects.requireNonNull(Comet.getInstance().getLanguage()
                                    .getConfiguration().getString("SUCCESS.MUTE-PUBLIC")).replace("{player}", args[0])
                            .replace("{staff}", sender.getName())
                            .replace("{reason}", reason)));
                } else {
                    for (final Player staff : Bukkit.getOnlinePlayers()) {
                        if (staff.hasPermission("comet.staff")) {
                            staff.sendMessage(Comet.getInstance().getMessage().getMessage(Objects.requireNonNull(Comet.getInstance().getLanguage()
                                            .getConfiguration().getString("SUCCESS.MUTE-SILENT")).replace("{player}", args[0])
                                    .replace("{staff}", sender.getName())
                                    .replace("{reason}", reason)));
                        }
                    }
                }

                if (Bukkit.getPlayer(args[0]) != null && Objects.requireNonNull(Bukkit.getPlayer(args[0])).isOnline()) {
                    Objects.requireNonNull(Bukkit.getPlayer(args[0])).sendMessage(Comet.getInstance().getMessage().getMessage(Objects.requireNonNull(Comet.getInstance().getLanguage()
                                    .getConfiguration().getString("PLAYER.MUTED"))
                            .replace("{reason}", reason)
                            .replace("{staff}", sender.getName())));
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
}
