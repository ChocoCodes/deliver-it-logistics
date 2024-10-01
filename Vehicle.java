public class Vehicle {
    protected int vehicleID;
    protected String type;
    protected String licensePlate;
    protected String driver;
    protected double capacity;
    protected boolean isAvailable;

    // Vehicle Constructor
    public Vehicle(int vehicleID, String type, String licensePlate, String driver, double capacity, boolean isAvailable) {
        this.vehicleID = vehicleID;
        this.type = type;
        this.licensePlate = licensePlate;
        this.driver = driver;
        this.capacity = capacity;
        this.isAvailable = isAvailable;
    }

    // Getters
    public String getType() {
        return type;
    }
    public boolean isAvailable() {
        return isAvailable;
    }
    public double getCapacity() {
        return capacity;
    }
    public int getVehicleID() {
        return vehicleID;
    }
    public String getLicensePlate() {
        return licensePlate;
    }
    public String getDriver() {
        return driver;
    }

    // Setters
    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    public void setDriver(String driver) {
        this.driver = driver;
    }

    // Display Information
    public void displayVehicleInformation() {
        System.out.println("=========================================");
        System.out.println("Vehicle ID: " + getVehicleID());
        System.out.println("Vehicle Type: " + getType());
        System.out.println("License Plate: " + getLicensePlate());
        System.out.println("Vehicle Driver: " + getDriver());
        System.out.printf("Capacity: %.2f\n", getCapacity());
        System.out.println("Available: " + isAvailable());
        System.out.println("=========================================");
    }
}
