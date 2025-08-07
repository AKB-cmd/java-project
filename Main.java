/*
 * Main.java - Entry point of the Grocery Store Management System
 */
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GroceryStoreApp app = new GroceryStoreApp();
        app.run();
    }
}

/*
 * GroceryStoreApp.java - Handles the main loop and menu
 */
import java.util.Scanner;

class GroceryStoreApp {
    private InventoryManager inventoryManager;
    private BillingSystem billingSystem;
    private Scanner scanner;

    public GroceryStoreApp() {
        inventoryManager = new InventoryManager();
        billingSystem = new BillingSystem(inventoryManager);
        scanner = new Scanner(System.in);
    }

    public void run() {
        int choice;
        do {
            System.out.println("\n=== Grocery Store Management ===");
            System.out.println("1. View Inventory");
            System.out.println("2. Add Item");
            System.out.println("3. Delete Item");
            System.out.println("4. Generate Bill");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> inventoryManager.viewInventory();
                case 2 -> inventoryManager.addItem(scanner);
                case 3 -> inventoryManager.deleteItem(scanner);
                case 4 -> billingSystem.generateBill(scanner);
                case 0 -> System.out.println("Exiting application...");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 0);
    }
}

/*
 * GroceryItem.java - Represents a single item
 */
class GroceryItem {
    private String name;
    private double price;
    private int quantity;

    public GroceryItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String toString() {
        return name + " | ₹" + price + " | Qty: " + quantity;
    }
}

/*
 * InventoryManager.java - Manages item list
 */
import java.util.ArrayList;
import java.util.Scanner;

class InventoryManager {
    private ArrayList<GroceryItem> inventory;

    public InventoryManager() {
        inventory = new ArrayList<>();
        seedData();
    }

    public void seedData() {
        inventory.add(new GroceryItem("Rice", 60.0, 100));
        inventory.add(new GroceryItem("Wheat", 50.0, 80));
    }

    public void viewInventory() {
        System.out.println("\n-- Inventory --");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.get(i));
        }
    }

    public void addItem(Scanner scanner) {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine();
        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter quantity: ");
        int qty = scanner.nextInt();
        inventory.add(new GroceryItem(name, price, qty));
        System.out.println("Item added successfully.");
    }

    public void deleteItem(Scanner scanner) {
        viewInventory();
        System.out.print("Enter item number to delete: ");
        int index = scanner.nextInt() - 1;
        if (index >= 0 && index < inventory.size()) {
            inventory.remove(index);
            System.out.println("Item deleted.");
        } else {
            System.out.println("Invalid index.");
        }
    }

    public GroceryItem getItemByIndex(int index) {
        if (index >= 0 && index < inventory.size()) {
            return inventory.get(index);
        }
        return null;
    }

    public ArrayList<GroceryItem> getInventory() {
        return inventory;
    }
}

/*
 * BillingSystem.java - Generates bills
 */
import java.util.ArrayList;
import java.util.Scanner;

class BillingSystem {
    private InventoryManager inventoryManager;

    public BillingSystem(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }

    public void generateBill(Scanner scanner) {
        ArrayList<GroceryItem> inventory = inventoryManager.getInventory();
        double total = 0;
        StringBuilder bill = new StringBuilder("\n-- Bill Receipt --\n");

        while (true) {
            inventoryManager.viewInventory();
            System.out.print("Enter item number to add to bill (0 to finish): ");
            int choice = scanner.nextInt();
            if (choice == 0) break;

            GroceryItem item = inventoryManager.getItemByIndex(choice - 1);
            if (item == null) {
                System.out.println("Invalid choice. Try again.");
                continue;
            }

            System.out.print("Enter quantity: ");
            int qty = scanner.nextInt();
            if (qty > item.getQuantity()) {
                System.out.println("Insufficient stock.");
                continue;
            }

            double cost = item.getPrice() * qty;
            bill.append(item.getName()).append(" x ").append(qty).append(" = ₹").append(cost).append("\n");
            total += cost;
            item.setQuantity(item.getQuantity() - qty);
        }

        bill.append("Total: ₹").append(total);
        System.out.println(bill);
    }
}
