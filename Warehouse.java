public class Warehouse {
    private int wareHouseID;
    private String location;
    private int maxPackageCount; 
    private int currentPackageCount;
    private int maxVehicleCount;
    private int currentVehicleCount;
    private Package[] packages; // pkgID in csv
    private Vehicle[] vehicles; // vID in csv

    // Warehouse Constructor
    public Warehouse(int wareHouseID, String location, int maxPackageCount, int maxVehicleCount) {
        this.wareHouseID = wareHouseID;
        this.location = location;
        this.maxPackageCount = maxPackageCount;
        this.currentPackageCount = 0;
        this.maxVehicleCount = maxVehicleCount;
        this.currentVehicleCount = 0;
        this.packages = new Package[maxPackageCount];
        this.vehicles = new Vehicle[maxVehicleCount]; 
    }

    // Getters
    public int getWarehouseID() { return wareHouseID; }
    public String getLocation() { return location; }
    public int getmaxPackageCount() { return maxPackageCount; }
    public int getCurrentPackageCount() { return currentPackageCount; }
    public int getmaxVehicleCount() { return maxVehicleCount; }
    public int getCurrentVehicleCount() { return currentVehicleCount; }
    public Package[] getPackages() { return packages; }
    public Vehicle[] getVehicles() { return vehicles; }

    // Add a package to the warehouse (e.g., drop off package of a vehicle)
    public boolean addPackage(Package pkg) {
        if (currentPackageCount < maxPackageCount) {
            packages[currentPackageCount] = pkg;
            currentPackageCount++;
            return true;
        }
        return false; 
    }

    // Remove a package from the warehouse (e.g., being loaded to the vehicle)
    public boolean removePackage(Package pkg) {
        for (int i = 0; i < currentPackageCount; i++) {
            if (packages[i] != null && packages[i].equals(pkg)) {
                for (int j = i; j < currentPackageCount - 1; j++) {
                    packages[j] = packages[j + 1];
                }
                packages[currentPackageCount - 1] = null;
                currentPackageCount--;
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
        return maxPackageCount - currentPackageCount;
    }

    // Display info about the warehouse
    @Override
    public String toString() {
        return String.format(
            "Warehouse ID: %d\nLocation: %s\nmaxPackageCount: %d\nCurrent Packages: %d\nMax Vehicles: %d\nCurrent Vehicles: %d\n",
            getWarehouseID(),
            getLocation(),
            getmaxPackageCount(),
            getCurrentPackageCount(),
            getmaxVehicleCount(),
            getCurrentVehicleCount()
        );
    }

    public String[] toCSVFormat() {
        return new String[]{
                String.valueOf(getWarehouseID()),
                location,
                String.valueOf(getmaxPackageCount()),
                String.valueOf(getmaxVehicleCount())
        };
    }
}
