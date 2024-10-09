import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Admin extends Employee {
    public Admin(String name, String username, String password) {
        super(name, username, password);
    }

    // Admin-specific functionalities
    public void addWarehouse() {
        String location = Logistics.getInput("Enter warehouse location");
        int maxPackageCount;
        do {
            String maxPackageCountStr = Logistics.getInput("Enter maximum package capacity");
            maxPackageCount = CSVParser.toInt(maxPackageCountStr);
        } while (maxPackageCount <= 0);

        int maxVehicleCount;
        do {
            String maxVehicleCountStr = Logistics.getInput("Enter maximum number of vehicles");
            maxVehicleCount = CSVParser.toInt(maxVehicleCountStr);
        } while (maxVehicleCount <= 0);

        Warehouse newWarehouse = new Warehouse(CSVParser.getLatestID() + 1, location, maxPackageCount, maxVehicleCount);

        CSVParser.setFilePath("CSVFiles/warehouses.csv");
        CSVParser.saveEntry(newWarehouse.toCSVFormat());

        System.out.println("Warehouse added successfully.");
    }

    public void removeWarehouse() {
        System.out.println("Available Warehouses:");
        CSVParser.setFilePath("warehouses.csv");
        String[][] warehousesData = CSVParser.loadCSVData(CSVParser.getFilePath());
        for (String[] warehouse : warehousesData) {
            System.out.println("ID: " + warehouse[0] + ", Location: " + warehouse[1] + ", Package Capacity: " + warehouse[2] + ", Vehicle Capacity: " + warehouse[3]);
        }

        int warehouseId;
        do {
            String idStr = Logistics.getInput("Enter Warehouse ID to remove");
            warehouseId = CSVParser.toInt(idStr);
        } while (warehouseId <= 0);

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
            CSVParser.writeToCSV(updatedData, CSVParser.getWarehouseHeader(), false);
            System.out.println("Warehouse removed successfully.");
        } else {
            System.out.println("Warehouse not found.");
        }
    }

    public void addVehicles() {
        String type = Logistics.getInput("Enter vehicle type (Truck/Motorcycle)");

        String licensePlate = Logistics.getInput("Enter vehicle license plate");
        String driver = Logistics.getInput("Enter driver name");
        boolean isAvailable = true;

        Vehicle newVehicle;
        if (type.equalsIgnoreCase("Truck")) {
            int maxWarehouseRoutes;
            do {
                String maxWarehouseRoutesStr = Logistics.getInput("Enter maximum number of warehouse routes for the truck");
                maxWarehouseRoutes = CSVParser.toInt(maxWarehouseRoutesStr);
            } while (maxWarehouseRoutes <= 0);

            newVehicle = new Truck(CSVParser.getLatestID() + 1, licensePlate, driver, isAvailable, maxWarehouseRoutes);
        } else if (type.equalsIgnoreCase("Van")) {
            newVehicle = new Van(CSVParser.getLatestID() + 1, licensePlate, driver, isAvailable);
        } else {
            System.out.println("Invalid vehicle type. Please enter either 'Truck' or 'Motorcycle'.");
            return;
        }

        CSVParser.setFilePath("vehicles.csv");
        CSVParser.saveEntry(newVehicle.toCSVFormat());
    }

    public void removeVehicles() {
        System.out.println("Available Vehicles:");
        CSVParser.setFilePath("vehicles.csv");
        String[][] vehiclesData = CSVParser.loadCSVData(CSVParser.getFilePath());
        for (String[] vehicle : vehiclesData) {
            System.out.println("ID: " + vehicle[0] + ", Type: " + vehicle[1] + ", License Plate: " + vehicle[2] + ", Driver: " + vehicle[3] + ", Capacity(KG): " + vehicle[4] + ", Availability: " + vehicle[5]);
        }

        int vehicleId;
        do {
            String idStr = Logistics.getInput("Enter Vehicle ID to remove");
            vehicleId = CSVParser.toInt(idStr);
        } while (vehicleId <= 0);

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
            CSVParser.writeToCSV(updatedData, CSVParser.getVehicleHeader(), false);
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
        CSVParser.setFilePath("warehouses.csv");
        String[][] warehousesData = CSVParser.loadCSVData(CSVParser.getFilePath());
        for (String[] warehouse : warehousesData) {
            report.append(String.format("ID: %s, Location: %s, Max Packages: %s, Max Vehicles: %s\n",
                    warehouse[0], warehouse[1], warehouse[2], warehouse[3]));
        }
        report.append("\n");

        // Add vehicle information
        report.append("Vehicles:\n");
        CSVParser.setFilePath("CSVFiles/vehicles.csv");
        String[][] vehiclesData = CSVParser.loadCSVData(CSVParser.getFilePath());
        for (String[] vehicle : vehiclesData) {
            report.append(String.format("ID: %s, Type: %s, License Plate: %s, Driver: %s, Capacity: %s KG\n",
                    vehicle[0], vehicle[1], vehicle[2], vehicle[3], vehicle[4]));
        }
        report.append("\n");

        // Add package information
        report.append("Packages:\n");
        CSVParser.setFilePath("CSVFiles/packages.csv");
        String[][] packagesData = CSVParser.loadCSVData(CSVParser.getFilePath());
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

    // Add admin-specific implementation
    @Override
    public void showMenu() {}
}