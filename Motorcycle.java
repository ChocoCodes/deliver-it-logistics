public class Motorcycle extends Vehicle {

    public Motorcycle(int vehicleID, String licensePlate, String driver, boolean isAvailable) {
        // Max capacity is only 80kg and only allows 1 package
        super(vehicleID, "Motorcycle", licensePlate, driver, 80, 1, isAvailable);
    }

    // Ensure that the motorcycle can only receive one package at <= 80KG 
    @Override
    public boolean addPackage(Package pkg) {
        if (getCurrentPackageCount() == 0 && pkg.getWeight() <= 80) {
            packages[0] = pkg;
            currentCapacityKG += pkg.getWeight();
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
