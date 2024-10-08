public class Admin extends FrontlineEmployee {
    public Admin(String name, String username, String password) {
        super(name, username, password);
    }

    // Admin-specific functionalities
    public void addWarehouse() {
        // Admin can add Warehouse
    }

    public void removeWarehouse() {
        // Admin can remove Warehouse
    }

    public void addVehicles() {
        // Admin can add Vehicles
    }

    public void removeVehicles() {
        // Admin can remove Vehicles
    }

    public void generateReports(String fileReportLocation) {
        // Admin can generate system reports
    }

    @Override
    public void showMenu() {
        // Add admin-specific implementation
    }
}