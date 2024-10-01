public class Vehicle {
    protected int vehicleID;
    protected String type;
    protected String licensePlate;
    protected String driver;
    protected double capacityKG;
    protected boolean isAvailable;

    // Vehicle Constructor
    public Vehicle(int vehicleID, String type, String licensePlate, String driver, double capacityKG, boolean isAvailable) {
        this(vehicleID, type, licensePlate);
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
    public String getType() {
        return this.type;
    }
    public boolean isAvailable() {
        return this.isAvailable;
    }
    public double getCapacity() {
        return this.capacityKG;
    }
    public int getVehicleID() {
        return this.vehicleID;
    }
    public String getLicensePlate() {
        return this.licensePlate;
    }
    public String getDriver() {
        return this.driver;
    }

    // Setters
    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    public void setDriver(String driver) {
        this.driver = driver;
    }

    // Display Info for Vehicles only
    @Override
    public String toString() {
        return String.format("ID: %d\nType: %s\nLicense Plate: %s\nCapacity: %.2f\nAvailability: %b", 
        getVehicleID(), 
        getType(), 
        getLicensePlate(), 
        getCapacity(),
        isAvailable()
        );
    }
}
