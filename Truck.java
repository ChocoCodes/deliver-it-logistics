public class Truck extends Vehicle {
    private Warehouse[] routes;
    private Warehouse[] routesDone;
    private int currentWarehouseRouteCount;
    private int maxWarehouseRoutes;
    private int droppedOffWarehouseCount;
    public Truck(int vehicleID, String licensePlate, String driver, boolean isAvailable, int maxWarehouseRoutes) {
        super(vehicleID, "Truck", licensePlate, driver, 1000,100, isAvailable);
        this.routes = new Warehouse[maxWarehouseRoutes];
        this.routesDone = new Warehouse[maxWarehouseRoutes];
        this.maxWarehouseRoutes = maxWarehouseRoutes;
        this.droppedOffWarehouseCount = 0;
        this.currentWarehouseRouteCount = 0;
    }

    public int getCurrentWarehouseRouteCount() {
        return currentWarehouseRouteCount;
    }

    public int getDroppedOffWarehouseCount() {
        return droppedOffWarehouseCount;
    }

    public boolean addWarehouse(Warehouse wh) {
        if (getCurrentWarehouseRouteCount() < maxWarehouseRoutes) {
            for (int i = 0; i < routes.length; i++) {
                if (routes[i] == null) {
                    routes[i] = wh;
                    currentWarehouseRouteCount++;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeWarehouse(Warehouse wh) {
        for (int i = 0; i < routes.length; i++) {
            if (routes[i] != null && routes[i].equals(wh)) {
                currentWarehouseRouteCount--;
                routes[i] = null;
                addRemovedWarehouse(routes[i]);
                return true;
            }
        }
        return false;
    }

    public boolean addRemovedWarehouse(Warehouse wh) {
        if (getDroppedOffWarehouseCount() < maxWarehouseRoutes) {
            for (int i = 0; i < routesDone.length; i++) {
                if (routesDone[i] == null) {
                    routesDone[i] = wh;
                    droppedOffWarehouseCount++;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format(
                "Truck ID: %d\nLicense Plate: %s\nDriver: %s\nCapacity: %.2f KG\nCurrent Load: %.2f KG\nMax Packages: %d\nCurrent Packages: %d\nWarehouses to visit: %d\nWarehouses visited: %d\nAvailable: %b",
                getVehicleID(),
                getLicensePlate(),
                getDriver(),
                getCapacity(),
                getCurrentCapacityKG(),
                getMaxPackageCount(),
                getCurrentPackageCount(),
                getCurrentWarehouseRouteCount(),
                getDroppedOffWarehouseCount(),
                isAvailable()
        );
    }
}
