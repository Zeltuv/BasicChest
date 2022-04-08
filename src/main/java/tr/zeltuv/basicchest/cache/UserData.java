package tr.zeltuv.basicchest.cache;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.tr7zw.nbtapi.NBTItem;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tr.zeltuv.basicchest.ChestPlugin;
import tr.zeltuv.basicchest.manager.impl.DatabaseManager;
import tr.zeltuv.basicchest.utils.ItemBuilder;
import tr.zeltuv.basicchest.utils.ItemSerializer;

import javax.xml.crypto.Data;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static tr.zeltuv.basicchest.configuration.Configuration.CONFIGURATION;
import static tr.zeltuv.basicchest.configuration.Configuration.getMessage;

public class UserData {

    private DatabaseManager databaseManager;

    private Player player;
    private Map<Integer, List<SlotItem>> content = new ConcurrentHashMap<>();

    public UserData(DatabaseManager databaseManager, Player player) {
        this.databaseManager = databaseManager;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Map<Integer, List<SlotItem>> getContent() {
        return content;
    }

    public void openStorage(int page){
        String inventoryName = ChatColor.translateAlternateColorCodes(
                '&',
                CONFIGURATION.getString("Storage-Title")+
                CONFIGURATION.getString("Storage-Title-Page")
                );

        int size = CONFIGURATION.getInt("Storage-Size");

        Inventory inventory = Bukkit.createInventory(null, size,
                inventoryName.replace("%page%",page+""));


        content.getOrDefault(page,new ArrayList<>()).forEach(slotItem -> {
            inventory.setItem(slotItem.getSlot(),slotItem.getItemStack());
        });

        NBTItem previous = new NBTItem(
                new ItemBuilder(Material.PAPER)
                        .name("§7Previous Page").amount(page));

        previous.setString("changePage","previous");

        inventory.setItem(size-9,
                previous.getItem()
        );


        NBTItem next = new NBTItem(
                new ItemBuilder(Material.PAPER)
                        .name("§7Next Page").amount(page));

        next.setString("changePage","next");

        inventory.setItem(size-1,next.getItem());

        player.openInventory(inventory);
    }

    public void save() {
        ChestPlugin plugin = ChestPlugin.get();
        MongoCollection<Document> collection = plugin.getDatabaseManager().getCollection();

        Document document = new Document();

        if(collection.find(Filters.eq(
                        "uuid",player.getUniqueId().toString()))
                .first() == null) {
            createOnDatabase();
        }else{

            document.put("uuid",player.getUniqueId().toString());

            document.put("content",ItemSerializer.serializeIntoString(content));

            plugin.getDatabaseManager().getCollection().replaceOne(Filters.eq(
                    "uuid",player.getUniqueId().toString()),document);
        }
    }

    private void createOnDatabase(){
        ChestPlugin plugin = ChestPlugin.get();
        MongoCollection<Document> collection = plugin.getDatabaseManager().getCollection();

        Document document = new Document();

        document.put("uuid",player.getUniqueId().toString());
        document.put("content",ItemSerializer.serializeIntoString(content));

        collection.insertOne(document);
    }

    public static void load(Player player){
        ChestPlugin plugin = ChestPlugin.get();
        MongoCollection<Document> collection = plugin.getDatabaseManager().getCollection();

        Document document = collection.find(Filters.eq(
                        "uuid",player.getUniqueId().toString()))
                .first();

        if(document == null){

            UserData userData = new UserData(plugin.getDatabaseManager(), player);

            userData.save();

            plugin.getUserManager().getUserDatas().put(player,userData);

            return;
        }

        Map<Integer,List<SlotItem>> content = new HashMap<>();

        content.putAll(ItemSerializer.fromMapString(document.getString("content")));

        UserData userData = new UserData(plugin.getDatabaseManager(), player);

        userData.getContent().putAll(content);

        plugin.getUserManager().getUserDatas().put(player,userData);
    }
}
