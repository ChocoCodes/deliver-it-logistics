import java.util.ArrayList;

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
        Vehicle[] vehicles = Vehicle.toVehicle(vehicleData); 

        // DB
        System.out.println("DB Vehicle ID 2 Availability should be true based on csv");
        System.out.println(vehicles[1].toString());
        System.out.println("___________________________________\n");
        // Display available vehicles
        System.out.println("Available Vehicles:");
        int i = 0;
        ArrayList<Vehicle> availableVehicles = new ArrayList<Vehicle>();
        for (Vehicle v : vehicles) {
            if (v.getDriver() != null && !v.getDriver().isEmpty() && v.isAvailable()) { // Vehicles without a driver and is not used (available)
                System.out.println((i + 1) + ": " + v.toString());
                availableVehicles.add(v);
                i++;
            }
        }
        // Select Vehicle to Assign to Driver
        Vehicle[] availabVehiclesArr = availableVehicles.toArray(new Vehicle[0]);
        int selectedVehicleNum = Logistics.getValidatedInput("Select a vehicle by ID number: ", 1, vehicles.length) - 1;
        assignedVan = availabVehiclesArr[selectedVehicleNum];

        // Update the CSV with the driver assigned to the selected vehicle
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        CSVParser.updateCSV(assignedVan.vehicleID, assignedVan.getDriver(), 5, assignedVan.getVehicleHeader()); 
    }

    public void deliverPackage() {
        if (assignedVan == null) {
            System.out.println("No vehicle assigned yet. Please assign a vehicle first.");
            return;
        } else {
            System.out.println("Delivering package with vehicle ID : " + assignedVan.getVehicleID() + ", type: " + assignedVan.getType());
            // Load all shipments from the CSV file
            CSVParser.setFilePath("CSVFiles/shipments.csv");
            String[][] shipmentData = CSVParser.loadCSVData(CSVParser.getFilePath());
            Shipment[] shipments = new Shipment[shipmentData.length];

            // Display and Load Shipments to the Vehicle
            System.out.println("Shipments Stored in the Vehicle: ");
                int i = 0;
                for (Shipment s : shipments) {
                    if(s.getStatus().equalsIgnoreCase("Pending") && s != null && s.getVehicleId() == assignedVan.getVehicleID()) {
                        System.out.println((i + 1) + ": " + s.toString());
                        assignedVan.addShipment(s);
                        i++;
                    }
                }
                if (assignedVan.getShipments() != null && assignedVan.getCurrentShipmentCount() < 1) {
                System.out.println("No pending shipments to deliver.");
                return;
                }
            }
            
            // Ask User what Shipment to manage
            int selectedShipmentNum = Logistics.getValidatedInput("Select a shipment to manage by number: ", 1, assignedVan.getCurrentShipmentCount()) - 1;

            String toDeliver = Logistics.getInput("Set Status of Shipment ID: " + assignedVan.getShipments()[selectedShipmentNum] + " to \"Delivered\"? (Yes/No)");
            if(toDeliver.equalsIgnoreCase("Yes")) {
                assignedVan.getShipments()[selectedShipmentNum].setStatus("Delivered");
                assignedVan.getShipments()[selectedShipmentNum].setVehicleId(0); // Set Vehicle ID to 0 indicating that its not in the vehicle anymore
                assignedVan.removeShipment(assignedVan.getShipments()[selectedShipmentNum]); // Remove it from the Vehicle

                CSVParser.setFilePath("CSVFiles/shipments.csv");
                // Set Status to Delivered in Shipment CSV
                CSVParser.updateCSV(
                    assignedVan.getShipments()[selectedShipmentNum].getShipmentID(), 
                    assignedVan.getShipments()[selectedShipmentNum].getStatus(), 
                    8, 
                    assignedVan.getShipments()[selectedShipmentNum].getShipmentHeader()
                    );
                    
                // Save Updates to Shipment CSV
                CSVParser.updateCSV(
                    assignedVan.getShipments()[selectedShipmentNum].getShipmentID(), 
                    String.valueOf(assignedVan.getShipments()[selectedShipmentNum].getVehicleId()), 
                    3, 
                    assignedVan.getShipments()[selectedShipmentNum].getShipmentHeader()
                    );

                // Save Updates to Vehicle CSV
                CSVParser.setFilePath("CSVFiles/vehicles.csv");
                CSVParser.updateCSV(assignedVan.getVehicleID(), String.valueOf(assignedVan.getCurrentShipmentCount()), 9, assignedVan.getVehicleHeader());
                CSVParser.updateCSV(assignedVan.getVehicleID(), String.valueOf(assignedVan.getCurrentCapacityKG()), 7, assignedVan.getVehicleHeader());

                System.out.println("Set to Delivered Successfully.");
            } 
            return;
        }

        // DB
        public static void main(String[] args) {
            Driver driver = new Driver(null, null, null);
            Logistics lg = new Logistics();
            driver.showMenu();
        }
    }

