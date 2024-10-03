import java.util.Date;

public class Package {
    private int id;
    private float weight;
    private float length;
    private float width;
    private float height;
    private Date packageCreated;
    private Item[] contents;
    private String receiverAddress;

    public Package(int id, float weight, Date packageCreated, Item[] contents, String receiverAddress) {
        this.id = id;
        this.weight = weight;
        this.packageCreated = packageCreated;
        this.contents = contents;
        this.receiverAddress = receiverAddress;
    }

    public void setId(int id) { this.id = id; }
    public void setWeight(float weight) { this.weight = weight; }
    public void setLength(float length) { this.length = length; }
    public void setWidth(float width) { this.width = width; }
    public void setHeight(float height) { this.height = height; }
    public void setDate(Date packageCreated) { this.packageCreated = packageCreated; }
    public void setContents(Item[] contents) { this.contents = contents; }
    public void setReceiver(String receiverAddress) { this.receiverAddress = receiverAddress; }

    public int getId() { return this.id; }
    public double getWeight() { return this.weight; }
    public float getLength() { return this.length; }
    public float getWidth() { return this.width; }
    public float getHeight() { return this.height; }
    public Date getDate() { return this.packageCreated; }
    public Item[] getContents() { return this.contents; }
    public String getreceiverAddress() { return this.receiverAddress; }
    public int itemCounts() { return getContents().length; }
    
    public void displayPackageContents() {
        for(int i = 0; i < contents.length; i++) {
            System.out.println(contents.toString());
        }
    }
    
    public void setPackageDimensions() {
        float lengthTmp = 0, widthTmp = 0, heightTmp = 0;
        for (Item item: getContents()) {
            lengthTmp = Math.max(lengthTmp, item.getLength());
            widthTmp = Math.max(widthTmp, item.getWidth());
            heightTmp = Math.max(heightTmp, item.getHeight());
        }
        setLength(lengthTmp);
        setWidth(widthTmp);
        setHeight(heightTmp);
    }
}
