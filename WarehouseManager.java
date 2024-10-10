import java.util.ArrayList;

public class WarehouseManager extends Employee {

    private Warehouse currentWarehouse; 

    public WarehouseManager(String name, String username, String password) {
        super(name, username, password);
        this.currentWarehouse = null;
    }

    @Override
    public void showMenu() {
        while (true) {
            if (currentWarehouse == null) {
                assignWarehouse();
            }

            System.out.println("\nWarehouse Manager Menu");
            System.out.println("1. Load Packages by Destination");
            System.out.println("2. Drop Off Vehicle Load to Warehouse");
            System.out.println("3. Manage Vehicles");
            System.out.println("4. Assign / Change Warehouse");
            System.out.println("5. Exit");

            int option = Logistics.getValidatedInput("Please select an option (1-5): ", 1, 5);

            switch (option) {
                case 1:
                    if (checkWarehouseAssigned()) {
                            loadShipmentToVehicle();
                        }
                        break;
                case 2:
                    if (checkWarehouseAssigned()) {
                        dropOffLoadToWarehouse();
                       }
                    break;
                case 3:
                    if (checkWarehouseAssigned()) {
                        manageVehicles();
                    }
                    break;
                case 4:
                    assignWarehouse(); // Option to change the warehouse
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
                    break;
                }
        }
    }

    // Method to assign a warehouse for the manager to manage
    private void assignWarehouse() {
        System.out.println("Assign a warehouse to manage:");
        // Load warehouses from CSV or database and display them
        CSVParser.setFilePath("CSVFiles/warehouses.csv");
        String[][] warehouseData = CSVParser.loadCSVData(CSVParser.getFilePath());
        Warehouse[] availableWarehouses = Warehouse.toWarehouse(warehouseData);

        for (int i = 0; i < availableWarehouses.length; i++) {
            System.out.println((i + 1) + ". " + availableWarehouses[i].getLocation());
        }

        int choice = Logistics.getValidatedInput("Please select a warehouse (1-" + availableWarehouses.length + "): ", 1, availableWarehouses.length);

        // Set the selected warehouse as the current warehouse
        currentWarehouse = availableWarehouses[choice - 1];
        System.out.println("Assigned to warehouse at: " + currentWarehouse.getLocation());
    }

    // Check if a warehouse is assigned before proceeding with operations
    private boolean checkWarehouseAssigned() {
        if (currentWarehouse == null) {
            System.out.println("No warehouse assigned. Please assign a warehouse first.");
            return false;
        }
        return true;
    }

    public void loadShipmentToVehicle() {
        // Load shipment and vehicle data
        Shipment[] shipments = loadShipments();
        Vehicle[] vehicles = loadVehicles();
    
        // Ask for destination
        String inputtedDestination = Logistics.getInput("Enter destination to load shipments: ");
    
        // Filter shipments by warehouse ID, destination, and status "Pending"
        ArrayList<Shipment> loadToVehicleShipments = filterShipmentsForLoading(shipments, inputtedDestination);
    
        if (loadToVehicleShipments.isEmpty()) {
            System.out.println("No shipments available for the selected destination.");
            return;
        }
    
        // Select vehicle type (van or truck)
        String vehicleTypeChoice = chooseVehicleType();
    
        // Filter available vehicles by type and warehouse
        ArrayList<Vehicle> availableVehicles = filterAvailableVehicles(vehicles, vehicleTypeChoice);
    
        Vehicle selectedVehicle = selectVehicle(availableVehicles, vehicleTypeChoice);
        if (selectedVehicle == null) return;
    
        // Confirm loading
        if (!confirmAction("loading shipments to vehicle ID: " + selectedVehicle.getVehicleID())) {
            System.out.println("Loading canceled.");
            return;
        }
    
        // Load shipments into the selected vehicle
        loadShipmentsToVehicle(loadToVehicleShipments, selectedVehicle);
    
        // Update shipment and vehicle CSV files
        updateShipmentAndVehicleCSV(loadToVehicleShipments, selectedVehicle);
    
        System.out.println("Shipments successfully loaded to " + vehicleTypeChoice + " with ID: " + selectedVehicle.getVehicleID());
    }
    
    public void dropOffLoadToWarehouse() {
        // Load vehicle and shipment data
        Vehicle[] vehicles = loadVehicles();
        Shipment[] shipments = loadShipments();
    
        // Filter available trucks in the warehouse
        ArrayList<Vehicle> availableTrucks = filterAvailableVehicles(vehicles, "truck");
    
        if (availableTrucks.isEmpty()) {
            System.out.println("No trucks available for drop-off.");
            return;
        }
    
        // Display and let user select a truck for drop-off
        Vehicle selectedVehicle = selectVehicle(availableTrucks, "truck");
        if (selectedVehicle == null) return;
    
        // Assign shipments to the selected vehicle
        assignShipmentsToVehicle(shipments, selectedVehicle);
    
        // Drop off shipments at the warehouse
        Shipment[] droppedOffShipments = dropOffShipments(selectedVehicle);
    
        // Confirm and display drop-off result
        displayDropOffResult(selectedVehicle, droppedOffShipments);
    
        // Update CSV files
        updateShipmentAndVehicleCSV(droppedOffShipments, selectedVehicle);
    
        System.out.println("Vehicle and shipment records updated successfully.");
    }

    public void manageVehicles() {
        while (true) {
            System.out.println("\nManage Vehicles");
            System.out.println("1. Vehicle Arrival");
            System.out.println("2. Vehicle Leaving");
            System.out.println("3. Return to Main Menu");

            int option = Logistics.getValidatedInput("Please select an option (1-3): ", 1, 3);

            switch (option) {
                case 1:
                    vehicleArrival();
                    break;
                case 2:
                    vehicleLeaving();
                    break;
                case 3:
                    System.out.println("Returning to main menu...");
                    return;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
                    break;
                }
        }
    }

    public void vehicleArrival() {
        // Logic for vehicle arriving in the warehouse
        System.out.println("Vehicle arriving to warehouse... ");
    }

    public void vehicleLeaving() {
        //Logic for vehicle leaving the warehouse
        System.out.println("Vehicle leaving the warehouse...");
    }

    // Helper Methods
    private Shipment[] loadShipments() {
        CSVParser.setFilePath("CSVFiles/shipments.csv");
        String[][] shipmentData = CSVParser.loadCSVData(CSVParser.getFilePath());
        return Shipment.toShipment(shipmentData);
    }

    private Vehicle[] loadVehicles() {
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        String[][] vehicleData = CSVParser.loadCSVData(CSVParser.getFilePath());
        return Vehicle.toVehicle(vehicleData);
    }

    private ArrayList<Shipment> filterShipmentsForLoading(Shipment[] shipments, String destination) {
        ArrayList<Shipment> filteredShipments = new ArrayList<>();
        for (Shipment shipment : shipments) {
            if (shipment.getWarehouseID() == currentWarehouse.getWarehouseID()
                    && shipment.getStatus().equalsIgnoreCase("Pending")
                    && shipment.getDestination().equalsIgnoreCase(destination)) {
                filteredShipments.add(shipment);
            }
        }
        return filteredShipments;
    }

    private String chooseVehicleType() {
        String vehicleTypeChoice = "";
        while (!vehicleTypeChoice.equalsIgnoreCase("van") && !vehicleTypeChoice.equalsIgnoreCase("truck")) {
            vehicleTypeChoice = Logistics.getInput("Do you want to load shipments into a van (for receiver delivery) or a truck (for warehouse delivery)? (van/truck): ");
            if (!vehicleTypeChoice.equalsIgnoreCase("van") && !vehicleTypeChoice.equalsIgnoreCase("truck")) {
                System.out.println("Please choose either 'van' or 'truck'.");
            }
        }
        return vehicleTypeChoice;
    }

    private ArrayList<Vehicle> filterAvailableVehicles(Vehicle[] vehicles, String vehicleType) {
        ArrayList<Vehicle> availableVehicles = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getWarehouseID() == currentWarehouse.getWarehouseID() && vehicle.isAvailable()
                    && vehicle.getType().equalsIgnoreCase(vehicleType)) {
                availableVehicles.add(vehicle);
            }
        }
        return availableVehicles;
    }

    private Vehicle selectVehicle(ArrayList<Vehicle> availableVehicles, String vehicleTypeChoice) {
        if (availableVehicles.isEmpty()) {
            System.out.println("No available " + vehicleTypeChoice + "s for loading shipments.");
            return null;
        }

        System.out.println("Available " + vehicleTypeChoice + "s: ");
        int i = 0;
        for (Vehicle vehicle : availableVehicles) {
            System.out.println((i + 1) + ". " + vehicle.toString());
            i++;
        }

        int vehicleChoice = Logistics.getValidatedInput("Select a vehicle by number: ", 1, availableVehicles.size());
        return availableVehicles.get(vehicleChoice - 1);
    }

    private boolean confirmAction(String action) {
        String confirm = Logistics.getInput("Confirm " + action + "? (Yes/No)");
        return confirm.equalsIgnoreCase("Yes");
    }

    private void loadShipmentsToVehicle(ArrayList<Shipment> shipments, Vehicle vehicle) {
        for (Shipment s : shipments) {
            if (vehicle.addShipment(s)) {
                currentWarehouse.removeShipment(s);
                s.setVehicleID(vehicle.getVehicleID());
            } else {
                System.out.println("Cannot add Shipment ID: " + s.getShipmentID() + " to vehicle due to exceeding capacity and/or weight limit.");
            }
        }
    }

    private void assignShipmentsToVehicle(Shipment[] shipments, Vehicle vehicle) {
        for (Shipment shipment : shipments) {
            if (shipment.getVehicleID() == vehicle.getVehicleID()) {
                vehicle.addShipment(shipment);
            }
        }
    }

    private Shipment[] dropOffShipments(Vehicle vehicle) {
        Shipment[] droppedOffShipments = new Shipment[vehicle.getShipments().length];
        int index = 0;
        for (Shipment shipment : vehicle.getShipments()) {
            vehicle.removeShipment(shipment);
            currentWarehouse.addShipment(shipment);
            droppedOffShipments[index] = shipment;
            index++;
        }
        vehicle.setAvailability(true); // After drop-off, set vehicle availability to true
        return droppedOffShipments;
    }

    private void displayDropOffResult(Vehicle vehicle, Shipment[] droppedOffShipments) {
        System.out.println("Drop-off completed for vehicle ID: " + vehicle.getVehicleID());
        System.out.println("Dropped off shipments: ");
        for (Shipment s : droppedOffShipments) {
            System.out.println("Shipment ID: " + s.getShipmentID() + " | Destination: " + s.getDestination());
        }
    }

    private void updateShipmentAndVehicleCSV(Shipment[] shipments, Vehicle vehicle) {
        for (Shipment s : shipments) {
            CSVParser.updateShipmentCSV(s.getShipmentID(), vehicle.getVehicleID(), 3); // TODO: Change to actual column
        }

        CSVParser.updateVehicleCSV(vehicle.getVehicleID(), vehicle.getCurrentCapacityKG(), 7); // Update current capacity
        CSVParser.updateVehicleCSV(vehicle.getVehicleID(), vehicle.getCurrentShipmentCount(), 9); // Update shipment count
        CSVParser.updateVehicleCSV(vehicle.getVehicleID(), vehicle.isAvailable(), 10); // Update availability
    }
    // MISCELLANEOUS METHODS

}