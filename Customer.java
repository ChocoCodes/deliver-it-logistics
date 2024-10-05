public class Customer {
    private int id;
    private String name;
    private String contactInfo;
    private String address;

    // Customer Constructor
    public Customer(int id, String name, String contactInfo, String address) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.address = address;
    }

    // Getters
    public int getCustomerID() { return this.id; }
    public String getName() { return this.name; }
    public String getContactInfo() { return this.contactInfo; }
    public String getAddress() { return this.address; }

    public void setName(String name) { this.name = name; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String toString() {
        return String.format("ID: %d\nName: %s\nContact Number: %s\nAddress: %s\n", 
        getCustomerID(), 
        getName(), 
        getContactInfo(), 
        getAddress()
        );
    }

    public String[] toCSVFormat() {
        return new String[] {
            String.valueOf(getCustomerID()),
            getName(),
            getContactInfo(),
            getAddress()
        };
    }
}
