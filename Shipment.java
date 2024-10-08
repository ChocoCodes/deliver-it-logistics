import java.util.Date;
import java.util.Random;
import java.util.Calendar;

public class Shipment {
    private int id;
    private String destination; // receiver address alr avail - constructor
    private Vehicle vehicle; // assign by e/a
    private int vehicleID;  // NEW: FK to reference Vehicle ID
    private double shippingCost; // calculate - package-related methods are already implemented - implement shipping cost inside shipment
    private boolean confirmed; // assign by e/a - pending set confimed
    private Package pkg; // alr finish by the time shipment obj is instantiated - constructor
    private int pkgID; // Not sure if needed pero insurance 
    private String status; // control by e/a default - Pending
    private Date shipTakeOff; // assign after the vehicle left the main warehouse
    private Date estDelivery; // create algo acdg to warehouse location - random algo max 7 days

    public Shipment(String destination, Package pkg) {
        this.destination = destination;
        this.pkg = pkg;
        this.status = "Pending";
        this.confirmed = false;
    }
    // Constructor for Shipment CSV File Extraction
    public Shipment(String destination, double shippingCost, boolean confirmed, Package pkg, String status, Date shipTakeOff, Date estDelivery) {
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
    // Getters
    public Date getEtaDelivery() { return this.estDelivery; }
    public String getStatus() { return this.status; }
    public Date getShipTakeOff() { return this.shipTakeOff; }
    public void confirmShip() { if (!confirmed) this.confirmed = true; }
    // TODO: calcShipCost
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
    // shipToCSVFormat()
}