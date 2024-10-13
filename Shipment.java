import java.util.Date;
import java.util.Random;
import java.util.Calendar;

public class Shipment {
    private final String[] SHIPMENT_H = {"id","sId","vId","wId","dest","shipCost","confirmed","status","shipDate","eta"};
    private int id;
    private int whId;
    private int vId; // assign by e/a
    private String destination; // receiver address alr avail - constructor
    private double shippingCost; // calculate - package-related methods are already implemented - implement shipping cost inside shipment
    private boolean confirmed; // assign by e/a - pending set confimed
    private Package pkg; // alr finish by the time shipment obj is instantiated - constructor
    private String status; // control by e/a default - Pending
    private Date shipTakeOff; // assign after the vehicle left the main warehouse
    private Date estDelivery; // create algo acdg to warehouse location - random algo max 7 days

    // Constructor for new Shipments Created
    public Shipment(int id, String destination, Package pkg) {
        this.id = id;
        this.whId = 0;
        this.destination = destination;
        this.pkg = pkg;
        this.status = "Pending";
        this.confirmed = false;
        this.shipTakeOff = null;
        this.estDelivery = null;
    }
    // Constructor for Shipment CSV File Extraction
    public Shipment(int id, int vId, int whId, String destination, double shippingCost, boolean confirmed, Package pkg, String status, Date shipTakeOff, Date estDelivery) {
        this.id = id;
        this.whId = whId;
        this.vId = vId;
        this.destination = destination;
        this.shippingCost = shippingCost;
        this.confirmed = confirmed;
        this.pkg = pkg;
        this.status = status;
        this.shipTakeOff = shipTakeOff;
        this.estDelivery = estDelivery;
    }
    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setShipTakeOff() { if (this.shipTakeOff == null) this.shipTakeOff = new Date(); }
    public void setEtaDelivery(Date estDelivery) { this.estDelivery = estDelivery; }
    public void setWarehouseId(int whId) { this.whId = whId; }
    public void setVehicleId(int vId) { this.vId = vId; }
    // Getters
    public Date getEtaDelivery() { return this.estDelivery; }
    public String getStatus() { return this.status; }
    public Date getShipTakeOff() { return this.shipTakeOff; }
    public void confirmShip() { if (!confirmed) this.confirmed = true; }
    public Package getPackage() { return this.pkg; }
    public int getShipmentID() { return this.id; }
    public String getDestination() { return this.destination; }
    public boolean isConfirmed() { return this.confirmed; }
    public double getShipCost() { return this.shippingCost; }
    public int getWarehouseId() { return this.whId; }
    public int getVehicleId() { return this.vId; }
    public String[] getShipmentHeader() { return this.SHIPMENT_H; }
    // Algorithm to select an "estimated" timeframe of the shipment delivery within 7 days
    // using Calendar class to manipulate Date and Random class to implement randomization
    public Date calcEstTime() {
        Date takeOff = getShipTakeOff();
        Random rand = new Random();
        int etaRange = rand.nextInt(7) + 1;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(takeOff);
        calendar.add(Calendar.DAY_OF_MONTH, etaRange);
        return calendar.getTime();
    }
    // Calculate Delivery Fee in Fixed Rate for within and outside Bacolod
    private double calcShipFee() {
        String dest = getDestination();
        return (dest.toLowerCase().contains("bacolod") || dest.toLowerCase().contains("bcd")) ? 50.00 : 150.00;
    }
    // Get the shipping fee by adding the box fee acdg. to weight basis (actual or dimensional), and the ship fee acdg to region location
    public void calcShipCost() {  
        Package deliveryPkg = getPackage();
        double basis = deliveryPkg.detWeightBasis();
        this.shippingCost = deliveryPkg.calcBoxFee(basis) + calcShipFee();
    }

    // Format the same as header: id,pkgId,vId,wId,dest,shipCost,confirmed,status,shipDate,eta
    public String[] toCSVFormat() {
        return new String[] {
            String.valueOf(getShipmentID()),
            String.valueOf(getPackage().getId()),
            String.valueOf(getVehicleId()),
            String.valueOf(getWarehouseId()),
            getDestination(),
            String.valueOf(getShipCost()),
            String.valueOf(isConfirmed()),
            getStatus(),
            getShipTakeOff() == null ? "Pending" : CSVParser.dateToString(getShipTakeOff()),
            getEtaDelivery() == null ? "Pending" : CSVParser.dateToString(getEtaDelivery())
        };
    }

    @Override
    public String toString() {
        return String.format("Shipment ID: %d\nVehicle ID: %d\nWarehouse ID: %d\nDestination: %s\nShipping Cost: %.2f\nShipment Confirmed: %b\nStatus: %s\nTake Off: %s\nETA: %s",
            getShipmentID(),
            getVehicleId(),
            getWarehouseId(),
            getDestination(),
            getShipCost(),
            isConfirmed(),
            getStatus(),
            getShipTakeOff() == null ? "Pending" : CSVParser.dateToString(getShipTakeOff()),
            getEtaDelivery() == null ? "Pending" : CSVParser.dateToString(getEtaDelivery()) 
        );
    }

    public void displayShipmentForm() {
        System.out.println("=====================================");
        System.out.println("Shipment Form");
        System.out.println("-------------------------------------");
        System.out.printf("%s\n", toString());
        System.out.println("\n=====================================");
        getPackage().displayPackageContents();
    }

    public static Shipment toShipment(String[][] raw, int idx, Package pkg) {
        return new Shipment(
            CSVParser.toInt(raw[idx][0]),
            CSVParser.toInt(raw[idx][2]),
            CSVParser.toInt(raw[idx][3]),
            raw[idx][4],
            CSVParser.toDouble(raw[idx][5]),
            Boolean.parseBoolean(raw[idx][6]),
            pkg,
            raw[idx][7],
            CSVParser.strToDate(raw[idx][8]),
            CSVParser.strToDate(raw[idx][9])
        );
    }
}