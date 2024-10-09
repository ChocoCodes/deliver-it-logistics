import java.io.*;
import java.util.*;

public class Admin extends Employee {
    public Admin(String name, String username, String password) {
        super(name, username, password);
    }

    // Admin-specific functionalities
    public void addWarehouse() {
        parser.setFilePath("CSVFiles/warehouses.csv");
        String location = logistics.getInput("Enter warehouse location");
        int maxPackageCount;
        do {
            String maxPackageCountStr = logistics.getInput("Enter maximum package capacity");
            maxPackageCount = parser.toInt(maxPackageCountStr);
        } while (maxPackageCount <= 0);

        int maxVehicleCount;
        do {
            String maxVehicleCountStr = logistics.getInput("Enter maximum number of vehicles");
            maxVehicleCount = parser.toInt(maxVehicleCountStr);
        } while (maxVehicleCount <= 0);

        Warehouse newWarehouse = new Warehouse(parser.getLatestID() + 1, location, maxPackageCount, maxVehicleCount);

        parser.saveEntry(newWarehouse.toCSVFormat());

        System.out.println("Warehouse added successfully.");
    }

    public void removeWarehouse() {
        System.out.println("Available Warehouses:");
        parser.setFilePath("CSVFiles/warehouses.csv");
        String[][] warehousesData = parser.loadCSVData(parser.getFilePath());
        for (String[] warehouse : warehousesData) {
            System.out.println("ID: " + warehouse[0] + ", Location: " + warehouse[1] + ", Package Capacity: " + warehouse[2] + ", Vehicle Capacity: " + warehouse[3]);
        }

        int warehouseId;
        do {
            String idStr = logistics.getInput("Enter Warehouse ID to remove");
            warehouseId = parser.toInt(idStr);
        } while (warehouseId <= 0);

        boolean removed = false;
        for (int i = 0; i < warehousesData.length; i++) {
            if (parser.toInt(warehousesData[i][0]) == warehouseId) {
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
            parser.writeToCSV(updatedData, parser.getWarehouseHeader(), false);
            System.out.println("Warehouse removed successfully.");
        } else {
            System.out.println("Warehouse not found.");
        }
    }

    public void addVehicles() {
        parser.setFilePath("CSVFiles/vehicles.csv");
        String type = logistics.getInput("Enter vehicle type (Truck/Motorcycle)");

        String licensePlate = logistics.getInput("Enter vehicle license plate");
        String driver = logistics.getInput("Enter driver name");
        boolean isAvailable = true;

        Vehicle newVehicle;
        if (type.equalsIgnoreCase("Truck")) {
            int maxWarehouseRoutes;
            do {
                String maxWarehouseRoutesStr = logistics.getInput("Enter maximum number of warehouse routes for the truck");
                maxWarehouseRoutes = parser.toInt(maxWarehouseRoutesStr);
            } while (maxWarehouseRoutes <= 0);

            newVehicle = new Truck(parser.getLatestID() + 1, licensePlate, driver, isAvailable, maxWarehouseRoutes);
        } else if (type.equalsIgnoreCase("Motorcycle")) {
            newVehicle = new Motorcycle(parser.getLatestID() + 1, licensePlate, driver, isAvailable);
        } else {
            System.out.println("Invalid vehicle type. Please enter either 'Truck' or 'Motorcycle'.");
            return;
        }

        parser.saveEntry(newVehicle.toCSVFormat());

        System.out.println("Vehicle added successfully.");
    }

    public void removeVehicles() {
        System.out.println("Available Vehicles:");
        parser.setFilePath("CSVFiles/vehicles.csv");
        String[][] vehiclesData = parser.loadCSVData(parser.getFilePath());
        for (String[] vehicle : vehiclesData) {
            System.out.println("ID: " + vehicle[0] + ", Type: " + vehicle[1] + ", License Plate: " + vehicle[2] + ", Driver: " + vehicle[3] + ", Capacity(KG): " + vehicle[4] + ", Availability: " + vehicle[5]);
        }

        int vehicleId;
        do {
            String idStr = logistics.getInput("Enter Vehicle ID to remove");
            vehicleId = parser.toInt(idStr);
        } while (vehicleId <= 0);

        boolean removed = false;
        for (int i = 0; i < vehiclesData.length; i++) {
            if (parser.toInt(vehiclesData[i][0]) == vehicleId) {
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
            parser.writeToCSV(updatedData, parser.getVehicleHeader(), false);
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
        parser.setFilePath("CSVFiles/warehouses.csv");
        String[][] warehousesData = parser.loadCSVData(parser.getFilePath());
        for (String[] warehouse : warehousesData) {
            report.append(String.format("ID: %s, Location: %s, Max Packages: %s, Max Vehicles: %s\n",
                    warehouse[0], warehouse[1], warehouse[2], warehouse[3]));
        }
        report.append("\n");

        // Add vehicle information
        report.append("Vehicles:\n");
        parser.setFilePath("CSVFiles/vehicles.csv");
        String[][] vehiclesData = parser.loadCSVData(parser.getFilePath());
        for (String[] vehicle : vehiclesData) {
            report.append(String.format("ID: %s, Type: %s, License Plate: %s, Driver: %s, Capacity: %s KG\n",
                    vehicle[0], vehicle[1], vehicle[2], vehicle[3], vehicle[4]));
        }
        report.append("\n");

        // Add package information
        report.append("Packages:\n");
        parser.setFilePath("CSVFiles/packages.csv");
        String[][] packagesData = parser.loadCSVData(parser.getFilePath());
        for (String[] pkg : packagesData) {
            report.append(String.format("ID: %s, Customer ID: %s, Receiver Address: %s, Created: %s\n",
                    pkg[1], pkg[0], pkg[2], pkg[3]));
        }

        // Write report to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileReportLocation))) {
            writer.write(report.toString());
            System.out.println("Report generated successfully: " + fileReportLocation);
        } catch (IOException e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }

    @Override
    public void showMenu() {
        int choice;

        do {
            System.out.println("\nAdmin Menu:");
            System.out.println("1. Encode Order");
            System.out.println("2. Process Shipment");
            System.out.println("3. Sort Package");
            System.out.println("4. Add Warehouse");
            System.out.println("5. Remove Warehouse");
            System.out.println("6. Add Vehicle");
            System.out.println("7. Remove Vehicle");
            System.out.println("8. Generate Report");
            System.out.println("9. Exit");

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
                    addWarehouse();
                    break;
                case 5:
                    removeWarehouse();
                    break;
                case 6:
                    addVehicles();
                    break;
                case 7:
                    removeVehicles();
                    break;
                case 8:
                    String reportLocation = logistics.getInput("Enter report file location");
                    generateReports(reportLocation);
                    break;
                case 9:
                    System.out.println("Exiting Admin Menu...");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        } while (choice != 9);
        System.out.println("Admin session ended.");
    }
}