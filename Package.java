import java.util.Date;

public class Package {
    private int id;
    private double dimensionalWeight;
    private Dimension dimension;
    private Date packageCreated;
    private Item[] contents;
    private String receiverAddress;

    public Package(int id, Item[] contents, String receiverAddress) {
        this.id = id;
        this.packageCreated = new Date();
        this.contents = contents;
        this.receiverAddress = receiverAddress;
        this.dimension = setDimensions();
        this.dimensionalWeight = setDimensionalWeight();
    }
    
    public int getId() { return this.id; }
    public double getDimensionWeight() { return this.dimensionalWeight; }
    public Date getDate() { return this.packageCreated; }
    public Item[] getContents() { return this.contents; }
    public String getreceiverAddress() { return this.receiverAddress; }
    public int itemCounts() { return getContents().length; }
    public Dimension getDimensions() { return this.dimension; }

    public void setId(int id) { this.id = id; }
    public void setDate(Date packageCreated) { this.packageCreated = packageCreated; }
    public void setContents(Item[] contents) { this.contents = contents; }
    public void setReceiver(String receiverAddress) { this.receiverAddress = receiverAddress; }

    // Calculate the Dimensional Weight of a package in cm^3/kg.
    public double setDimensionalWeight() {
        return (getDimensions().getLength() * getDimensions().getWidth() * getDimensions().getHeight()) / 5000;
    }
    
    // Sum all item weights in KG - different from the package's dimensional weight
    public double getTotalItemWeight(Item[] contents) { 
        double weight = 0.0;
        for(Item item : contents) { weight += item.getWeight(); }
        return weight;
    }

    public Dimension setDimensions() {
        double lengthTmp = 0.0, widthTmp = 0.0, heightTmp = 0.0;
        for (Item item : getContents()) {
            lengthTmp = Math.max(lengthTmp, item.getDimensions().getLength());
            widthTmp = Math.max(widthTmp, item.getDimensions().getWidth());
            heightTmp = Math.max(heightTmp, item.getDimensions().getHeight());
        }
        return new Dimension(lengthTmp, widthTmp, heightTmp);
    }

    public void displayPackageContents() {
        for(int i = 0; i < contents.length; i++) {
            System.out.println(contents.toString());
        }
    }
}

class Item {
    private String name;
    private double weight; // in kilograms
    private Dimension dimension;

    public Item(String name, double weight, Dimension dimension) {
        this.name = name;
        this.weight = toKilograms(weight);
        this.dimension = dimension;
    }

    public String getName() { return this.name; }
    public double getWeight() { return this.weight; }
    public Dimension getDimensions() { return this.dimension; }

    @Override
    public String toString() {
        return String.format("Name: %s\nWeight: %.2f\nDimensions: %.2fx%.2fx%.2f",
            getName(),
            getWeight(),
            getDimensions().getLength(),
            getDimensions().getWidth(),
            getDimensions().getHeight()
        );
    }

    public double toKilograms(double grams) {
        return grams / 1000.0;
    }
}

// All measurements in centimeters for accurate calculations later
class Dimension {
    private double height;
    private double width;
    private double length;

    public Dimension(double height, double width, double length) {
        this.height = height;
        this.width = width;
        this.length = length;
    }

    public double getLength() { return this.length; }
    public double getWidth() { return this.width; }
    public double getHeight() { return this.height; }

    public void setLength(double length) { this.length = length; }
    public void setWidth(double width) { this.width = width; }
    public void setHeight(double height) { this.height = height; }
}

class Shipment {
    private Warehouse dropOff; 
    private String destination;
    private Vehicle vehicle;
    private double shippingCost;
    private boolean confirmed;
    private Package pkg;
    private String status;
    private Date shipmentTakeOffDate;
    private Date estimatedDeliveryDate; 
}