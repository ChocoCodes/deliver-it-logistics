import java.util.Date;

public class Shipment {
    private Warehouse dropOff; // assign by employee/admin
    private String destination; // receiver address alr avail - constructor
    private Vehicle vehicle; // assign by e/a
    private double shippingCost; // calculate - package-related methods are already implemented - implement shipping cost inside shipment
    private boolean confirmed; // assign by e/a
    private Package pkg; // alr finish by the time shipment obj is instantiated - constructor
    private String status; // control by e/a
    private Date shipmentTakeOffDate; // assign after ???
    private Date estimatedDeliveryDate; // create algo acdg to warehouse location
}