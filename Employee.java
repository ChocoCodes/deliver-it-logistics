public abstract class Employee {
    public static final String DEFAULT_USER = "employee";
    public static final String DEFAULT_PASS = "employee123";

    protected String name;

    public Employee(String name) {
        this.name = name;
    }

    public static boolean login(String inUser, String inPass) {
        return DEFAULT_USER.equals(inUser) && DEFAULT_PASS.equals(inPass);
    }

    public abstract void showMenu();
}