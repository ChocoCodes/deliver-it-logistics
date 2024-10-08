public class Vehicle {
    protected int vehicleID;
    protected String type;
    protected String licensePlate;
    protected String driver;
    protected double capacityKG;
    protected double currentCapacityKG;
    protected int maxPackageCount;
    protected int currentPackageCount;
    protected boolean isAvailable;
    protected Package[] packages;


    // Vehicle Constructor
    public Vehicle(int vehicleID, String type, String licensePlate, String driver, double capacityKG, int maxPackageCount, boolean isAvailable) {
        this.vehicleID = vehicleID;
        this.type = type;
        this.licensePlate = licensePlate;
        this.driver = driver;
        this.capacityKG = capacityKG;
        this.isAvailable = isAvailable;
    }

    public Vehicle(int vehicleID, String type, String licensePlate) {
        this.vehicleID = vehicleID;
        this.type = type;
        this.licensePlate = licensePlate;
    }

    // Getters
    public String getType() { return this.type; }
    public boolean isAvailable() { return this.isAvailable; }
    public double getCapacity() { return this.capacityKG; }
    public int getVehicleID() { return this.vehicleID; }
    public String getLicensePlate() { return this.licensePlate; }
    public String getDriver() { return this.driver; }
    public Package[] getPackages() { return this.packages; }
    public double getCurrentCapacityKG() { return this.currentCapacityKG; }
    public int getMaxPackageCount() { return this.maxPackageCount; }
    public int getCurrentPackageCount() { return this.currentPackageCount; }
    
    // Setters
    public void setAvailability(boolean isAvailable) { this.isAvailable = isAvailable; }
    public void setDriver(String driver) { this.driver = driver; }

    // Display Info for Vehicles only
    @Override
    public String toString() {
        return String.format(
            "Vehicle ID: %d\nType: %s\nLicense Plate: %s\nDriver: %s\nCapacity: %.2f KG\nCurrent Load: %.2f KG\nMax Packages: %d\nCurrent Packages: %d\nAvailable: %b", 
            getVehicleID(),
            getType(),
            getLicensePlate(),
            getDriver(),
            getCapacity(),
            getCurrentCapacityKG(),
            getMaxPackageCount(),
            getCurrentPackageCount(),
            isAvailable()
        );
    }

    public String[] toCSVFormat() {
        return new String[]{
                String.valueOf(vehicleID),
                type,
                licensePlate,
                driver,
                String.valueOf(capacityKG),
                String.valueOf(isAvailable)
        };
    }

    public double getAvailableCapacity() {
        return capacityKG - currentCapacityKG;
    }

    public boolean addPackage(Package pkg) {
        if (getAvailableCapacity() < capacityKG && getCurrentPackageCount() < maxPackageCount) {
            for (int i = 0; i < packages.length; i++) {
                if (packages[i] == null) { 
                    if (!(currentCapacityKG + pkg.getTotalItemWeight(pkg.getContents()) > capacityKG)) {
                        packages[i] = pkg;
                        currentCapacityKG += pkg.getTotalItemWeight(pkg.getContents());
                        currentPackageCount++;
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    // Remove ONE package from the vehicle if it is delivered
    public boolean removePackage(Package pkg) {
        for (int i = 0; i < packages.length; i++) {
            if (packages[i] != null && packages[i].equals(pkg)) {
                currentCapacityKG -= pkg.getTotalItemWeight(pkg.getContents());
                currentPackageCount--;
                packages[i] = null;
                return true;
            }
        }
        return false;
    }
}

class Motorcycle extends Vehicle {

    public Motorcycle(int vehicleID, String licensePlate, String driver, boolean isAvailable) {
        // Max capacity is only 80kg and only allows 1 package
        super(vehicleID, "Motorcycle", licensePlate, driver, 80, 1, isAvailable);
    }

    // Ensure that the motorcycle can only receive one package at <= 80KG 
    @Override
    public boolean addPackage(Package pkg) {
        if (getCurrentPackageCount() == 0 && pkg.getTotalItemWeight(pkg.getContents()) <= 80) {
            packages[0] = pkg;
            currentCapacityKG += pkg.getTotalItemWeight(pkg.getContents());
            currentPackageCount++;
            return true;
        }
        return false;
    }

    // Override the toString method for Motorcycle specific display
    @Override
    public String toString() {
        return String.format(
            "Motorcycle ID: %d\nLicense Plate: %s\nDriver: %s\nCapacity: %.2f KG\nCurrent Load: %.2f KG\nMax Packages: %d\nCurrent Packages: %d\nAvailable: %b",
            getVehicleID(),
            getLicensePlate(),
            getDriver(),
            getCapacity(),
            getCurrentCapacityKG(),
            getMaxPackageCount(),
            getCurrentPackageCount(),
            isAvailable()
        );
    }
}
