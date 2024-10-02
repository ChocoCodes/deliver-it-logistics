public class Item {
    private String name;
    private float weight;
    private float height;
    private float width;
    private float length;

    public Item(String name, float weight, float height, float width, float length) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.width = width;
        this.length = length;
    }

    public String getName() { return this.name; }
    public float getWeight() { return this.weight; }
    public float getHeight() { return this.height; }
    public float getLength() { return this.width; }
    public float getWidth() { return this.length; }

    @Override
    public String toString() {
        return String.format("Name: %s\n, Weight: %.2f\n, Dimensions: %.2fx%.2fx%.2f",
            getName(),
            getWeight(),
            getHeight(),
            getWidth(),
            getLength()
        );
    }
}
