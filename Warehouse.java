public class Warehouse {
    private int wareHouseID;
    private String location;
    private int maxShipCount; 
    private int currShipCount;
    private int currentVehicleCount;
    private Package[] packages; // pkgID in csv
    private Vehicle[] vehicles; // vID in csv

    // Warehouse Constructor
    public Warehouse(int wareHouseID, String location, int maxShipCount, int maxVehicleCount) {
        this.wareHouseID = wareHouseID;
        this.location = location;
        this.maxShipCount = maxShipCount;
        this.currShipCount = 0;
        this.currentVehicleCount = 0;
        this.packages = new Package[maxShipCount];
        this.vehicles = new Vehicle[maxVehicleCount]; 
    }

    // Getters
    public int getWarehouseID() { return wareHouseID; }
    public String getLocation() { return location; }
    public int getmaxShipCount() { return maxShipCount; }
    public int getcurrShipCount() { return currShipCount; }
    public int getCurrentVehicleCount() { return currentVehicleCount; }
    public Package[] getPackages() { return packages; }
    public Vehicle[] getVehicles() { return vehicles; }

    // Add a package to the warehouse (e.g., drop off package of a vehicle)
    public boolean addPackage(Package pkg) {
        if (currShipCount < maxShipCount) {
            packages[currShipCount] = pkg;
            currShipCount++;
            return true;
        }
        return false; 
    }

    // Remove a package from the warehouse (e.g., being loaded to the vehicle)
    public boolean removePackage(Package pkg) {
        for (int i = 0; i < currShipCount; i++) {
            if (packages[i] != null && packages[i].equals(pkg)) {
                for (int j = i; j < currShipCount - 1; j++) {
                    packages[j] = packages[j + 1];
                }
                packages[currShipCount - 1] = null;
                currShipCount--;
                return true; 
            }
        }
        return false; 
    }

    // Add a vehicle to the warehouse (e.g., a vehicle came to the warehouse for loading or drop off)
    public boolean addVehicle(Vehicle vehicle) {
        if (currentVehicleCount < maxVehicleCount) {
            vehicles[currentVehicleCount] = vehicle;
            currentVehicleCount++;
            return true;
        }
        return false;
    }

    // Remove a vehicle from the warehouse (e.g., a vehicle left for delivery to another warehouse location)
    public boolean removeVehicle(Vehicle vehicle) {
        for (int i = 0; i < currentVehicleCount; i++) {
            if (vehicles[i] != null && vehicles[i].equals(vehicle)) {
                for (int j = i; j < currentVehicleCount - 1; j++) {
                    vehicles[j] = vehicles[j + 1];
                }
                vehicles[currentVehicleCount - 1] = null;
                currentVehicleCount--;
                return true; 
            }
        }
        return false; 
    }

    // Check available space for packages
    public int getAvailableSpace() {
        return maxShipCount - currShipCount;
    }

    // Display info about the warehouse
    @Override
    public String toString() {
        return String.format(
            "Warehouse ID: %d\nLocation: %s\nmaxShipCount: %d\nCurrent Packages: %d\nCurrent Vehicles: %d\n",
            getWarehouseID(),
            getLocation(),
            getmaxShipCount(),
            getcurrShipCount(),
            getCurrentVehicleCount()
        );
    }
    // Revise
    public String[] toCSVFormat() {
        return new String[]{
                String.valueOf(getWarehouseID()),
                location,
                String.valueOf(getmaxShipCount()),
                String.valueOf(getmaxShipCount())
        };
    }
}
