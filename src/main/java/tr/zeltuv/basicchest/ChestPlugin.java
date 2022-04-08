package tr.zeltuv.basicchest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import tr.zeltuv.basicchest.cache.UserData;
import tr.zeltuv.basicchest.manager.IManager;
import tr.zeltuv.basicchest.manager.impl.CommandManager;
import tr.zeltuv.basicchest.manager.impl.ConfigurationManager;
import tr.zeltuv.basicchest.manager.impl.DatabaseManager;
import tr.zeltuv.basicchest.manager.impl.UserManager;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ChestPlugin extends JavaPlugin {

    private static ChestPlugin instance;
    private static Logger logger = Logger.getLogger("BasicChest");

    private ConfigurationManager configurationManager = new ConfigurationManager();
    private DatabaseManager databaseManager = new DatabaseManager();
    private UserManager userManager = new UserManager();
    private CommandManager commandManager = new CommandManager();

    private IManager[] managers = new IManager[]{
            configurationManager,
            databaseManager,
            userManager,
            commandManager,
    };

    @Override
    public void onEnable() {
        instance = this;
        for (IManager manager : managers) {
            manager.load(this);
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            UserData.load(onlinePlayer);
        }
    }

    @Override
    public void onDisable() {
        for (IManager manager : managers) {
            manager.unload();
        }
    }

    public static ChestPlugin get() {
        return instance;
    }

    public static void log(String msg){
        logger.info(msg);
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
