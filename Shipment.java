import java.util.Date;

public class Shipment {
    private int currPkgLoc
    private String destination; // receiver address alr avail - constructor
    private Vehicle vehicle; // assign by e/a
    private double shippingCost; // calculate - package-related methods are already implemented - implement shipping cost inside shipment
    private boolean confirmed; // assign by e/a - pending set confimed
    private Package pkg; // alr finish by the time shipment obj is instantiated - constructor
    private String status; // control by e/a default - Pending
    private Date shipmentTakeOffDate; // assign after the vehicle left the main warehouse
    private Date estimatedDeliveryDate; // create algo acdg to warehouse location - random algo max 7 days

    public Shipment(String destination, Package pkg) {
        this.destination = destination;
        this.pkg = pkg;
        this.status = "Pending";
        this.confirmed = false;
    }
    
    // payShipment
    // calcShipCost
    // confirmShip
    // estDeliverDay
    // setStatus
    // setShipTakeOff - attr only
    // calcETA() - 7 days max
    // shipToCSVFormat()
}