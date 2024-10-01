public class Customer {
    protected int customerID;
    protected String name;
    protected String contactInfo;
    protected String address;
    protected String receiverAddress;

    // Customer Constructor
    public Customer(int customerID, String name, String contactInfo, String address, String receiverAddress) {
        this.customerID = customerID;
        this.name = name;
        this.contactInfo = contactInfo;
        this.address = address;
        this.receiverAddress = receiverAddress;
    }

    // Getters
    public int getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getAddress() {
        return address;
    }

    public String getRecieverAddress() {
        return receiverAddress;
    }

    // Customer Details
    public void displayCustomerDetails() {
        System.out.println("=========================================");
        System.out.println("Customer ID: " + getCustomerID());
        System.out.println("Customer Name: " + getName());
        System.out.println("Customer Contact Info: " + getContactInfo());
        System.out.println("Customer Address: " + getAddress());
        System.out.println("Receiver Address: " + getRecieverAddress());
        System.out.println("=========================================");
    }

    // Update Customer Details
    public void updateCustomerDetails(String name, String contactInfo, String address, String receiverAddress) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.address = address;
        this.receiverAddress = receiverAddress;
    }
}
