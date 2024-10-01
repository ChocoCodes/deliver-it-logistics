import java.util.Date;

public class Package {
    private int id;
    private float weight;
    private float dimensions;
    private Date packageCreated;
    private Item[] contents;
    private String receiverAddress;

    public Package(int id, float weight, float dimensions, Date packageCreated, Item[] contents, String receiverAddress) {
        this.id = id;
        this.weight = weight;
        this.dimensions = dimensions;
        this.packageCreated = packageCreated;
        this.contents = contents;
        this.receiverAddress = receiverAddress;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }
    public void setDimensions(float dimensions) {
        this.dimensions = dimensions;
    }
    public void setDate(Date packageCreated) {
        this.packageCreated = packageCreated;
    }
    public void setContents(Item[] contents) {
        this.contents = contents;
    }
    public void setId(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public int getId() {
        return this.id;
    }
    public double getWeight() {
        return this.weight;
    }
    public float getDimensions() {
        return this.dimensions;
    }
    public Date getDate() {
        return this.packageCreated;
    }
    public Item[] getContents() {
        return this.contents;
    }
    public String getreceiverAddress() {
        return this.receiverAddress;
    }

    public void displayPackageContents() {
        for(int i = 0; i < contents.length; i++) {
            System.out.println(contents.toString());
        }
    }

    public float calculateTotalDimensions(Item[] contents) {
        float dim_l = 0, dim_w = 0, dim_h = 0;
        for(Item item: contents) {
            dim_l = Math.max(dim_l, item.getDimensionDepth());
            dim_h = Math.max(dim_h, item.getDimensionHeight());
            dim_w = Math.max(dim_w, item.getDimensionHeight());
        }
        return dim_l * dim_h * dim_w;
   }
}
