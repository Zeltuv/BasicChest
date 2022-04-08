package tr.zeltuv.basicchest.manager.impl;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tr.zeltuv.basicchest.ChestPlugin;
import tr.zeltuv.basicchest.cache.SlotItem;
import tr.zeltuv.basicchest.cache.UserData;
import tr.zeltuv.basicchest.manager.IManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static tr.zeltuv.basicchest.configuration.Configuration.CONFIGURATION;

public class UserManager implements IManager, Listener {

    private ChestPlugin plugin;

    private Map<Player, UserData> userDatas = new ConcurrentHashMap<>();

    @Override
    public void load(ChestPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void unload() {

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UserData.load(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        userDatas.remove(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = Bukkit.getPlayer(event.getPlayer().getName());

        String inventoryName = ChatColor.translateAlternateColorCodes(
                '&',
                CONFIGURATION.getString("Storage-Title"));

        if (event.getInventory().getTitle().startsWith(inventoryName)) {
            //UserData userData = userDatas.get(player);
            saveFromChestInv(player, event.getInventory());
        }

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)
            return;

        String inventoryName = ChatColor.translateAlternateColorCodes(
                '&',
                CONFIGURATION.getString("Storage-Title"));

        if (event.getInventory().getTitle().startsWith(inventoryName)) {
            Player player = (Player) event.getWhoClicked();
            UserData userData = userDatas.get(player);

            Inventory inventory = event.getInventory();
            int size = inventory.getSize();
            int page = inventory.getItem(size - 9).getAmount();
            int maxPage = CONFIGURATION.getInt("Page-Amount");

            NBTItem nbtItem = new NBTItem(event.getCurrentItem());

            if (nbtItem.hasKey("changePage")) {
                String changePage = nbtItem.getString("changePage");

                if (changePage.equals("next")) {
                    event.setCancelled(true);
                    player.updateInventory();

                    if (page < maxPage) {
                        saveFromChestInv(player, inventory);
                        userData.openStorage(page + 1);
                        userData.save();
                        return;
                    }
                } else if (changePage.equals("previous")) {
                    event.setCancelled(true);
                    player.updateInventory();

                    if (page > 1) {
                        saveFromChestInv(player, inventory);
                        userData.openStorage(page - 1);
                        userData.save();
                        return;
                    }
                }
            }
        }
    }

    public void saveFromChestInv(Player player, Inventory inventory) {
        int size = inventory.getSize();
        int page = inventory.getItem(size - 9).getAmount();

        UserData userData = userDatas.get(player);

        if (!userData.getContent().containsKey(page))
            userData.getContent().put(page, new ArrayList<>());

        userData.getContent().get(page).clear();

        for (int i = 0; i < size; i++) {
            if (i != (size - 1) && i != (size - 9)) {
                if (inventory.getItem(i) != null && inventory.getItem(i).getType() != Material.AIR) {
                       userData.
                               getContent().
                               get(page)
                               .add(new SlotItem(i, inventory.getItem(i)));
                }
            }
        }
    }

    public Map<Player, UserData> getUserDatas() {
        return userDatas;
    }
}
