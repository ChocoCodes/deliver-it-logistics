import java.util.ArrayList;

public class FrontlineEmployee extends Employee {

    public FrontlineEmployee(String name, String username, String password) {
        super(name, username, password);
    }

    @Override
    public void showMenu() {
        boolean exit = false;
    
        while (!exit) {
            System.out.println("---------------------------");
            System.out.println("1. Process Shipment");
            System.out.println("2. Exit");
            System.out.println("---------------------------");
            
            int choice = 0;
            while (true) {
                try {
                    choice = Integer.parseInt(Logistics.getInput("Enter your choice: "));
                    if (choice >= 1 && choice <= 2) {
                        break;
                    } else {
                        System.out.println("Invalid choice. Please select between 1 and 2.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter an integer.");
                }
            }
    
            switch (choice) {
                case 1:
                    processShipment(); 
                    break;
                case 2:
                    exit = true;
                    System.out.println("Exiting menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
                    break;
            }
        }
    }
    
    public void processShipment() {
        // Load available shipments from the CSV file
        CSVParser.setFilePath("CSVFiles/shipments.csv");
        String[][] shipmentData = CSVParser.loadCSVData(CSVParser.getFilePath());
        Shipment[] shipments = new Shipment[shipmentData.length];
        for (int i = 0; i < shipmentData.length; i++) {
            shipments[i] = Shipment.toShipment(shipmentData, i, null); // Pass null for the Package
        }
        
        // Display available shipments
        ArrayList<Shipment> paidShipments = new ArrayList<Shipment>();
        System.out.println("Available Shipments:");
        for (int i = 0; i < shipments.length; i++) {
            if (shipments[i].getStatus().equalsIgnoreCase("Paid")) {
                System.out.println((i + 1) + ". " + shipments[i].toString());
                paidShipments.add(shipments[i]);
            }
        }
        
        Shipment[] paidShipmentsArr = paidShipments.toArray(new Shipment[0]);
        // Ask the employee which shipment to process
        int selectedShipmentIndex = (Logistics.getValidatedInput("Select a number to manage Shipment: ", 1, shipments.length)) - 1;
    
        // Ask to confirm the shipment
        String confirmShipment = Logistics.getInput("Confirm shipment? (Yes/No): ");
        if (confirmShipment.equalsIgnoreCase("Yes") && shipments[selectedShipmentIndex].getStatus().equalsIgnoreCase("Paid")) {
            paidShipmentsArr[selectedShipmentIndex].confirmShip();
            paidShipmentsArr[selectedShipmentIndex].setStatus("Pending");;
            
            // Update the CSV with the confirmed shipment status
            CSVParser.updateShipmentCSV(paidShipmentsArr[selectedShipmentIndex].getShipmentID(), paidShipmentsArr[selectedShipmentIndex].isConfirmed(), 7); // TODO: Fix
            // Update the CSV Shipment Status to "Pending"
            CSVParser.updateShipmentCSV(paidShipmentsArr[selectedShipmentIndex].getShipmentID(), paidShipmentsArr[selectedShipmentIndex].getStatus(), 8); // TODO: Fix
            System.out.println("Shipment confirmed successfully.");
        } else if (confirmShipment.equalsIgnoreCase("Yes") && !(paidShipmentsArr[selectedShipmentIndex].getStatus().equalsIgnoreCase("Paid"))) {
            System.out.println("An error occured. Shipment status is not \"Paid\".");
            System.out.println("Shipment Get Status: " + paidShipmentsArr[selectedShipmentIndex].getStatus());
            System.out.println("Shipment confirmation canceled.");
        } else {
            System.out.println("Shipment confirmation canceled.");
        }
    }
}
