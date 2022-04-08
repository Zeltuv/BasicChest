package tr.zeltuv.basicchest.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface ICommand {

    String getCommandName();

    void execute(CommandSender sender, Command command,String label, String[] args);
}
