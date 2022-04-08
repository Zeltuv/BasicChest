package tr.zeltuv.basicchest.cache;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;

public class SlotItem implements Serializable {

    private int slot;
    private ItemStack itemStack;

    public SlotItem(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
