public class Truck extends Vehicle {
    private int maxWarehouseRoutes;
    public Truck(int vehicleID, int whId, String licensePlate, String driver, int maxShipmentCount, int currentShipmentCount, boolean isAvailable, int maxWarehouseRoutes) {
        super(vehicleID, whId,"Truck", licensePlate, driver, 1000,100, maxShipmentCount, currentShipmentCount, isAvailable);
        this.maxWarehouseRoutes = maxWarehouseRoutes;
    }

    public int getMaxWarehouseRoutes() { return this.maxWarehouseRoutes; }
    public void setMaxWarehouseRoutes(int maxWarehouseRoutes) { this.maxWarehouseRoutes = maxWarehouseRoutes; }
    
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
