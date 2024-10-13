import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

class Admin extends Employee {
    public static final String ADMIN_USER = "admin";
    public static final String ADMIN_PASS = "admin123";

    public Admin(String name) {
        super(name);
    }

    public static boolean login(String inUser, String inPass) {
        return ADMIN_USER.equals(inUser) && ADMIN_PASS.equals(inPass);
    }
    // Admin-specific functionalities
    public void addWarehouse() {
        CSVParser.setFilePath("CSVFiles/warehouses.csv");
        String location = Logistics.getInput("Enter warehouse location");
        int maxPackageCount = getValidIntInput("Enter maximum package capacity");
        int maxVehicleCount = getValidIntInput("Enter maximum number of vehicles");
        Warehouse newWarehouse = new Warehouse(CSVParser.getLatestID() + 1, location, maxPackageCount, maxVehicleCount);
        CSVParser.saveEntry(newWarehouse.toCSVFormat(0, 0));
        System.out.println("Warehouse added successfully.");
    }

    public void removeWarehouse() {
        System.out.println("Available Warehouses:");
        CSVParser.setFilePath("CSVFiles/warehouses.csv");
        String[][] warehousesData = CSVParser.loadCSVData(CSVParser.getFilePath());
        Warehouse[] warehouses = Warehouse.toWarehouse(warehousesData);

        if (warehousesData.length == 0) {
            System.out.println("No warehouses available.");
            return;
        }

        displayTable(warehousesData, warehouses[0].getWarehouseHeader());

        int warehouseId = getValidIntInput("Enter Warehouse ID to remove");
        boolean removed = false;

        for (int i = 0; i < warehousesData.length; i++) {
            if (CSVParser.toInt(warehousesData[i][0]) == warehouseId) {
                warehousesData[i] = null;
                removed = true;
                break;
            }
        }

        if (removed) {
            String[][] updatedData = new String[warehousesData.length - 1][];
            int index = 0;
            for (String[] warehouse : warehousesData) {
                if (warehouse != null) {
                    updatedData[index++] = warehouse;
                }
            }
            CSVParser.writeToCSV(updatedData, warehouses[0].getWarehouseHeader(), false);
            System.out.println("Warehouse removed successfully.");
        } else {
            System.out.println("Warehouse not found.");
        }
    }

    public void addVehicles() {
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        String type = Logistics.getInput("Enter vehicle type (Truck/Van)");
        int whId = 0;
        String licensePlate = Logistics.getInput("Enter vehicle license plate");
        String driver = Logistics.getInput("Enter driver name");
        int maxShipmentCount = getValidIntInput("Enter maximum shipment count: ");
        int currentShipmentCount = 0;
        boolean isAvailable = true;

        Vehicle newVehicle;
        if (type.equalsIgnoreCase("Truck")) {
            int maxWarehouseRoutes = getValidIntInput("Enter maximum number of warehouse routes for the truck");
            newVehicle = new Truck(CSVParser.getLatestID() + 1, whId, licensePlate, driver, maxShipmentCount, currentShipmentCount, isAvailable, maxWarehouseRoutes);
        } else if (type.equalsIgnoreCase("Van")) {
            double capacityKG = getValidDoubleInput("Enter maximum capacity (KG): ");
            double currentCapacity = 0;
            newVehicle = new Vehicle(CSVParser.getLatestID() + 1, whId, type, licensePlate, driver, capacityKG, currentCapacity, maxShipmentCount, currentShipmentCount, isAvailable);
        } else {
            System.out.println("Invalid vehicle type. Please enter either 'Truck' or 'Van'.");
            return;
        }

        CSVParser.saveEntry(newVehicle.toCSVFormat());
    }

    public void removeVehicles() {
        System.out.println("Available Vehicles:");
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        String[][] vehiclesData = CSVParser.loadCSVData(CSVParser.getFilePath());
        Vehicle vehicles = Vehicle.toVehicle(vehiclesData, 1);

        if (vehiclesData.length == 0) {
            System.out.println("No vehicles available.");
            return;
        }

        displayTable(vehiclesData, vehicles.getVehicleHeader());

        int vehicleId = getValidIntInput("Enter Vehicle ID to remove");
        boolean removed = false;

        for (int i = 0; i < vehiclesData.length; i++) {
            if (CSVParser.toInt(vehiclesData[i][0]) == vehicleId) {
                vehiclesData[i] = null;
                removed = true;
                break;
            }
        }

        if (removed) {
            String[][] updatedData = new String[vehiclesData.length - 1][];
            int index = 0;
            for (String[] vehicle : vehiclesData) {
                if (vehicle != null) {
                    updatedData[index++] = vehicle;
                }
            }
            CSVParser.writeToCSV(updatedData, vehicles.getVehicleHeader(), false);
            System.out.println("Vehicle removed successfully.");
        } else {
            System.out.println("Vehicle not found.");
        }
    }

    public void generateReports(String fileReportLocation) {
        StringBuilder report = new StringBuilder();
        report.append("DeliverIT System Report\n");
        report.append("========================\n\n");

        // Add warehouse information
        report.append("Warehouses:\n");
        CSVParser.setFilePath("CSVFiles/warehouses.csv");
        String[][] warehousesData = CSVParser.loadCSVData(CSVParser.getFilePath());
        for (String[] warehouse : warehousesData) {
            report.append(String.format("ID: %s, Stock ID: %s, Vehicle ID: %s, Location: %s, Max Shipments: %s, Current Shipments: %s, Current Vehicles: %s\n",
                    warehouse[0], warehouse[1], warehouse[2], warehouse[3], warehouse[4], warehouse[5], warehouse[6]));
        }
        report.append("\n");

        // Add package information
        report.append("Packages:\n");
        CSVParser.setFilePath("CSVFiles/packages.csv");
        String[][] packagesData = CSVParser.loadCSVData(CSVParser.getFilePath());
        for (String[] pkg : packagesData) {
            report.append(String.format("ID: %s, Customer ID: %s, Receiver Address: %s, Created: %s, Weight: %s kg, Dimensions: %s x %s x %s cm\n",
                    pkg[0], pkg[1], pkg[2], pkg[3], pkg[4], pkg[5], pkg[6], pkg[7]));
        }
        report.append("\n");

        // Add vehicle information
        report.append("Vehicles:\n");
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        String[][] vehiclesData = CSVParser.loadCSVData(CSVParser.getFilePath());
        for (String[] vehicle : vehiclesData) {
            report.append(String.format("ID: %s, Warehouse ID: %s, Type: %s, License Plate: %s, Driver: %s, Max Capacity: %s KG, Current Capacity: %s KG, Max Shipments: %s, Current Shipments: %s, Available: %s\n",
                    vehicle[0], vehicle[1], vehicle[2], vehicle[3], vehicle[4], vehicle[5], vehicle[6], vehicle[7], vehicle[8], vehicle[9]));
        }
        report.append("\n");

        // Add shipment information
        report.append("Shipments:\n");
        CSVParser.setFilePath("CSVFiles/shipments.csv");
        String[][] shipmentsData = CSVParser.loadCSVData(CSVParser.getFilePath());
        for (String[] shipment : shipmentsData) {
            report.append(String.format("ID: %s, Package ID: %s, Vehicle ID: %s, Warehouse ID: %s, Destination: %s, Cost: %s, Confirmed: %s, Status: %s, Ship Date: %s, ETA: %s\n",
                    shipment[0], shipment[1], shipment[2], shipment[3], shipment[4], shipment[5], shipment[6], shipment[7], shipment[8], shipment[9]));
        }

        // Write report to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileReportLocation))) {
            writer.write(report.toString());
            System.out.println("Report generated successfully: " + fileReportLocation);
        } catch (IOException e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }

    // Add admin-specific implementation
    @Override
    public void showMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("---------------------------");
            System.out.println("Admin Menu");
            System.out.println("1. Add Warehouse");
            System.out.println("2. Remove Warehouse");
            System.out.println("3. Add Vehicle");
            System.out.println("4. Remove Vehicle");
            System.out.println("5. Generate Reports");
            System.out.println("6. Exit");
            System.out.println("---------------------------");

            int choice = Logistics.getValidatedInput("Enter your choice", 1, 6);

            switch (choice) {
                case 1:
                    addWarehouse();
                    break;
                case 2:
                    removeWarehouse();
                    break;
                case 3:
                    addVehicles();
                    break;
                case 4:
                    removeVehicles();
                    break;
                case 5:
                    String reportLocation = Logistics.getInput("Enter the file path for the report");
                    generateReports(reportLocation);
                    break;
                case 6:
                    exit = true;
                    System.out.println("Exiting Admin menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
    // Helper function for validating integer input
    private int getValidIntInput(String prompt) {
        int result;
        do {
            String inputStr = Logistics.getInput(prompt);
            try {
                result = CSVParser.toInt(inputStr);
                if (result <= 0) {
                    System.out.println("Please enter a valid positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                result = -1;
            }
        } while (result <= 0);
        return result;
    }

    // Helper function for validating double input
    private double getValidDoubleInput(String prompt) {
        double result;
        do {
            String inputStr = Logistics.getInput(prompt);
            try {
                result = CSVParser.toDouble(inputStr);
                if (result <= 0) {
                    System.out.println("Please enter a valid positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                result = -1;
            }
        } while (result <= 0);
        return result;
    }
    private void displayTable(String[][] data, String[] headers) {
        // Calculate column widths
        int[] columnWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length();
            for (String[] row : data) {
                if (row[i] != null && row[i].length() > columnWidths[i]) {
                    columnWidths[i] = row[i].length();
                }
            }
        }

        // Print header
        printTableRow(headers, columnWidths);
        printSeparator(columnWidths);

        // Print data
        for (String[] row : data) {
            printTableRow(row, columnWidths);
        }
    }
    private void printTableRow(String[] row, int[] columnWidths) {
        for (int i = 0; i < row.length; i++) {
            System.out.printf("| %-" + columnWidths[i] + "s ", row[i]);
        }
        System.out.println("|");
    }
    private void printSeparator(int[] columnWidths) {
        for (int width : columnWidths) {
            System.out.print("+-" + "-".repeat(width) + "-");
        }
        System.out.println("+");
    }
}



