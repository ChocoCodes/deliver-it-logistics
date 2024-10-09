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
        Shipment[] shipments = CSVParser.toShipment(shipmentData); // TODO
        
        // Display available shipments
        System.out.println("Available Shipments:");
        for (int i = 0; i < shipments.length; i++) {
            System.out.println((i + 1) + ". " + shipments[i].toString());
        }
    
        // Ask the employee which shipment to process
        int selectedShipmentIndex = 0;
        while (true) {
            try {
                selectedShipmentIndex = Integer.parseInt(Logistics.getInput("Enter the number of the shipment to process: ")) - 1;
                if (selectedShipmentIndex >= 0 && selectedShipmentIndex < shipments.length) {
                    break;
                } else {
                    System.out.println("Invalid selection. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input should be an integer. Try again.");
            }
        }
    
        // Ask to confirm the shipment
        String confirmShipment = Logistics.getInput("Confirm shipment? (Yes/No): ");
        if (confirmShipment.equalsIgnoreCase("Yes")) {
            shipments[selectedShipmentIndex].confirmShip();
            
            // Update the CSV with the confirmed shipment status
            CSVParser.updateShipmentCSV(shipments[selectedShipmentIndex].getShipmentID(), shipments[selectedShipmentIndex].isConfirmed(), 6); // TODO:
            System.out.println("Shipment confirmed successfully.");
        } else {
            System.out.println("Shipment confirmation canceled.");
        }
    }
}
