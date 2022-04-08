package tr.zeltuv.basicchest.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import tr.zeltuv.basicchest.ChestPlugin;
import tr.zeltuv.basicchest.cache.SlotItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * author: Zeltuv
 */

public class ItemSerializer {
    public static String serializeIntoString(List<SlotItem> slotItemList) {

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);

            bukkitObjectOutputStream.writeObject(slotItemList);
            bukkitObjectOutputStream.flush();

            String s = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());;

            bukkitObjectOutputStream.close();
            byteArrayOutputStream.close();

            return s;
        } catch (IOException e) {
            e.printStackTrace();
            ChestPlugin.log("Data corruption error !");
            return null;
        }
    }

    public static List<SlotItem> fromMapString(String data) {
        try {
            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(arrayInputStream);

            List<SlotItem> itemStacks = (List<SlotItem>) objectInputStream.readObject();

            objectInputStream.close();
            arrayInputStream.close();

            return itemStacks;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String serializeIntoString(ItemStack itemStack) {

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream(byteArrayOutputStream);

            bukkitObjectOutputStream.writeObject(itemStack);
            bukkitObjectOutputStream.flush();

            String s = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());;

            bukkitObjectOutputStream.close();
            byteArrayOutputStream.close();

            return s;
        } catch (IOException e) {
            e.printStackTrace();
            ChestPlugin.log("Data corruption error !");
            return null;
        }
    }

    public static ItemStack fromString(String data) {
        try {
            ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
            BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(arrayInputStream);

            ItemStack itemStack = (ItemStack) objectInputStream.readObject();

            objectInputStream.close();
            arrayInputStream.close();

            return itemStack;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
