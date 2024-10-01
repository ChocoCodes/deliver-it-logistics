public class Item {
    private String name;
    private float weight;
    private float dimensionHeight;
    private float dimensionWidth;
    private float dimensionDepth;

    public Item(String name, float weight, float dimensionHeight, float dimensionWidth, float dimensionDepth) {
        this.name = name;
        this.weight = weight;
        this.dimensionHeight = dimensionHeight;
        this.dimensionWidth = dimensionWidth;
        this.dimensionDepth = dimensionDepth;
    }

    public String getName() {
        return this.name;
    }

    public float getWeight() {
        return this.weight;
    }

    public float getDimensionHeight() {
        return this.dimensionHeight;
    }

    public float getDimensionWidth() {
        return this.dimensionWidth;
    }

    public float getDimensionDepth() {
        return this.dimensionDepth;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\n, Weight: %.2f\n, Dimensions: %.2fx%.2fx%.2f",
            getName(),
            getWeight(),
            getDimensionHeight(),
            getDimensionWidth(),
            getDimensionDepth()
        );
    }
}
