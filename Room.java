import java.util.HashMap;
import java.util.Map;

public class Room {
    private String description;
    private Map<String, Room> exits;
    private Map<String, Item> items;
    private boolean doorOpen;
    private boolean hasBoss;
    private int enemyCount;
    private boolean isDark;
    private int enemyDamage;

    public Room(String description, boolean isDark) {
        this.description = description;
        this.exits = new HashMap<>();
        this.items = new HashMap<>();
        this.doorOpen = false;
        this.hasBoss = false;
        this.enemyCount = 0;
        this.isDark = isDark;
        this.enemyDamage = 5; // Default enemy damage
    }

    public void setExit(String direction, Room room) {
        exits.put(direction, room);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public String getDescription(boolean hasLight) {
        if (isDark && !hasLight) {
            return "It's too dark to see anything.";
        }
        StringBuilder fullDescription = new StringBuilder(description);
        if (!items.isEmpty()) {
            fullDescription.append(" You see: ");
            items.forEach((name, item) -> fullDescription.append(name).append(" (").append(item.getDescription()).append("), "));
            fullDescription.setLength(fullDescription.length() - 2); 
            fullDescription.append(".");
        }
        if (enemyCount > 0) {
            fullDescription.append(" There ").append(enemyCount > 1 ? "are" : "is").append(" ").append(enemyCount).append(" enemy").append(enemyCount > 1 ? "ies" : "y").append(" here!");
        }
        if (hasBoss) {
            fullDescription.append(" A big boss is here!");
        }
        return fullDescription.toString();
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

    public boolean isDoorOpen() {
        return doorOpen;
    }

    public void setDoorOpen(boolean doorOpen) {
        this.doorOpen = doorOpen;
    }

    public boolean hasBoss() {
        return hasBoss;
    }

    public void setBoss(boolean hasBoss) {
        this.hasBoss = hasBoss;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }

    public boolean isDark() {
        return isDark;
    }

    public Map<String, Room> getExits() {
        return exits;
    }

    public Map<String, Item> getItems() {
        return items;
    }

    public void setEnemyDamage(int damage) {
        this.enemyDamage = damage;
    }

    public int getEnemyDamage() {
        return enemyDamage;
    }

    public void attackPlayer(Player player) {
        if (enemyCount > 0 || hasBoss) {
            player.takeDamage(enemyDamage);
            System.out.println("An enemy attacks you! You lose " + enemyDamage + " health points. Your current health: " + player.getHealth());
            if (!player.isAlive()) {
                System.out.println("You have been defeated by the enemy. Game over.");
                System.exit(0);
            }
        }
    }
}
