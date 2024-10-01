public class Item {
    protected String name;
    protected float weight;
    protected float dimensionHeight;
    protected float dimensionWidth;
    protected float dimensionDepth;

    public Item(String name, float weight, float dimensionHeight, float dimensionWidth, float dimensionDepth) {
        this.name = name;
        this.weight = weight;
        this.dimensionHeight = dimensionHeight;
        this.dimensionWidth = dimensionWidth;
        this.dimensionDepth = dimensionDepth;
    }

    public String getName() {
        return name;
    }

    public float getWeight() {
        return weight;
    }

    public float getDimensionHeight() {
        return dimensionHeight;
    }

    public float getDimensionWidth() {
        return dimensionWidth;
    }

    public float getDimensionDepth() {
        return dimensionDepth;
    }

    public void displayItemInfo() {
        System.out.println("Item name: " + getName());
        System.out.println("Item weight: " + getWeight());
        System.out.println("Height: " + getDimensionHeight());
        System.out.println("Width: " + getDimensionWidth());
        System.out.println("Depth: " + getDimensionDepth());
    }
}
