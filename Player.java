import java.util.ArrayList;
import java.util.List;

public class Player {
    private int health;
    private Inventory inventory;
    private Room currentRoom;
    private Item equippedItem;

    public Player(Room startRoom) {
        this.health = 100; 
        this.inventory = new Inventory();
        this.currentRoom = startRoom;
        

        this.inventory.addItem(new Item("knife", "A sharp knife. Useful for self-defense."));
        this.inventory.addItem(new Item("map", "A map of the bunker."));
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public Item getEquippedItem() {
        return equippedItem;
    }

    public void equipItem(Item item) {
        this.equippedItem = item;
    }

    public void unequipItem() {
        this.equippedItem = null;
    }

    public boolean hasItemEquipped(String itemName) {
        return equippedItem != null && equippedItem.getName().equalsIgnoreCase(itemName);
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) this.health = 0;
    }

    public boolean isAlive() {
        return this.health > 0;
    }
}
