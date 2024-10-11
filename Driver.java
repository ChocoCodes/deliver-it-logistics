import java.util.InputMismatchException;

public class Driver extends Employee {
    private Vehicle assignedVan;

    public Driver(String name, String username, String password) {
        super(name, username, password);
    }

    @Override
    public void showMenu() {
        int choice = 0;
        boolean done = false;

        while (!done) {
            System.out.println("---------------------------------");
            System.out.println("1. Assign Vehicle");
            System.out.println("2. Deliver Package");
            System.out.println("3. Done");
            System.out.println("---------------------------------");

            // Getting user input and handling exceptions
            try {
                choice = Integer.parseInt(Logistics.getInput("Enter your choice: "));

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
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public void assignVehicle() {
        // Load available vehicles from the CSV file
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        String[][] vehicleData = CSVParser.loadCSVData(CSVParser.getFilePath());
        Vehicle[] vehicles = CSVParser.toVehicle(vehicleData); // TODO 
        // Display available vehicles
        System.out.println("Available Vehicles:");
        for (Vehicle v : vehicles) {
            if (v.getDriver().equals(null) || v.getDriver().isEmpty()) {
                System.out.println(v.toString());
            }
        }

        // Select Vehicle to Assign to Driver
        int selectedVehicleID = 0;
        while(true) {
            try {
                selectedVehicleID = Integer.parseInt(Logistics.getInput("Enter Vehicle ID to Assign: "));
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input should be an integer. Try Again");
            }
        }

        for (Vehicle v : vehicles) {
            if (v.getVehicleID() == selectedVehicleID) {
                v.setDriver(this.name);
                assignedVan = v;
                break;
            }
        }

        // Update the CSV with the driver assigned to the selected vehicle
        // TODO: CSVParser.updateVehicleCSV(assignedVan.vehicleID, assignedVan.getDriver(), 4);
    }

    public void deliverPackage(CSVParser parser) {
        if (assignedVan == null) {
            System.out.println("No vehicle assigned yet. Please assign a vehicle first.");
        } else {
            System.out.println("Delivering package with vehicle ID : " + assignedVan.getVehicleID() + ", type: " + assignedVan.getType());
            loadVehicleShipmentFromCSV(assignedVan.vehicleID);

            // Display Shipments in the Vehicle
            System.out.println("Shipments Stored in the Vehicle: ");
            for (Shipment s : assignedVan.getShipments()) {
                System.out.println(s.toString());
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
                CSVParser.updateShipmentCSV(assignedVan.getShipments()[choice - 1].getShipmentID(), 
                                         assignedVan.getShipments()[choice - 1].getStatus(), 5); // TODO change this to actual col value
                System.out.println("Set to Delivered Successfully.");
            } 
            return;
        }
    }

    // D ko sure if diri ni or sa Vehicle na class
    public void loadVehicleShipmentFromCSV(int vehicleID) {
        // Load all shipments from the CSV file
        CSVParser.setFilePath("CSVFiles/shipments.csv");
        String[][] shipmentData = CSVParser.loadCSVData(CSVParser.getFilePath());
        Shipment[] shipments = CSVParser.toShipment(shipmentData);

        // Find shipments with matching vehicleID and assign them to this vehicle
        for (Shipment shipment : shipments) {
            if (shipment.getVehicleID() == vehicleID) {
                // Assign shipment to vehicle
                assignedVan.addShipment(shipment);
            }
        }
    }
}
