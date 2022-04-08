package tr.zeltuv.basicchest.manager.impl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tr.zeltuv.basicchest.ChestPlugin;
import tr.zeltuv.basicchest.command.ICommand;
import tr.zeltuv.basicchest.command.impl.ChestCommand;
import tr.zeltuv.basicchest.manager.IManager;

public class CommandManager implements IManager {

    private ChestPlugin plugin;

    private ICommand[] commands = new ICommand[]{
            new ChestCommand()
    };

    @Override
    public void load(ChestPlugin plugin) {
        this.plugin = plugin;

        for (ICommand command : commands) {
            register(command);
        }
    }

    @Override
    public void unload() {

    }

    public void register(ICommand command) {
        plugin.getCommand(command.getCommandName()).setExecutor((commandSender, cmd, s, strings) -> {
            command.execute(commandSender, cmd, s, strings);
            return false;
        });
    }
}
