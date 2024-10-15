import java.util.ArrayList;

public class Driver extends Employee {
    private Vehicle currentAssignedVehicle;

    public Driver(String name) {
        super(name);
    }

    @Override
    public void showMenu() {
        boolean done = false;

        while (!isDriverAssigned()) {
            System.out.println("Driver name is not detected in any vehicle yet. Please assign a vehicle first.");
            assignVehicle();
        }

        currentAssignedVehicle = getAssignedVehicle();
        
        while (!done) {
            System.out.println("Welcome, " + this.getName()); 
            System.out.println("---------------------------------");
            System.out.println("1. Re-assign Vehicle");
            System.out.println("2. Deliver Package");
            System.out.println("3. Done");
            System.out.println("---------------------------------");
    
            // Getting user input and handling exceptions
            int choice = Logistics.getValidatedInput("Select a number: ", 1, 3);
    
            switch (choice) {
                case 1:
                    assignVehicle();
                    break;
                case 2:
                    if (!isDriverAssigned()) {
                        System.out.println("Please assign a vehicle before delivering a package.");
                    } else {
                        deliverPackage();
                    }
                    break;
                case 3:
                    System.out.println("Exiting the system. Goodbye!");
                    done = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
                    break;
            }
        }
    }

    public void assignVehicle() {
        // Load available vehicles from the CSV file
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        String[][] vehicleData = CSVParser.loadCSVData(CSVParser.getFilePath());
        Vehicle[] vehicles = Vehicle.toVehicle(vehicleData); 

        // Display available vehicles
        System.out.println("Available Vehicles:");
        int i = 0;
        ArrayList<Vehicle> availableVehicles = new ArrayList<Vehicle>();
        for (Vehicle v : vehicles) {
            // Check if the driver is either null or empty, and the vehicle is available
            if (
                (v.getDriver().equals("null") || v.getDriver().equalsIgnoreCase("NA") || 
                v.getDriver().trim().isEmpty()) && v.isAvailable() && v.getType().equalsIgnoreCase("Van")) {
                    System.out.printf("Vehicle %d ID: %d | Type: %s | Warehouse ID: %d | License Plate: %s | Current Shipments: %d\n", 
                    (i + 1),
                    v.getVehicleID(),
                    v.getType(),
                    v.getWarehouseId(),
                    v.getLicensePlate(),
                    v.getCurrentShipmentCount()
                    );
                availableVehicles.add(v);
                i++;
            }
        }

        if (availableVehicles.isEmpty()) {
            System.out.println("All vehicles are filled. There is no available vehicles at the moment. Try again later."); 
            return;
        } 
        // Select Vehicle to Assign to Driver and Remove their name on the previous Vehicle
        Vehicle[] availableVehiclesArr = availableVehicles.toArray(new Vehicle[0]);
        Vehicle previousAssignedVehicle = null;
        int selectedVehicleNum = Logistics.getValidatedInput("Select a vehicle by number: ", 1, availableVehiclesArr.length) - 1;

        // Loop through available vehicles to find the one previously assigned to the driver
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getDriver().equalsIgnoreCase(this.getName())) { 
                previousAssignedVehicle = vehicle;
                previousAssignedVehicle.setDriver("NA"); // Unassign the driver
            }
        }

        // Assign the new selected vehicle
        currentAssignedVehicle = availableVehiclesArr[selectedVehicleNum];
        currentAssignedVehicle.setDriver(this.getName()); // Assign the driver to the new vehicle

        // Update the CSV with the new vehicle assignment
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        CSVParser.updateCSV(currentAssignedVehicle.getVehicleID(), currentAssignedVehicle.getDriver(), 4, currentAssignedVehicle.getVehicleHeader()); // Assigned Vehicle

        // Check if there was a previous assigned vehicle and update its record in the CSV
        if (previousAssignedVehicle != null) {
            CSVParser.updateCSV(previousAssignedVehicle.getVehicleID(), previousAssignedVehicle.getDriver(), 4, previousAssignedVehicle.getVehicleHeader()); // Remove the previous driver's name
        }
    }

    public void deliverPackage() {
        if (currentAssignedVehicle == null) {
            System.out.println("No vehicle assigned yet. Please assign a vehicle first.");
            return;
        } else {
        // Delivering package with the assigned vehicle
        System.out.println("Delivering package with vehicle ID: " + currentAssignedVehicle.getVehicleID() + ", type: " + currentAssignedVehicle.getType());

        // Load shipments for the assigned vehicle 
        Shipment[] shipments = loadShipments();
        // Display and Load Shipments to the Vehicle
        System.out.println("Shipments Stored in the Vehicle: ");
        int i = 0;
        boolean hasPendingShipments = false;

        ArrayList<Shipment> pendingShipments = new ArrayList<Shipment>();
        for (Shipment s : shipments) {
            if (s != null && s.getStatus().equalsIgnoreCase("Pending") && s.getVehicleId() == currentAssignedVehicle.getVehicleID()) {
                System.out.printf("Shipment No. %d | ID: %d | Status: %s\n",(i + 1), s.getShipmentID(), s.getStatus());
                pendingShipments.add(s);
                hasPendingShipments = true; // Mark that we have added a pending shipment
                i++;
            }
        }

        currentAssignedVehicle.setShipments(pendingShipments.toArray(new Shipment[0]));

        // Check if no pending shipments were found
        if (!hasPendingShipments) {
            System.out.println("No pending shipments to deliver.");
            return;
        }
            // Ask User what Shipment to manage
            int selectedShipmentNum = Logistics.getValidatedInput("Select a shipment to manage by number: ", 1, currentAssignedVehicle.getCurrentShipmentCount()) - 1;
            
            // Verify the selected shipment exists
            Shipment selectedShipment = currentAssignedVehicle.getShipments()[selectedShipmentNum];
            if (selectedShipment == null) {
                System.out.println("Invalid selection. Shipment does not exist.");
                return;
            }
            String toDeliver = Logistics.getInput("Set Status of Shipment ID: " + currentAssignedVehicle.getShipments()[selectedShipmentNum].getShipmentID() + " to \"Delivered\"? (Yes/No)");
            if(toDeliver.equalsIgnoreCase("Yes")) {
                selectedShipment.setStatus("Delivered");
                selectedShipment.setVehicleId(0); // Set Vehicle ID to 0 indicating that it’s no longer in the vehicle
                currentAssignedVehicle.removeShipment(selectedShipment); // Remove it from the Vehicle

                CSVParser.setFilePath("CSVFiles/shipments.csv");
                // Set Status to Delivered in Shipment CSV 
                CSVParser.setFilePath("CSVFiles/shipments.csv");
                CSVParser.updateCSV(selectedShipment.getShipmentID(), selectedShipment.getStatus(), 7, selectedShipment.getShipmentHeader());
                CSVParser.updateCSV(selectedShipment.getShipmentID(), String.valueOf(selectedShipment.getVehicleId()), 2, selectedShipment.getShipmentHeader());
                    
                // Save Updates to Vehicle CSV
                CSVParser.setFilePath("CSVFiles/vehicles.csv");
                CSVParser.updateCSV(currentAssignedVehicle.getVehicleID(), String.valueOf(currentAssignedVehicle.getCurrentShipmentCount()), 8, currentAssignedVehicle.getVehicleHeader());
                CSVParser.updateCSV(currentAssignedVehicle.getVehicleID(), String.format("%d",(int)currentAssignedVehicle.getCurrentCapacityKG()), 6, currentAssignedVehicle.getVehicleHeader());

                System.out.println("Set to Delivered Successfully.");
            } 
            return;
        }
        }

    public boolean isDriverAssigned() {
        // Load available vehicles from the CSV file
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        String[][] vehicleData = CSVParser.loadCSVData(CSVParser.getFilePath());
        Vehicle[] vehicles = Vehicle.toVehicle(vehicleData); 

        for (Vehicle vehicle : vehicles) {
            if(vehicle.getDriver().equalsIgnoreCase(this.getName())) { 
                return true;
            }
        }
        return false;
    }

    public Vehicle getAssignedVehicle() {
        // Load available vehicles from the CSV file
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        String[][] vehicleData = CSVParser.loadCSVData(CSVParser.getFilePath());
        Vehicle[] vehicles = Vehicle.toVehicle(vehicleData); 
        Vehicle vehicleAssigned = null;
        for (Vehicle vehicle : vehicles) {
            if(vehicle.getDriver().equalsIgnoreCase(this.getName())) { 
                vehicleAssigned = vehicle;
            }
        }
        return vehicleAssigned;
    }

    public Shipment[] loadShipments() {
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
}

