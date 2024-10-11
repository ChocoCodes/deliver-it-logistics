public class Truck extends Vehicle {
    private int maxWarehouseRoutes;
    public Truck(int vehicleID, String licensePlate, String driver, boolean isAvailable, int maxWarehouseRoutes) {
        super(vehicleID, "Truck", licensePlate, driver, 1000,100, isAvailable);
        this.maxWarehouseRoutes = maxWarehouseRoutes;
    }

    @Override
    public String toString() {
        return String.format(
                "Truck ID: %d\nLicense Plate: %s\nDriver: %s\nCapacity: %.2f KG\nCurrent Load: %.2f KG\nMax Shipments: %d\nCurrent Shipments: %d\nAvailable: %b",
                getVehicleID(),
                getLicensePlate(),
                getDriver(),
                getCapacity(),
                getCurrentCapacityKG(),
                getMaxShipmentCount(),
                getCurrentShipmentCount(),
                isAvailable()
        );
    }
}
