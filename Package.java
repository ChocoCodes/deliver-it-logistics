import java.util.Date;

public class Package {
    protected int id;
    protected float weight;
    protected float dimensions;
    protected Date packageCreated;
    protected  Item[] contents;
    protected String receiverAddress;
    public Package(int id, float weight, float dimensions, Date packageCreated, Item[] contents, String receiverAddress) {
        this.id = id;
        this.weight = weight;
        this.dimensions = dimensions;
        this.packageCreated = packageCreated;
        this.contents = contents;
        this.receiverAddress = receiverAddress;
    }
   public float calculateTotalDimensions(Item[] contents) {
        float dim_l = 0, dim_w = 0, dim_h = 0;
        for(Item item: contents) {
            if(item.getDimensionDepth() > dim_l) {
                dim_l = item.getDimensionDepth();
            }
            if(item.getDimensionHeight() > dim_h) {
                dim_h = item.getDimensionHeight();
            }
            if(item.getDimensionWidth() > dim_w) {
                dim_w = item.getDimensionWidth();
            }
        }
        return dim_l * dim_h * dim_w;
   }
}
