import java.util.ArrayList;

public class FrontlineEmployee extends Employee {

    public FrontlineEmployee(String name) {
        super(name);
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
                    choice = Integer.parseInt(Logistics.getInput("Enter your choice"));
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
        // Check first if we have entries in the csv files - exit if none
        if(shipmentData.length == 0) {
            System.out.println("No available shipments for now.");
            return;
        }
        // Process the shipments by loading into a shipments array
        Shipment[] shipments = new Shipment[shipmentData.length];
        for (int i = 0; i < shipmentData.length; i++) {
            shipments[i] = Shipment.toShipment(shipmentData, i, null); // Pass null for the Package
        }
        boolean hasPaidShipments = false;
        // Display available shipments
        ArrayList<Shipment> paidShipments = new ArrayList<Shipment>();
        System.out.println("Available Shipments:");
        int n = 0;
        for (int i = 0; i < shipments.length; i++) {
            if (shipments[i].getStatus().equalsIgnoreCase("Paid")) {
                hasPaidShipments = true;
                System.out.printf("Shipment No. %d | ID: %d | Status: %s\n",(n + 1), shipments[i].getShipmentID(), shipments[i].getStatus());
                paidShipments.add(shipments[i]);
                n++;
            }
        }
        if(!hasPaidShipments) {
            System.out.println("No available shipment/s with status as Paid.");
            return;
        }
        Shipment[] paidShipmentsArr = paidShipments.toArray(new Shipment[0]);
        // Ask the employee which shipment to process
        int selectedShipmentIndex = 0;
        do {
            selectedShipmentIndex = (Logistics.getValidatedInput("Select a number to manage Shipment", 1, shipments.length)) - 1;
            if(selectedShipmentIndex >= n) System.out.printf("Select a valid range from %d to %d only.\n", 1, n);
        } while(selectedShipmentIndex >= n);
        // Ask to confirm the shipment
        String confirmShipment = Logistics.getInput("Confirm shipment? (Yes/No)");
        if (confirmShipment.equalsIgnoreCase("Yes") && paidShipmentsArr[selectedShipmentIndex].getStatus().equalsIgnoreCase("Paid")) {
            paidShipmentsArr[selectedShipmentIndex].confirmShip();
            paidShipmentsArr[selectedShipmentIndex].setStatus("Pending");
            paidShipmentsArr[selectedShipmentIndex].setShipTakeOff();
            paidShipmentsArr[selectedShipmentIndex].setEtaDelivery(paidShipmentsArr[selectedShipmentIndex].calcEstTime());
            System.out.printf("%s\n", paidShipmentsArr[selectedShipmentIndex].toString());
            // Update the CSV with the confirmed shipment status
            CSVParser.setFilePath("CSVFiles/shipments.csv");
            CSVParser.updateCSV(
                paidShipmentsArr[selectedShipmentIndex].getShipmentID(), 
                String.valueOf(paidShipmentsArr[selectedShipmentIndex].isConfirmed()), 
                6, 
                paidShipmentsArr[selectedShipmentIndex].getShipmentHeader()
            );
            
            // Update the CSV Shipment Status to "Pending"
            CSVParser.updateCSV(
                paidShipmentsArr[selectedShipmentIndex].getShipmentID(), 
                paidShipmentsArr[selectedShipmentIndex].getStatus(), 
                7, 
                paidShipmentsArr[selectedShipmentIndex].getShipmentHeader()
            );
            
            // Update ShipTakeOff and ETA Delivery
            CSVParser.updateCSV(
                paidShipmentsArr[selectedShipmentIndex].getShipmentID(), 
                CSVParser.dateToString(paidShipmentsArr[selectedShipmentIndex].getShipTakeOff()), 
                8, 
                paidShipmentsArr[selectedShipmentIndex].getShipmentHeader()
            );
            CSVParser.updateCSV(
                paidShipmentsArr[selectedShipmentIndex].getShipmentID(), 
                CSVParser.dateToString(paidShipmentsArr[selectedShipmentIndex].getEtaDelivery()), 
                9, 
                paidShipmentsArr[selectedShipmentIndex].getShipmentHeader()
            );
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
