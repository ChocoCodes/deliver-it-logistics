import java.util.InputMismatchException;

public class Driver extends Employee {
    private Vehicle assignedVan;

    public Driver(String name, String username, String password) {
        super(name, username, password);
    }

    @Override
    public void showMenu() {
        boolean done = false;

        while (!done) {
            System.out.println("---------------------------------");
            System.out.println("1. Assign Vehicle");
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
                        deliverPackage();
                        break;
                    case 3:
                        System.out.println("Exiting the system. Goodbye!");
                        done = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please select 1, 2, or 3.");
                }
        }
    }

    public void assignVehicle() {
        // Load available vehicles from the CSV file
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        String[][] vehicleData = CSVParser.loadCSVData(CSVParser.getFilePath());
        Vehicle[] vehicles = Vehicle.toVehicle(vehicleData); // TODO 
        // Display available vehicles
        System.out.println("Available Vehicles:");
        int i = 0;
        for (Vehicle v : vehicles) {
            if (!(v.getDriver().equals(null) || v.getDriver().isEmpty())) {
                System.out.println((i + 1) + ": " + v.toString());
                i++;
            }
        }

        // Select Vehicle to Assign to Driver
        int selectedVehicle = Logistics.getValidatedInput("Select a vehicle by ID number: ", 0, 0);

        for (Vehicle v : vehicles) {
            if (v.getVehicleID() == selectedVehicle) {
                v.setDriver(this.name);
                assignedVan = v;
                break;
            }
        }

        // Update the CSV with the driver assigned to the selected vehicle
        CSVParser.updateVehicleCSV(assignedVan.vehicleID, assignedVan.getDriver(), 5); // TODO: Change to actual col
    }

    public void deliverPackage() {
        if (assignedVan == null) {
            System.out.println("No vehicle assigned yet. Please assign a vehicle first.");
        } else {
            System.out.println("Delivering package with vehicle ID : " + assignedVan.getVehicleID() + ", type: " + assignedVan.getType());
            loadVehicleShipmentFromCSV(assignedVan.vehicleID);

            // Display Shipments in the Vehicle
            System.out.println("Shipments Stored in the Vehicle: ");
            for (Shipment s : assignedVan.getShipments()) {
                if(s.getStatus().equalsIgnoreCase("Pending") && s != null){
                    System.out.println(s.toString());
                }
            }
            
            // Ask User what Shipment to manage
            int choice = 0;
            while(choice < 1 || choice > assignedVan.getShipments().length) {
                while(true) {
                    try {
                        choice = Integer.parseInt(Logistics.getInput("Enter Shipment ID to Deliver: "));
                        break;
                    } catch(InputMismatchException | NumberFormatException e) {
                        System.out.println("Enter a valid integer.");
                    }
                }
            }

            String toDeliver = Logistics.getInput("Set Status of Shipment ID: " + assignedVan.getShipments()[choice - 1] + " to \"Delivered\"? (Yes/No)");
            if(toDeliver.equalsIgnoreCase("Yes")) {
                assignedVan.getShipments()[choice - 1].setStatus("Delivered");
                CSVParser.setFilePath("CSVFiles/shipments.csv");
                // Set Status to Delivered
                CSVParser.updateShipmentCSV(assignedVan.getShipments()[choice - 1].getShipmentID(), 
                                            assignedVan.getShipments()[choice - 1].getStatus(), 8); // TODO change this to actual col value
                assignedVan.getShipments()[choice - 1] = null;
                System.out.println("Set to Delivered Successfully.");
            } 
            return;
        }
    }

    public void loadVehicleShipmentFromCSV(int vehicleID) {
        // Load all shipments from the CSV file
        CSVParser.setFilePath("CSVFiles/shipments.csv");
        String[][] shipmentData = CSVParser.loadCSVData(CSVParser.getFilePath());
        Shipment[] shipments = Shipment.toShipment(shipmentData);

        // Find shipments with matching vehicleID and assign them to this vehicle
        for (Shipment shipment : shipments) {
            if (shipment.getVehicleId() == vehicleID) {
                // Assign shipment to vehicle
                assignedVan.addShipment(shipment);
            }
        }
    }
}
