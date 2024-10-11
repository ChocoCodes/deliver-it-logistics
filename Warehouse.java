public class Warehouse {
    private final String[] WAREHOUSE_H = {"wID", "location", "package_capacity", "vehicle_capacity"};
    private int wareHouseID;
    private String location;
    private int maxShipCount; 
    private int currShipCount;
    private int currVehicleCtr;
    private Shipment[] shipments; // ShipmentID in csv
    private Vehicle[] vehicles; // vID in csv

    // Warehouse Constructor
    public Warehouse(int wareHouseID, String location, int maxShipCount, int maxVehicleCount) {
        this.wareHouseID = wareHouseID;
        this.location = location;
        this.maxShipCount = maxShipCount;
        this.currShipCount = 0;
        this.currVehicleCtr = 0;
        this.shipments = new Shipment[maxShipCount];
        this.vehicles = new Vehicle[maxVehicleCount]; 
    }
    public Warehouse(int wareHouseID, String location, int maxShipCount, int currShipCount, int currVehicleCtr) {
        this.wareHouseID = wareHouseID;
        this.location = location;
        this.maxShipCount = maxShipCount;
        this.currShipCount = currShipCount;
        this.currVehicleCtr = currVehicleCtr;
    }
    // Getters
    public int getWarehouseID() { return this.wareHouseID; }
    public String getLocation() { return this.location; }
    public int getmaxShipCount() { return this.maxShipCount; }
    public int getcurrShipCount() { return this.currShipCount; }
    public int getcurrVehicleCtr() { return this.currVehicleCtr; }
    public Shipment[] getshipments() { return this.shipments; }
    public Vehicle[] getVehicles() { return this.vehicles; }
    public String[] getWarehouseHeader() { return this.WAREHOUSE_H; }

    public static Warehouse[] toWarehouse(String[][] raw) {
        Warehouse[] wh = new Warehouse[raw.length];
        for(int i = 0; i < raw.length; i++) {
            wh[i] = new Warehouse(
                CSVParser.toInt(raw[i][0]), 
                raw[i][3], 
                CSVParser.toInt(raw[i][4]), 
                CSVParser.toInt(raw[i][5]), 
                CSVParser.toInt(raw[i][6])
            );
        }
        return wh;
    }


    // Add a Shipment to the warehouse (e.g., drop off Shipment of a vehicle)
    public boolean addShipment(Shipment pkg) {
        if (currShipCount < maxShipCount) {
            shipments[currShipCount] = pkg;
            currShipCount++;
            return true;
        }
        return false; 
    }

    // Remove a Shipment from the warehouse (e.g., being loaded to the vehicle)
    public boolean removeShipment(Shipment pkg) {
        for (int i = 0; i < currShipCount; i++) {
            if (shipments[i] != null && shipments[i].equals(pkg)) {
                for (int j = i; j < currShipCount - 1; j++) {
                    shipments[j] = shipments[j + 1];
                }
                shipments[currShipCount - 1] = null;
                currShipCount--;
                return true; 
            }
        }
        return false; 
    }

    // Add a vehicle to the warehouse (e.g., a vehicle came to the warehouse for loading or drop off)
    public boolean addVehicle(Vehicle vehicle) {
            vehicles[currVehicleCtr] = vehicle;
            currVehicleCtr++;
            return true;
    }

    // Remove a vehicle from the warehouse (e.g., a vehicle left for delivery to another warehouse location)
    public boolean removeVehicle(Vehicle vehicle) {
        for (int i = 0; i < currVehicleCtr; i++) {
            if (vehicles[i] != null && vehicles[i].equals(vehicle)) {
                for (int j = i; j < currVehicleCtr - 1; j++) {
                    vehicles[j] = vehicles[j + 1];
                }
                vehicles[currVehicleCtr - 1] = null;
                currVehicleCtr--;
                return true; 
            }
        }
        return false; 
    }

    // Check available space for shipments
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
            getcurrVehicleCtr()
        );
    }

    public String[] toCSVFormat(int sId, int vId) {
        return new String[]{
            String.valueOf(getWarehouseID()),
            String.valueOf(sId),
            String.valueOf(vId),
            location,
            String.valueOf(getmaxShipCount()),
            String.valueOf(getmaxShipCount())
        };
    }
}