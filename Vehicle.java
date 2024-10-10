public class Vehicle {
    protected int vehicleID;
    protected int whId;
    protected String type;
    protected String licensePlate;
    protected String driver;
    protected double capacityKG;
    protected double currentCapacityKG;
    protected int maxShipmentCount;
    protected int currentShipmentCount;
    protected boolean isAvailable;
    protected Shipment[] shipments; 

    // Changed Package[] packages -> Shipment[] shipments because the Driver Role Needs Vehicle to have a Shipment 
    // Packages method can still be accessed through shipment class

    // Vehicle Constructor for CSV File
    public Vehicle(int vehicleID, int whId, String type, String licensePlate, String driver, double capacityKG, double currentCapacityKG, int maxShipmentCount, boolean isAvailable) {
        this.vehicleID = vehicleID;
        this.type = type;
        this.licensePlate = licensePlate;
        this.driver = driver;
        this.capacityKG = capacityKG;
        this.isAvailable = isAvailable;
        this.shipments = null;
    }

    // Getters
    public String getType() { return this.type; }
    public boolean isAvailable() { return this.isAvailable; }
    public double getCapacity() { return this.capacityKG; }
    public int getVehicleID() { return this.vehicleID; }
    public String getLicensePlate() { return this.licensePlate; }
    public String getDriver() { return this.driver; }
    public Shipment[] getShipments() { return this.shipments; }
    public double getCurrentCapacityKG() { return this.currentCapacityKG; }
    public int getMaxShipmentCount() { return this.maxShipmentCount; }
    public int getCurrentShipmentCount() { return this.currentShipmentCount; }
    
    // Setters
    public void setAvailability(boolean isAvailable) { this.isAvailable = isAvailable; }
    public void setDriver(String driver) { this.driver = driver; }

    // Display Info for Vehicles only
    @Override
    public String toString() {
        return String.format(
            "Vehicle ID: %d\nType: %s\nLicense Plate: %s\nDriver: %s\nCapacity: %.2f KG\nCurrent Load: %.2f KG\nMax Shipments: %d\nCurrent Shipments: %d\nAvailable: %b", 
            getVehicleID(),
            getType(),
            getLicensePlate(),
            getDriver(),
            getCapacity(),
            getCurrentCapacityKG(),
            getMaxShipmentCount(),
            getCurrentShipmentCount(),
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

    public boolean addShipment(Shipment shipment) {
        if (getAvailableCapacity() < capacityKG && getCurrentShipmentCount() < maxShipmentCount) {
            for (int i = 0; i < shipments.length; i++) {
                if (shipments[i] == null) { 
                    if (!(currentCapacityKG + shipments[i].getPackage().getTotalItemWeight(shipments[i].getPackage().getContents()) > capacityKG)) {
                        shipments[i] = shipment;
                        currentCapacityKG += shipment.getPackage().getTotalItemWeight(shipment.getPackage().getContents());
                        currentShipmentCount++;
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    // Remove ONE Shipment from the vehicle if it is delivered
    public boolean removeShipment(Shipment shipment) {
        for (int i = 0; i < shipments.length; i++) {
            if (shipments[i] != null && shipments[i].equals(shipment)) {
                currentCapacityKG -= shipment.getPackage().getTotalItemWeight(shipment.getPackage().getContents());
                currentShipmentCount--;
                shipments[i] = null;
                return true;
            }
        }
        return false;
    }

    // Follow header in formatting - id,whId,type,licensePlate,driver,cap_max,curr_cap,max_ship,curr_ship,avail
    public static Vehicle[] toVehicle(String[][] raw) {
       Vehicle[] vehicles = new Vehicle[raw.length];
        for (int i = 0; i < raw.length; i++) {
            vehicles[i] = new Vehicle(
                CSVParser.toInt(raw[i][0]),
                CSVParser.toInt(raw[i][1]),
                raw[i][2],
                raw[i][3],
                raw[i][4],
                detMaxCap(raw[i][2]),
                Double.parseDouble(raw[i][6]),
                Integer.parseInt(raw[i][7]),
                Boolean.parseBoolean(raw[i][8])
            );
        }
        return vehicles;
     }

    private static int detMaxCap(String type) {
        return type.toLowerCase().equals("van") ? 500 : 1000;
    }
    public static Vehicle toVehicle(String[][] raw, int idx) { 
        return new Vehicle(
            CSVParser.toInt(raw[idx][0]),
            CSVParser.toInt(raw[idx][1]),
            raw[idx][2],
            raw[idx][3],
            raw[idx][4],
            Double.parseDouble(raw[idx][5]),
            Double.parseDouble(raw[idx][6]),
            Integer.parseInt(raw[idx][7]),
            Boolean.parseBoolean(raw[idx][8])
        ); 
    }
}

class Van extends Vehicle {

    public Van(int vehicleID, String licensePlate, String driver, boolean isAvailable) {
        // Max capacity is only 1200kg and only allows 75 Shipment
        super(vehicleID, "Van", licensePlate, driver, 1200, 75, isAvailable);
    }

    // Override the toString method for Van specific display
    @Override
    public String toString() {
        return String.format(
            "Van ID: %d\nLicense Plate: %s\nDriver: %s\nCapacity: %.2f KG\nCurrent Load: %.2f KG\nMax Shipments: %d\nCurrent Shipments: %d\nAvailable: %b",
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
