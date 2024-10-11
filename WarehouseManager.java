public class WarehouseManager extends Employee {

    public WarehouseManager(String name, String username, String password) {
        super(name, username, password);
    }

    @Override
    public void showMenu() {
        System.out.println("1. Load Packages by Destination");
        System.out.println("2. Drop Off Vehicle Load to Warehouse");
        System.out.println("3. Manage Vehicles");
        // Other options for WarehouseManager... NOT YET FINISHED, MAY ADD SOME MORE
    }

    public void sortPackages() {
        // Logic for sorting packages by destination and Add Package to Vehicles / Remove from Warehouse
        System.out.println("Sorting packages by destination...");
    }

    public void moveVehicles() {
        // Logic for vehicle movement between warehouses
        System.out.println("Moving vehicles to destination...");
    }

    public void vehicleArrival() {
        // Logic for vehicle arriving in the warehouse
        System.out.println("Vehicle arriving to warehouse ");
    }

    public void addVehicleToWarehouse() {
        // Logic for adding vehicles to warehouse
        System.out.println("Adding vehicles to warehouse...");
    }
}