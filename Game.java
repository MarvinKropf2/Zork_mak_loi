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

