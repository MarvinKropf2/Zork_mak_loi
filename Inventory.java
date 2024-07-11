import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<String, Item> items;

    public Inventory() {
        this.items = new HashMap<>();
    }

    public void addItem(Item item) {
        items.put(item.getName(), item);
    }

    public Item getItem(String name) {
        return items.get(name);
    }

    public boolean hasItem(String name) {
        return items.containsKey(name);
    }

    public void removeItem(String name) {
        items.remove(name);
    }

    public Map<String, Item> getItems() {
        return items;
    }
}

