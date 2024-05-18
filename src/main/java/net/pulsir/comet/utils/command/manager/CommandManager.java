package net.pulsir.comet.utils.command.manager;

import net.pulsir.comet.utils.command.Command;
import net.pulsir.comet.utils.command.adapter.CommandAdapter;
import org.bukkit.command.PluginCommand;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private final PluginCommand pluginCommand;
    private final List<Command> commands = new ArrayList<>();

    public CommandManager(PluginCommand pluginCommand) {
        this.pluginCommand = pluginCommand;
    }

    public void addSubCommand(Command command){
        commands.add(command);
    }

    public void setCommand(List<Command> commandList) {
        commands.addAll(commandList);
    }

    public void registerCommands() {
        pluginCommand.setExecutor(new CommandAdapter(commands));
    }

    public List<Command> getRegisteredSubCommands() {
        return commands;
    }
}