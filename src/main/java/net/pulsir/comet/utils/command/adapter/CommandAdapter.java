package net.pulsir.comet.utils.command.adapter;

import net.pulsir.comet.utils.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommandAdapter implements CommandExecutor {

    private final List<Command> commands;

    public CommandAdapter(List<Command> commands){
        this.commands = commands;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length > 0) {
            for (Command cmd : commands) {
                if (args[0].equalsIgnoreCase(cmd.getName())) {
                    cmd.execute(commandSender, args);
                }
            }
        } else {
            for (Command cmd : commands) {
                if (cmd.allow()) {
                    if (cmd.permissible() && commandSender.hasPermission(cmd.permission())) {
                        commandSender.sendMessage(cmd.getUsage());
                    } else {
                        commandSender.sendMessage(cmd.getUsage());
                    }
                }
            }
        }

        return true;
    }
}