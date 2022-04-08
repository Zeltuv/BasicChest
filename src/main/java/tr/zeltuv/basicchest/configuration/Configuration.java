package tr.zeltuv.basicchest.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import tr.zeltuv.basicchest.ChestPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public enum Configuration {

    CONFIGURATION("configuration"),
    MESSAGES("messages");

    private File file;
    private FileConfiguration config;
    private final String fileName;

    Configuration(String fileName) {
        this.fileName = fileName;
    }

    public FileConfiguration getFileConfiguration() {
        return config;
    }

    public void initializeConfiguration() {
        file = new File(ChestPlugin.get().getDataFolder(), fileName + ".yml");
        if (!file.exists()) {
            ChestPlugin.get().saveResource(fileName + ".yml", false);
        }
        config = YamlConfiguration.loadConfiguration(file);
        ChestPlugin.log(fileName + ".yml loaded successfully !");
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }
    public String getString(String path){
        return getString(path,false);
    }

    public String getString(String path,boolean replace){
        String string = getFileConfiguration().getString(path);

        if(replace)
            string = string.replace("&","§");

        return string;
    }

    public static String getMessage(String path){
        String string = MESSAGES.getString(path,true);

        return string;
    }

    public int getInt(String path){
        return getFileConfiguration().getInt(path);
    }

    public double getDouble(String path){
        return getFileConfiguration().getDouble(path);
    }

    public boolean getBoolean(String path){
        return getFileConfiguration().getBoolean(path);
    }

    public List<String> getStringList(String path, boolean replace){
        List<String> strings = getFileConfiguration().getStringList(path);

        if(replace) {
            strings = new ArrayList<>();

            for (String s : getFileConfiguration().getStringList(path)) {
                strings.add(s.replace("&","§"));
            }
        }

        return strings;
    }
}