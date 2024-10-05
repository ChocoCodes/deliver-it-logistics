import java.util.Date;

public class Package {
    private int id;
    private String receiverAddress;
    private Date packageCreated;
    private double dimensionalWeight;
    private Dimension dimension;
    private Item[] contents;

    public Package(int id, Item[] contents, String receiverAddress) {
        this.id = id;
        this.packageCreated = new Date();
        this.contents = contents;
        this.receiverAddress = receiverAddress;
        this.dimension = calculateDimensions();
        this.dimensionalWeight = calculateDimensionalWeight();
    }
    
    public int getId() { return this.id; }
    public double getDimensionalWeight() { return this.dimensionalWeight; }
    public Date getDate() { return this.packageCreated; }
    public Item[] getContents() { return this.contents; }
    public String getreceiverAddress() { return this.receiverAddress; }
    public int itemCounts() { return getContents().length; }
    public Dimension getDimensions() { return this.dimension; }

    public void setId(int id) { this.id = id; }
    public void setDate(Date packageCreated) { this.packageCreated = packageCreated; }
    public void setContents(Item[] contents) { this.contents = contents; }
    public void setReceiver(String receiverAddress) { this.receiverAddress = receiverAddress; }

    // Return a String data for CSV Format used in parsing
    public String[] toCSVFormat(int customerID) { 
        Dimension dim = getDimensions();
        return new String[] {
            String.valueOf(getId()), 
            String.valueOf(customerID),
            getreceiverAddress(), 
            String.valueOf(getDimensionalWeight()), 
            String.valueOf(dim.getLength()), 
            String.valueOf(dim.getWidth()), 
            String.valueOf(dim.getHeight())
        }; 
    }
    // Calculate the Dimensional Weight of a package in cm^3/kg.
    public double calculateDimensionalWeight() {
        double boxVolume = calculateBoxVolume();
        return boxVolume / 5000;
    }
    // Get which weight is bigger as basis for calculating costs: fairly priced for the space they occupy.
    // assumed weight - total item weight only, dimensional weight - amount of space a package takes up relative to its actual weight
    public double getPricingBasis() {
        double assumedWeight = getTotalItemWeight(getContents());
        return Math.max(assumedWeight, getDimensionalWeight());
    }
    // Get volume length of the max dimensions of the package
    public double calculateBoxVolume() {
        Dimension dim = getDimensions();
        return dim.getLength() * dim.getWidth() * dim.getHeight();
    }
    // Sum all item weights in KG - different from the package's dimensional weight
    public double getTotalItemWeight(Item[] contents) { 
        double weight = 0.0;
        for(Item item : contents) { weight += item.getWeight(); }
        return weight;
    }
    public Dimension calculateDimensions() {
        double maxLength = 0.0, maxWidth = 0.0, maxHeight = 0.0;
        for (Item item : getContents()) {
            Dimension dim = item.getDimensions();
            maxLength = Math.max(maxLength, dim.getLength());
            maxWidth = Math.max(maxWidth, dim.getWidth());
            maxHeight = Math.max(maxHeight, dim.getHeight());
        }
        return new Dimension(maxLength, maxWidth, maxHeight);
    }

    @Override
    public String toString() {
        Dimension dim = getDimensions();
        return String.format(
            "Package ID: %d\nDimensions: %dx%dx%d cm.\nItem Counts: %d\nReceiver Address: %s\n",
            getId(),
            (int) Math.round(dim.getLength()),
            (int) Math.round(dim.getWidth()),
            (int) Math.round(dim.getHeight()),
            itemCounts(),
            getreceiverAddress()
        );
    }

    public void displayPackageContents() {
        System.out.println("| name | weight(kg) | dimensions(cm) |");
        for(int i = 0; i < contents.length; i++) {
            System.out.println(contents[i].toString());
        }
    }
}

