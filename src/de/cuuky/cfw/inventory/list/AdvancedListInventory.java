package de.cuuky.cfw.inventory.list;

import de.cuuky.cfw.inventory.AdvancedInventory;
import de.cuuky.cfw.inventory.AdvancedInventoryManager;
import de.cuuky.cfw.inventory.ItemClick;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class AdvancedListInventory<T> extends AdvancedInventory {

    private final int size;
    private final List<T> list;

    public AdvancedListInventory(AdvancedInventoryManager manager, Player player, int size, List<T> list) {
        super(manager, player);

        this.size = size;
        this.list = list;
    }

    @Deprecated
    public AdvancedListInventory(AdvancedInventoryManager manager, Player player, int size, Supplier<List<T>> list) {
        this(manager, player, size, list.get());
    }

    public AdvancedListInventory(AdvancedInventoryManager manager, Player player, int size) {
        this(manager, player, size, (List<T>) null);
    }

    @Override
    public int getSize() {
        return this.size;
    }

    protected void addListItem(int index, T item) {
        this.addItem(index, this.getItemStack(item), this.getClick(item));
    }

    protected boolean copyList() {
        return true;
    }

    protected abstract ItemStack getItemStack(T item);

    protected abstract ItemClick getClick(T item);

    @Override
    protected final int getStartPage() {
        return 1;
    }

    @Override
    protected final int getMinPage() {
        return 1;
    }

    @Override
    public int getMaxPage() {
        List<T> original = this.getList();
        if (original == null || original.size() == 0)
            return 1;

        return (int) Math.ceil((float) original.size() / (float) this.getUsableSize());
    }

    @Override
    public void refreshContent() {
        List<T> original = this.getList();
        if (original == null)
            return;

        int start = this.getUsableSize() * (this.getPage() - 1);
        List<T> list = this.copyList() ? new ArrayList<>(original) : original;
        for (int i = 0; (start + i) < list.size() && i < this.getUsableSize(); i++) {
            T item = list.get(i + start);
            this.addListItem(i, item);
        }
    }

    public List<T> getList() {
        return list;
    }
}