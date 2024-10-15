import java.util.ArrayList;

public class WarehouseManager extends Employee {

    private Warehouse currentWarehouse; 

    public WarehouseManager(String name) {
        super(name);
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

            int option = Logistics.getValidatedInput("Please select an option (1-5)", 1, 5);

            switch (option) {
                case 1:
                    if (checkWarehouseAssigned()) loadShipmentToVehicle();
                    break;
                case 2:
                    if (checkWarehouseAssigned()) dropOffLoadToWarehouse();
                    break;
                case 3:
                    if (checkWarehouseAssigned()) manageVehicles();
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

    private void assignWarehouse() {
        System.out.println("Assign a warehouse to manage:");
        
        // Load warehouses from CSV or database and display them
        CSVParser.setFilePath("CSVFiles/warehouses.csv");
        String[][] warehouseData = CSVParser.loadCSVData(CSVParser.getFilePath());
        Warehouse[] availableWarehouses = Warehouse.toWarehouse(warehouseData);

        // Display the available warehouses
        for (int i = 0; i < availableWarehouses.length; i++) {
            System.out.printf("Warehouse %d: %s Hub\n", (i + 1), availableWarehouses[i].getLocation());
        }

        int choice = Logistics.getValidatedInput("Please select a warehouse (1-" + availableWarehouses.length + ")", 1, availableWarehouses.length);

        // Set the selected warehouse as the current warehouse
        this.currentWarehouse = availableWarehouses[choice - 1];
        System.out.printf("Assigned to warehouse at %s\n", currentWarehouse.getLocation());

        loadAndAssignResources();
    }

    private void loadAndAssignResources() {
        Vehicle[] vehicles = loadVehicles();
        Shipment[] shipments = loadShipments();

        ArrayList<Vehicle> whVehicle = new ArrayList<>();
        for (Vehicle vehicle : vehicles) { 
            if (vehicle.getWarehouseId() == currentWarehouse.getWarehouseID()) {
                whVehicle.add(vehicle);
            }
        }
        
        ArrayList<Shipment> whShipments = new ArrayList<>();
        for (Shipment shipment : shipments) { 
            if (shipment.getWarehouseId() == currentWarehouse.getWarehouseID()) {
                whShipments.add(shipment);
            }
        }

        for (int i = 0; i < whShipments.size(); i++) {
            boolean shipmentExists = false;

            for (int j = 0; j < currentWarehouse.getShipments().length; j++) {
                // Safety net: Skip if the shipment at index j is null
                if (currentWarehouse.getShipments()[j] == null) {
                    continue;  
                }
        
                if (currentWarehouse.getShipments()[j].getShipmentID() == whShipments.get(i).getShipmentID()) {
                    shipmentExists = true;
                    break;  
                }
            }
    
            if (!shipmentExists) {
                currentWarehouse.addShipment(whShipments.get(i));
            }
        }
        
        for (int i = 0; i < whVehicle.size(); i++) {
            boolean vehicleExists = false;

            for (int j = 0; j < currentWarehouse.getVehicles().length; j++) {
                // Safety net: Skip if the shipment at index j is null
                if (currentWarehouse.getVehicles()[j] == null) {
                    continue;  
                }
        
                if (currentWarehouse.getVehicles()[j].getVehicleID() == whVehicle.get(i).getVehicleID()) {
                    vehicleExists = true;
                    break;  
                }
            }
    
            if (!vehicleExists ) {
                currentWarehouse.addVehicle(whVehicle.get(i));
            }
        }
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

        loadAndAssignResources();
        // Ask for destination
        String inputtedDestination = Logistics.getInput("Enter destination to load shipments(Type 'Back' to cancel operation)");
        if (inputtedDestination.equalsIgnoreCase("Back")) return;
    
        // Filter shipments by warehouse ID, destination, and status "Pending"
        ArrayList<Shipment> loadToVehicleShipments = filterShipmentsForLoading(currentWarehouse.getShipments(), inputtedDestination);
        if (loadToVehicleShipments.isEmpty()) {
            System.out.println("No shipments available for the selected destination.");
            return;
        }
    
        // Select vehicle type (van or truck)
        String vehicleTypeChoice = chooseVehicleType();
    
        // Filter available vehicles by type and warehouse
        ArrayList<Vehicle> availableVehicles = filterAvailableVehicles(currentWarehouse.getVehicles(), vehicleTypeChoice, true);
        Vehicle selectedVehicle = selectVehicle(availableVehicles, vehicleTypeChoice);
        if (selectedVehicle == null) return;
    
        // Confirm loading
        if (!confirmAction("loading shipments to vehicle ID: " + selectedVehicle.getVehicleID())) {
            System.out.println("Loading canceled.");
            return;
        }
    
        // Load shipments into the selected vehicle
        loadShipmentsToVehicle(loadToVehicleShipments, selectedVehicle);
        // Update vehicle status to false
        selectedVehicle.setAvailability(false);
        // Update shipment and vehicle CSV files
        Shipment[] arrLoadToVehicleShipments = loadToVehicleShipments.toArray(new Shipment[0]);
        updateShipmentAndVehicleCSV(arrLoadToVehicleShipments, selectedVehicle);
    
        System.out.println("Shipments successfully loaded to " + vehicleTypeChoice + " with ID: " + selectedVehicle.getVehicleID());
    }
    
    public void dropOffLoadToWarehouse() {

        loadAndAssignResources();

        Shipment[] shipments = loadShipments();
        // Filter available trucks in the warehouse
        ArrayList<Vehicle> availableTrucks = filterAvailableVehicles(currentWarehouse.getVehicles(), "truck", false);
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

        loadAndAssignResources();
        while (true) {
            System.out.println("\nManage Vehicles");
            System.out.println("1. Vehicle Arrival");
            System.out.println("2. Vehicle Leaving");
            System.out.println("3. Return to Main Menu");

            int option = Logistics.getValidatedInput("Please select an option (1-3)", 1, 3);

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
        System.out.println("Vehicle arriving to warehouse... ");
        Vehicle[] vehicles = loadVehicles();
        ArrayList<Vehicle> possibleArrivingVehicles = new ArrayList<>();
    
        // Find vehicles not yet in any warehouse (warehouse ID == 0) and unavailable (isAvailable == false)
        for (Vehicle vehicle : vehicles) {
            if (!vehicle.isAvailable() && vehicle.getWarehouseId() == 0) {
                possibleArrivingVehicles.add(vehicle);
            }
        }
    
        if (possibleArrivingVehicles.isEmpty()) {
            System.out.println("No vehicles arriving.");
            return;
        }
    
        displayVehicleOptions(possibleArrivingVehicles, "arriving");
    
        int vehicleChoice = Logistics.getValidatedInput("Select a vehicle by number", 1, possibleArrivingVehicles.size());
        Vehicle selectedVehicle = possibleArrivingVehicles.get(vehicleChoice - 1);
    
        if (!confirmAction("marking vehicle ID: " + selectedVehicle.getVehicleID() + " as arrived")) {
            System.out.println("Arrival canceled.");
            return;
        } 
        System.out.println(currentWarehouse.getVehicles().length);
        selectedVehicle.setWarehouseId(currentWarehouse.getWarehouseID());
    
        // Update vehicle warehouse ID in CSV
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        CSVParser.updateCSV(selectedVehicle.getVehicleID(), String.valueOf(currentWarehouse.getWarehouseID()), 1, selectedVehicle.getVehicleHeader());

        System.out.println("Vehicle ID: " + selectedVehicle.getVehicleID() + " marked as arrived in warehouse: " + currentWarehouse.getLocation());
    }
    
    public void vehicleLeaving() {
        System.out.println("Vehicle leaving the warehouse...");
        
        Vehicle[] vehicles = loadVehicles();
        ArrayList<Vehicle> readyToLeaveVehicles = new ArrayList<>();
    
        // Find vehicles currently available in the warehouse
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getWarehouseId() == currentWarehouse.getWarehouseID()) {
                readyToLeaveVehicles.add(vehicle);
            }
        }
        if (readyToLeaveVehicles.isEmpty()) {
            System.out.println("No available vehicles ready to leave the warehouse.");
            return;
        }

        displayVehicleOptions(readyToLeaveVehicles, "leaving");
        int vehicleChoice = Logistics.getValidatedInput("Select a vehicle by number", 1, readyToLeaveVehicles.size());
        Vehicle selectedVehicle = readyToLeaveVehicles.get(vehicleChoice - 1);
        if (!confirmAction("marking vehicle ID: " + selectedVehicle.getVehicleID() + " as leaving")) {
            System.out.println("Departure canceled.");
            return;
        }
        currentWarehouse.removeVehicle(selectedVehicle);
        selectedVehicle.setWarehouseId(0);  // Indicate that vehicle is not on any warehouse
        selectedVehicle.setAvailability(false);
    
        // Update vehicle CSV (warehouse ID to 0, isAvailable to false)
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        CSVParser.updateCSV(selectedVehicle.getVehicleID(), String.valueOf(selectedVehicle.getWarehouseId()), 1, selectedVehicle.getVehicleHeader());
        CSVParser.updateCSV(selectedVehicle.getVehicleID(), String.valueOf(selectedVehicle.isAvailable()), 9, selectedVehicle.getVehicleHeader());

    
        System.out.println("Vehicle ID: " + selectedVehicle.getVehicleID() + " has left the warehouse.");
    }

    // Helper Methods
    private Shipment[] loadShipments() {
        // Load Packages from the CSV file first
        CSVParser.setFilePath("CSVFiles/packages.csv");
        String[][] packageData = CSVParser.loadCSVData(CSVParser.getFilePath());
        Package[] packages = new Package[packageData.length];
        for (int i = 0; i < packageData.length; i++) {
            packages[i] = Package.toPackage(packageData, i, null); // Pass null for the items
        }
        
        // Load shipments from the CSV file
        CSVParser.setFilePath("CSVFiles/shipments.csv");
        String[][] shipmentData = CSVParser.loadCSVData(CSVParser.getFilePath());
        Shipment[] shipments = new Shipment[shipmentData.length];
            
        // Assign the corresponding package directly in the toShipment method
        for (int i = 0; i < shipmentData.length; i++) {
            int pkgId = CSVParser.toInt(shipmentData[i][1]);  // Get the pkgID from shipment data
            
            // Find the corresponding package by ID
            Package correspondingPackage = null;
            for (Package pkg : packages) {
                if (pkg.getId() == pkgId) {
                    correspondingPackage = pkg;
                    break;  
                }
            }
                
            // Pass the corresponding package to the Shipment constructor
            shipments[i] = Shipment.toShipment(shipmentData, i, correspondingPackage);
        }
        
        return shipments;
    }

    private Vehicle[] loadVehicles() {
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        String[][] vehicleData = CSVParser.loadCSVData(CSVParser.getFilePath());
        return Vehicle.toVehicle(vehicleData);
    }

    private ArrayList<Shipment> filterShipmentsForLoading(Shipment[] shipments, String destination) {
        ArrayList<Shipment> filteredShipments = new ArrayList<>();
        for (Shipment shipment : shipments) {
            if (shipment != null && shipment.getWarehouseId() == currentWarehouse.getWarehouseID()
                    && shipment.getStatus().equalsIgnoreCase("Pending")
                    && shipment.getDestination().toLowerCase().contains(destination.toLowerCase())) {
                filteredShipments.add(shipment);
            }
        }
        return filteredShipments;
    }

    private void displayVehicleOptions(ArrayList<Vehicle> vehicles, String action) {
        System.out.println("Available vehicles for " + action + ":");
        for (int i = 0; i < vehicles.size(); i++) {
            System.out.printf("Vehicle %d ID: %d | Type: %s | Warehouse ID: %d | License Plate: %s | Current Shipments: %d\n", 
            (i + 1),
            vehicles.get(i).getVehicleID(),
            vehicles.get(i).getType(),
            vehicles.get(i).getWarehouseId(),
            vehicles.get(i).getLicensePlate(),
            vehicles.get(i).getCurrentShipmentCount()
            );
        }
    }

    private String chooseVehicleType() {
        String vehicleTypeChoice = "";
        while (!vehicleTypeChoice.equalsIgnoreCase("van") && !vehicleTypeChoice.equalsIgnoreCase("truck")) {
            vehicleTypeChoice = Logistics.getInput("Do you want to load shipments into a van (for receiver delivery) or a truck (for warehouse delivery)? (van/truck)");
            if (!vehicleTypeChoice.equalsIgnoreCase("van") && !vehicleTypeChoice.equalsIgnoreCase("truck")) {
                System.out.println("Please choose either 'van' or 'truck'.");
            }
        }
        return vehicleTypeChoice;
    }

    private ArrayList<Vehicle> filterAvailableVehicles(Vehicle[] vehicles, String vehicleType, boolean flag) {
        ArrayList<Vehicle> availableVehicles = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if (vehicle != null && (vehicle.getWarehouseId() == currentWarehouse.getWarehouseID()) && (vehicle.isAvailable() == flag)
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
            System.out.printf("Vehicle %d ID: %d | Type: %s | Warehouse ID: %d | License Plate: %s | Current Shipments: %d\n", 
                (i + 1),
                vehicle.getVehicleID(),
                vehicle.getType(),
                vehicle.getWarehouseId(),
                vehicle.getLicensePlate(),
                vehicle.getCurrentShipmentCount()
            );
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
                s.setWarehouseId(0); // Indicating its not on the warehouse anymore
                currentWarehouse.removeShipment(s);
                s.setVehicleId(vehicle.getVehicleID());
            } else {
                System.out.println("Cannot add Shipment ID: " + s.getShipmentID() + " to vehicle due to exceeding capacity and/or weight limit.");
            }
        }
    }

    private void assignShipmentsToVehicle(Shipment[] shipments, Vehicle vehicle) {
        ArrayList<Shipment> vehicleShipments = new ArrayList<>();
        for (Shipment shipment : shipments) {
            if (shipment.getVehicleId() == vehicle.getVehicleID())
            vehicleShipments.add(shipment);
        }
        vehicle.setShipments(vehicleShipments.toArray(new Shipment[0]));
    }

    private Shipment[] dropOffShipments(Vehicle vehicle) {
        Shipment[] droppedOffShipments = new Shipment[vehicle.getShipments().length];
        int index = 0;
        for (Shipment shipment : vehicle.getShipments()) {
            shipment.setVehicleId(0); // Indicating its not on the warehouse
            shipment.setWarehouseId(currentWarehouse.getWarehouseID()); // Indicating its on the warehouse
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
        CSVParser.setFilePath("CSVFiles/shipments.csv");
        for (Shipment s : shipments) {
            CSVParser.updateCSV(s.getShipmentID(), String.valueOf(s.getVehicleId()), 2, s.getShipmentHeader()); 
            CSVParser.updateCSV(s.getShipmentID(), String.valueOf(s.getWarehouseId()), 3, s.getShipmentHeader()); 
        }
        // update vehicle csv
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        CSVParser.updateCSV(vehicle.getVehicleID(), String.format("%d", (int)vehicle.getCurrentCapacityKG()), 6, vehicle.getVehicleHeader()); 
        CSVParser.updateCSV(vehicle.getVehicleID(), String.valueOf(vehicle.getCurrentShipmentCount()), 8, vehicle.getVehicleHeader()); 
        CSVParser.updateCSV(vehicle.getVehicleID(), String.valueOf(vehicle.isAvailable()), 9,vehicle.getVehicleHeader());
    }
}