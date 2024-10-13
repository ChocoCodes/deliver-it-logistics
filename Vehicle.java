public class Vehicle {
    protected final String[] VEHICLES_H = {"vID", "whId", "type", "licensePlate", "driver", "cap_max", "cap_curr", "max_ship", "curr_ship", "avail"};
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
    public Vehicle(int vehicleID, int whId, String type, String licensePlate, String driver, double capacityKG, double currentCapacityKG, int maxShipmentCount, int currentShipmentCount, boolean isAvailable) {
        this.vehicleID = vehicleID;
        this.whId = whId;
        this.type = type;
        this.licensePlate = licensePlate;
        this.driver = driver;
        this.capacityKG = capacityKG;
        this.currentCapacityKG = currentCapacityKG;
        this.maxShipmentCount = maxShipmentCount;
        this.currentShipmentCount = currentShipmentCount;
        this.isAvailable = isAvailable;
        this.shipments = new Shipment[maxShipmentCount];
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
    public int getWarehouseId() {return this.whId; }
    public String[] getVehicleHeader() { return this.VEHICLES_H; }
    
    // Setters
    public void setAvailability(boolean isAvailable) { this.isAvailable = isAvailable; }
    public void setDriver(String driver) { this.driver = driver; }
    public void setShipments(Shipment[] shipments) { this.shipments = shipments; }
    public void setWarehouseId(int id) {this.whId = id; }
    // Display Info for Vehicles only
    @Override
    public String toString() {
        return String.format(
            "Vehicle ID: %d\nWarehouse ID: %d\nType: %s\nLicense Plate: %s\nDriver: %s\nCapacity: %.2f KG\nCurrent Load: %.2f KG\nMax Shipments: %d\nCurrent Shipments: %d\nAvailable: %b", 
            getVehicleID(),
            getWarehouseId(),
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
                String.valueOf(getVehicleID()),
                String.valueOf(getWarehouseId()),
                getType(),
                getLicensePlate(),
                getDriver(),
                String.valueOf(getCapacity()),
                String.valueOf(getCurrentCapacityKG()),
                String.valueOf(getMaxShipmentCount()),
                String.valueOf(getCurrentShipmentCount()),
                String.valueOf(isAvailable())
        };
    }

    public double getAvailableCapacity() {
        return capacityKG - currentCapacityKG;
    }

    public boolean addShipment(Shipment shipment) {
        if (shipment == null) {
            return false;
        }

        if (this.shipments == null) {
            this.shipments = new Shipment[getMaxShipmentCount()];
        }

        double shipmentWeight = shipment.getPackage().getDimensionalWeight();
        if (currentCapacityKG + shipmentWeight > capacityKG || currentShipmentCount >= maxShipmentCount) {
            return false;
        }
    
        for (int i = 0; i < shipments.length; i++) {
            if (shipments[i] == null) {
                shipments[i] = shipment;
                currentCapacityKG += shipmentWeight;
                currentShipmentCount++;  
                return true;
            }
        }
        return false; 
    }
    
    public boolean removeShipment(Shipment shipment) {
        if (shipment == null) {
            return false;
        }
    
        for (int i = 0; i < shipments.length; i++) {
            if (shipments[i] != null && shipments[i].equals(shipment)) {
                // Decrease current weight and shipment count
                currentCapacityKG -= shipments[i].getPackage().getDimensionalWeight();
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
                Integer.parseInt(raw[i][8]),
                Boolean.parseBoolean(raw[i][9])
            );
        }
        return vehicles;
     }

    private static int detMaxCap(String type) {
        return type.toLowerCase().equals("van") ? 500 : 1000;
    }
    // id,whId,type,licensePlate,driver,cap_max,curr_cap,max_ship,curr_ship,avail
    // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9
    // int, int string, string, string, double, double, int, int, boolean
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
            Integer.parseInt(raw[idx][8]),
            Boolean.parseBoolean(raw[idx][9])
        ); 
    }
}
