public class Warehouse {
    private int wareHouseID;
    private String location;
    private int maxShipmentCount; 
    private int currentShipmentCount;
    private int maxVehicleCount;
    private int currentVehicleCount;
    private Shipment[] Shipments; // pkgID in csv
    private Vehicle[] vehicles; // vID in csv

    // Warehouse Constructor
    public Warehouse(int wareHouseID, String location, int maxShipmentCount, int maxVehicleCount) {
        this.wareHouseID = wareHouseID;
        this.location = location;
        this.maxShipmentCount = maxShipmentCount;
        this.currentShipmentCount = 0;
        this.maxVehicleCount = maxVehicleCount;
        this.currentVehicleCount = 0;
        this.Shipments = new Shipment[maxShipmentCount];
        this.vehicles = new Vehicle[maxVehicleCount]; 
    }

    // Getters
    public int getWarehouseID() { return wareHouseID; }
    public String getLocation() { return location; }
    public int getmaxShipmentCount() { return maxShipmentCount; }
    public int getCurrentShipmentCount() { return currentShipmentCount; }
    public int getmaxVehicleCount() { return maxVehicleCount; }
    public int getCurrentVehicleCount() { return currentVehicleCount; }
    public Shipment[] getShipments() { return Shipments; }
    public Vehicle[] getVehicles() { return vehicles; }

    // Add a Shipment to the warehouse (e.g., drop off Shipment of a vehicle)
    public boolean addShipment(Shipment pkg) {
        if (currentShipmentCount < maxShipmentCount) {
            Shipments[currentShipmentCount] = pkg;
            currentShipmentCount++;
            return true;
        }
        return false; 
    }

    // Remove a Shipment from the warehouse (e.g., being loaded to the vehicle)
    public boolean removeShipment(Shipment pkg) {
        for (int i = 0; i < currentShipmentCount; i++) {
            if (Shipments[i] != null && Shipments[i].equals(pkg)) {
                for (int j = i; j < currentShipmentCount - 1; j++) {
                    Shipments[j] = Shipments[j + 1];
                }
                Shipments[currentShipmentCount - 1] = null;
                currentShipmentCount--;
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

    // Check available space for Shipments
    public int getAvailableSpace() {
        return maxShipmentCount - currentShipmentCount;
    }

    // Display info about the warehouse
    @Override
    public String toString() {
        return String.format(
            "Warehouse ID: %d\nLocation: %s\nmaxShipmentCount: %d\nCurrent Shipments: %d\nMax Vehicles: %d\nCurrent Vehicles: %d\n",
            getWarehouseID(),
            getLocation(),
            getmaxShipmentCount(),
            getCurrentShipmentCount(),
            getmaxVehicleCount(),
            getCurrentVehicleCount()
        );
    }

    public String[] toCSVFormat() {
        return new String[]{
                String.valueOf(getWarehouseID()),
                location,
                String.valueOf(getmaxShipmentCount()),
                String.valueOf(getmaxVehicleCount())
        };
    }
}
