import java.util.Date;

public class Shipment {
    private int shipmentID; //NEEDED for Referencing
    private int currPkgLoc;
    private String destination; // receiver address alr avail - constructor
    private Vehicle vehicle; // assign by e/a
    private int vehicleID;  // NEW: FK to reference Vehicle ID
    private double shippingCost; // calculate - package-related methods are already implemented - implement shipping cost inside shipment
    private boolean confirmed; // assign by e/a - pending set confimed
    private Package pkg; // alr finish by the time shipment obj is instantiated - constructor
    private int pkgID; // Not sure if needed pero insurance 
    private String status; // control by e/a default - Pending
    private Date shipmentTakeOffDate; // assign after the vehicle left the main warehouse
    private Date estimatedDeliveryDate; // create algo acdg to warehouse location - random algo max 7 days

    // NOTE in Shipment CSV Shipment needs to have an FK of Vehicle ID
    // NOTE in Shipment CSV Shipment needs to have an FK of Package ID

    public Shipment(int shipmentID, String destination, Package pkg) {
        this.shipmentID = shipmentID;
        this.destination = destination;
        this.pkg = pkg;
        this.status = "Pending";
        this.confirmed = false;
    }

    public Package getPackage() {return this.pkg;}
    public int getShipmentID() {return shipmentID;}
    public String getStatus() {return status;}

    public void setVehicleID(int vehicleID) {this.vehicleID = vehicleID;} // Needed for Warehouse Employee
    public void setPackageID(int pkgID) {this.pkgID = pkgID;} // Not sure if needed pero insurance 
    public void setStatus(String status) {this.status = status;}
    // payShipment
    // calcShipCost
    // confirmShip
    // estDeliverDay
    // setStatus
    // setShipTakeOff - attr only
    // calcETA() - 7 days max
    // shipToCSVFormat()
}