import java.util.Scanner;

public class Game {
    private Player player;
    private TextParser parser;

    public Game() {
        createRooms();
        parser = new TextParser();
    }

    private void createRooms() {
        Room entrance = new Room("You are at the entrance of a dark bunker. It smells musty and damp.", false);
        Room hallway = new Room("You are in a dimly lit hallway. There are doors to the north, south, east, and west.", true);
        Room storageRoom = new Room("You are in a storage room filled with old, rusted equipment.", true);
        Room commandCenter = new Room("You are in the command center. There are screens and buttons everywhere.", false);
        Room armory = new Room("You are in the armory. There are weapons and armor here.", false);
        Room bossRoom = new Room("You are in a large chamber. A fearsome mutated creature is here!", true);

        entrance.setExit("north", hallway);
        hallway.setExit("south", entrance);
        hallway.setExit("north", storageRoom);
        hallway.setExit("east", commandCenter);
        hallway.setExit("west", armory);
        storageRoom.setExit("south", hallway);
        commandCenter.setExit("west", hallway);
        armory.setExit("east", hallway);
        armory.setExit("north", bossRoom);

        bossRoom.setBoss(true); 
        storageRoom.setEnemyCount(2); 
        hallway.setEnemyCount(1); 

        Item flashlight = new Item("flashlight", "A small flashlight. It helps you see in the dark.");
        Item medkit = new Item("medkit", "A medical kit. It can heal your wounds.");
        Item keycard = new Item("keycard", "A keycard that grants access to restricted areas.");
        Item pistol = new Item("pistol", "A basic pistol. Useful for self-defense.");
        Item bodyArmor = new Item("body armor", "A body armor. It provides protection.");

        entrance.addItem(flashlight);
        storageRoom.addItem(medkit);
        commandCenter.addItem(keycard);
        armory.addItem(pistol);
        armory.addItem(bodyArmor);

        player = new Player(entrance);
    }

    public void play() {
        printWelcome();
        boolean finished = false;
        Scanner scanner = new Scanner(System.in);

        while (!finished) {
            System.out.print("> ");
            String input = scanner.nextLine();
            Command command = parser.parse(input);
            finished = processCommand(command);
            if (!finished) {
                printSuggestions();
            }
        }
        System.out.println("Thank you for playing. Goodbye!");
    }

    private void printWelcome() {
        System.out.println("Welcome to the Bunker!");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(player.getCurrentRoom().getDescription(player.hasItemEquipped("flashlight")));
        printSuggestions();
    }

    private void printSuggestions() {
        Room currentRoom = player.getCurrentRoom();
        boolean hasLight = player.hasItemEquipped("flashlight");
        System.out.println("\nYou can:");

        if (!currentRoom.getExits().isEmpty() && (hasLight || !currentRoom.isDark())) {
            System.out.print("Go: ");
            for (String direction : currentRoom.getExits().keySet()) {
                System.out.print(direction + " ");
            }
            System.out.println();
        }

        if (!currentRoom.getItems().isEmpty()) {
            System.out.print("Take: ");
            for (String item : currentRoom.getItems().keySet()) {
                System.out.print(item + " ");
            }
            System.out.println();
        }

        if (player.getEquippedItem() != null) {
            System.out.print("Unequip: " + player.getEquippedItem().getName() + " ");
        }

        if (!player.getInventory().getItems().isEmpty()) {
            System.out.print("Equip or Use: ");
            for (String item : player.getInventory().getItems().keySet()) {
                System.out.print(item + " ");
            }
            System.out.println();
        }

        System.out.println("Other commands: examine [item], inventory, quit\n");
    }

    private boolean processCommand(Command command) {
        String commandWord = command.getCommandWord();

        switch (commandWord) {
            case "go":
                goRoom(command.getSecondWord());
                break;
            case "take":
                takeItem(command.getSecondWord());
                break;
            case "open":
                openItem(command.getSecondWord());
                break;
            case "close":
                closeItem(command.getSecondWord());
                break;
            case "attack":
                attack(command.getSecondWord());
                break;
            case "examine":
                examineItem(command.getSecondWord());
                break;
            case "use":
                useItem(command.getSecondWord());
                break;
            case "equip":
                equipItem(command.getSecondWord());
                break;
            case "unequip":
                unequipItem();
                break;
            case "inventory":
                showInventory();
                break;
            case "help":
                printHelp();
                break;
            case "quit":
                return true;
            default:
                System.out.println("I don't understand that command.");
        }
        return false;
    }

    private void printHelp() {
        System.out.println("You can use the following commands:");
        System.out.println("go [direction] - Move to another room (e.g., 'go north')");
        System.out.println("take [item] - Take an item (e.g., 'take flashlight')");
        System.out.println("open [item] - Open an item (e.g., 'open door')");
        System.out.println("close [item] - Close an item (e.g., 'close door')");
        System.out.println("attack [target] - Attack a target (e.g., 'attack creature')");
        System.out.println("examine [item] - Examine an item (e.g., 'examine map')");
        System.out.println("use [item] - Use an item (e.g., 'use medkit')");
        System.out.println("equip [item] - Equip an item (e.g., 'equip pistol')");
        System.out.println("unequip - Unequip the current item");
        System.out.println("inventory - Show your inventory");
        System.out.println("quit - Quit the game");
    }

    private void goRoom(String direction) {
        if (direction == null) {
            System.out.println("Please specify a direction to go (e.g., 'go north').");
            return;
        }

        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("You can't go that way.");
        } else {
            player.setCurrentRoom(nextRoom);
            nextRoom.attackPlayer(player); 
            System.out.println(player.getCurrentRoom().getDescription(player.hasItemEquipped("flashlight")));
        }
    }

    private void takeItem(String itemName) {
        if (itemName == null) {
            System.out.println("Please specify an item to take (e.g., 'take flashlight').");
            return;
        }

        Room currentRoom = player.getCurrentRoom();
        if (currentRoom.hasItem(itemName)) {
            Item item = currentRoom.getItem(itemName);
            player.getInventory().addItem(item);
            currentRoom.removeItem(itemName);
            System.out.println("Taken.");
        } else {
            System.out.println("There is no " + itemName + " here.");
        }
    }

    private void openItem(String itemName) {
        if (itemName == null) {
            System.out.println("Please specify an item to open (e.g., 'open door').");
            return;
        }

        Room currentRoom = player.getCurrentRoom();
        if ("door".equals(itemName)) {
            currentRoom.setDoorOpen(true);
            System.out.println("Door opened.");
        } else if (currentRoom.hasItem(itemName)) {
            System.out.println("You open the " + itemName + " and find: " + currentRoom.getItem(itemName).getDescription());
        } else {
            System.out.println("There is no " + itemName + " here to open.");
        }
    }

    private void closeItem(String itemName) {
        if (itemName == null) {
            System.out.println("Please specify an item to close (e.g., 'close door').");
            return;
        }

        Room currentRoom = player.getCurrentRoom();
        if ("door".equals(itemName)) {
            currentRoom.setDoorOpen(false);
            System.out.println("Door closed.");
        } else {
            System.out.println("There is no " + itemName + " here to close.");
        }
    }

    private void attack(String target) {
        if (target == null) {
            System.out.println("Please specify a target to attack (e.g., 'attack creature').");
            return;
        }

        Room currentRoom = player.getCurrentRoom();
        if (currentRoom.getEnemyCount() > 0 && "enemy".equals(target)) {
            System.out.println("You attack the enemy with all your might!");

            player.setHealth(player.getHealth() - 10); 
            System.out.println("The enemy retaliates! You lose 10 health points. Your current health: " + player.getHealth());
            if (player.getHealth() <= 0) {
                System.out.println("You have been defeated by the enemy. Game over.");
                System.exit(0);
            } else {
                System.out.println("The enemy is defeated!");
                currentRoom.setEnemyCount(currentRoom.getEnemyCount() - 1); 
            }
        } else if (currentRoom.hasBoss() && "boss".equals(target)) {
            System.out.println("You attack the boss with all your might!");
            
            player.setHealth(player.getHealth() - 20); 
            System.out.println("The boss retaliates! You lose 20 health points. Your current health: " + player.getHealth());
            if (player.getHealth() <= 0) {
                System.out.println("You have been defeated by the boss. Game over.");
                System.exit(0);
            } else {
                System.out.println("The boss is defeated! You win!");
                currentRoom.setBoss(false); 
            }
        } else {
            System.out.println("There is no " + target + " here to attack.");
        }
    }

    private void examineItem(String itemName) {
        if (itemName == null) {
            System.out.println("Please specify an item to examine (e.g., 'examine map').");
            return;
        }

        Room currentRoom = player.getCurrentRoom();
        Inventory inventory = player.getInventory();

        if (currentRoom.hasItem(itemName)) {
            Item item = currentRoom.getItem(itemName);
            System.out.println("You see a " + itemName + ": " + item.getDescription());
        } else if (inventory.hasItem(itemName)) {
            Item item = inventory.getItem(itemName);
            System.out.println("You examine your " + itemName + ": " + item.getDescription());
        } else {
            System.out.println("There is no " + itemName + " here to examine.");
        }
    }

    private void useItem(String itemName) {
        if (itemName == null) {
            System.out.println("Please specify an item to use (e.g., 'use medkit').");
            return;
        }

        Inventory inventory = player.getInventory();

        if (inventory.hasItem(itemName)) {
            Item item = inventory.getItem(itemName);
            if ("medkit".equals(itemName)) {
                player.setHealth(player.getHealth() + 20);
                System.out.println("You use the medkit. Your health is now: " + player.getHealth());
                inventory.removeItem(itemName); 
            } else if ("flashlight".equals(itemName)) {
                System.out.println("You use the flashlight. " + item.getDescription());
            } else {
               
                System.out.println("You use the " + itemName + ". " + item.getDescription());
            }
        } else {
            System.out.println("You don't have a " + itemName + " to use.");
        }
    }

    private void equipItem(String itemName) {
        if (itemName == null) {
            System.out.println("Please specify an item to equip (e.g., 'equip pistol').");
            return;
        }

        Inventory inventory = player.getInventory();

        if (inventory.hasItem(itemName)) {
            Item item = inventory.getItem(itemName);
            player.equipItem(item);
            inventory.removeItem(itemName);
            System.out.println("You have equipped the " + itemName + ".");
        } else {
            System.out.println("You don't have a " + itemName + " to equip.");
        }
    }

    private void unequipItem() {
        Item equippedItem = player.getEquippedItem();
        if (equippedItem != null) {
            player.getInventory().addItem(equippedItem);
            player.unequipItem();
            System.out.println("You have unequipped the " + equippedItem.getName() + ".");
        } else {
            System.out.println("You don't have an item equipped.");
        }
    }

    private void showInventory() {
        Inventory inventory = player.getInventory();
        if (inventory.getItems().isEmpty()) {
            System.out.println("Your inventory is empty.");
        } else {
            System.out.println("You have the following items in your inventory:");
            for (String itemName : inventory.getItems().keySet()) {
                System.out.println("- " + itemName);
            }
        }
        Item equippedItem = player.getEquippedItem();
        if (equippedItem != null) {
            System.out.println("Equipped: " + equippedItem.getName());
        }
    }
}
