import java.util.InputMismatchException;

public class Employee {
    protected String name;
    protected String username;
    protected String password;
    protected Logistics logistics;
    protected CSVParser parser;

    // Constructor
    public Employee(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.logistics = new Logistics();
        this.parser = new CSVParser();
    }

    public String getUsername() {
        return username;
    }

    // Login method
    public boolean login(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Basic functionalities for employees
    public void encodeOrder() {
        String customerName = logistics.getInput("Enter Customer Name");
        Customer customer = parser.searchCustomerName(customerName);
        if (customer == null) {
            System.out.println("Customer not found, Please register first.");
        }
        // Gather Item details
        Item[] items = logistics.getCustomerItems();
        String recieverAddress = logistics.getInput("Enter Reciever Address");

        //Create Package
        Package pkg = new Package(parser.getLatestID() + 1, items, recieverAddress);

        // Save package for further processing
        parser.setFilePath("CSVFiles/packages.csv");
        // Not yet implemented due to missing method in CSV Parser
        // TODO parser.savePackageEntry(pkg);

        System.out.println("Order encoded successfully.");
    }

    public void processShipment() {
        // Display available packages for shipment
        System.out.println("Available Packages for Shipment:");
        // TODO Need Method to read packages from csv file and return packages[]

        Package[] packages = new Package[5]; // Place Holder

        for (Package pkg : packages) {
            System.out.println(pkg.toString());
        }
    
        // Ask user to select a package by ID
        int packageId = Integer.parseInt(logistics.getInput("Enter Package ID to process shipment: "));
        Package pkgToShip = null;
    
        // Search for the package
        for (Package pkg : packages) {
            if (pkg.getId() == packageId) {
                pkgToShip = pkg;
                break;
            }
        }
    
        if (pkgToShip != null) {
            // Logic for processing the shipment
            // Method to select vehicle (to be implemented)
            // Method to select warehouse (to be implemented)
    
            //Shipment shipment = new Shipment();
        
            // Display confirmation
            System.out.println("Package ID " + pkgToShip.getId() + " has been processed for shipment successfully.");
        } else {
            System.out.println("Package with ID " + packageId + " not found.");
        }
    }

    public void sortPackage() {
        // TODO: Display Warehouses (Read Warehouse CSV)
        // TODO: Ask employee ID warehouse to sort get the info from warehouse csv and Return Warehouse
        // TODO: Based on the warehouse ID  read the package csv and return pkg and fill warehouse.packages[] that has matching ID
        
    Warehouse selectedWarehouse = new Warehouse(0, name, 1000, 5); // Placeholder for now

    // Ask user to select a package
    System.out.println("Packages in Warehouse Location: " + selectedWarehouse.getLocation() + ":");
    for (int i = 0; i < selectedWarehouse.getCurrentPackageCount(); i++) {
        Package pkg = selectedWarehouse.getPackages()[i];
        if (pkg != null) {
            System.out.println(pkg.toString());
        }
    }

    int packageId = Integer.parseInt(logistics.getInput("Enter Package ID to sort: "));
    Package pkg = null;

    // Search for the package in the selected warehouse
    for (int i = 0; i < selectedWarehouse.getCurrentPackageCount(); i++) {
        if (selectedWarehouse.getPackages()[i] != null && selectedWarehouse.getPackages()[i].getId() == packageId) {
            pkg = selectedWarehouse.getPackages()[i];
            break;
        }
    }

    // If package is found, proceed to sort it
    if (pkg != null) {
        // Confirm package details for sorting
        System.out.println("Sorting Package ID: " + pkg.getId());
        System.out.println("Contents: " + pkg.getContents().length + " item(s), Receiver Address: " + pkg.getreceiverAddress());

        // TODO: Apply Sorting Logic

        System.out.println("Package ID " + pkg.getId() + " has been sorted successfully.");
    } else {
        System.out.println("Package with ID " + packageId + " not found in the selected warehouse.");
    }
    }

public void showMenu() {
    int choice;

    do {
        System.out.println("\nEmployee Menu:");
        System.out.println("1. Encode Order");
        System.out.println("2. Process Shipment");
        System.out.println("3. Sort Package");
        System.out.println("5. Exit");

        while(true) {
            try {
                choice = Integer.parseInt(logistics.getInput("Enter your choice: "));
                break;
            } catch (InputMismatchException e) {
                System.out.println("Enter a valid Integer.");
            }
            
        }

        switch (choice) {
            case 1:
                System.out.println("Encoding order...");
                encodeOrder();
                break;
            case 2:
                System.out.println("Processing shipment...");
                System.out.println("Work in progress");
                break;
            case 3:
                System.out.println("Sorting package...");
                break;
            case 4:
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
