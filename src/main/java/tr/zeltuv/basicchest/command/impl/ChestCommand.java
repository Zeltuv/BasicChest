package tr.zeltuv.basicchest.command.impl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tr.zeltuv.basicchest.ChestPlugin;
import tr.zeltuv.basicchest.cache.UserData;
import tr.zeltuv.basicchest.command.ICommand;

import static tr.zeltuv.basicchest.configuration.Configuration.getMessage;

public class ChestCommand implements ICommand {

    @Override
    public String getCommandName() {
        return "chest";
    }

    @Override
    public void execute(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            UserData userData = ChestPlugin.get().getUserManager().getUserDatas().get(player);

            userData.openStorage(1);
            player.sendMessage(getMessage("open-storage"));

        }
    }
}
