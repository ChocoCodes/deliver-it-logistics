import java.util.Scanner;

public class Employee {
    protected String name;
    protected String username;
    protected String password;

    // Constructor
    public Employee(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    // Login method
    public boolean login(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Basic functionalities for employees
    public void encodeOrder(Customer customer, Item itemsToSend) {
        // Encode oder logic
    }

    public void processShipment(Package pkg, Customer customer) {
        // Process shipment logic
    }

    public void pickupPackage(Package pkg, Vehicle vehicle) {
        // Pickup package logic
    }

    public void sortPackage(Package pkg, Warehouse warehouse, Customer customer) {
        // Sorting package logic
    }

    // On progress until Shipment class is done
    // TODO: confirmDelivery(Shipment shipment)

public void showMenu() {
    Scanner scanner = new Scanner(System.in);
    int choice;

    do {
        System.out.println("\nEmployee Menu:");
        System.out.println("1. Encode Order");
        System.out.println("2. Process Shipment");
        System.out.println("3. Pickup Package");
        System.out.println("4. Sort Package");
        System.out.println("5. Exit");

        System.out.print("Enter your choice: ");
        choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // Call encodeOrder method
                System.out.println("Encoding order...");
                // Simulate customer and item (replace with actual logic)
                Customer customer = new Customer(); // Placeholder
                Item item = new Item(); // Placeholder
                encodeOrder(customer, item);
                break;
            case 2:
                // Call processShipment method
                System.out.println("Processing shipment...");
                // Simulate package and customer (replace with actual logic)
                Package pkg = new Package(); // Placeholder
                processShipment(pkg, customer); // Assume customer is defined
                break;
            case 3:
                // Call pickupPackage method
                System.out.println("Picking up package...");
                // Simulate package and vehicle (replace with actual logic)
                Vehicle vehicle = new Vehicle(); // Placeholder
                pickupPackage(pkg, vehicle);
                break;
            case 4:
                // Call sortPackage method
                System.out.println("Sorting package...");
                // Simulate package, warehouse, and customer (replace with actual logic)
                Warehouse warehouse = new Warehouse(); // Placeholder
                sortPackage(pkg, warehouse, customer);
                break;
            case 5:
                // Exit the menu
                System.out.println("Exiting Employee Menu...");
                break;
            default:
                System.out.println("Invalid choice, please try again.");
        }
    } while (choice != 5);

    System.out.println("Employee session ended.");
}

}
