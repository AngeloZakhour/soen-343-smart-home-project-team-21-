import javax.swing.*;

public class Main {

    //Frames
    private static Login loginFrame = new Login("Login");
    private static SmartHomeDashboard dashboard;

    //Bounds variables
    private static final int xLogin = 300;
    private static final int yLogin = 200;
    private static final int widthLogin = 400;
    private static final int heightLogin = 300;

    private static final int xDash = 100;
    private static final int yDash = 100;
    private static final int widthDash = 1100;
    private static final int heightDash = 600;


    public static void loginClicked(String username, String password){
        // User Authentication
        User user = UserManager.findUser(username, password);
        if(user != null) {
            loginFrame.setVisible(false);

            // User type
            if(user instanceof Child) {
                System.out.println("It is a child");
                // Show house simulator for child
                dashboard = new SmartHomeDashboard("Smart Home Simulator", UserTypes.CHILD.toString(), username);
            } else if (user instanceof Parent) {
                System.out.println("It is a parent");
                // Show house simulator for parent
                dashboard = new SmartHomeDashboard("Smart Home Simulator", UserTypes.PARENT.toString(), username);
            } else if (user instanceof Guest) {
                System.out.println("It is a guest");
                // Show house simulator for guest
                dashboard = new SmartHomeDashboard("Smart Home Simulator", UserTypes.GUEST.toString(), username);
            }
            if(dashboard != null) {
                dashboard.setBounds(xDash, yDash, widthDash, heightDash);
                dashboard.setVisible(true);
                dashboard.setResizable(false);
            }
        } else {
            System.out.println("Login failed");
            //display failed login message
        }
    }

    public static void logoutClicked(){
        dashboard.setVisible(false);
        loginFrame = new Login("Login");
        setUpLoginFrame();
    }

    public static void setUpLoginFrame() {
        //Show login frame
        loginFrame.setBounds(xLogin, yLogin, widthLogin, heightLogin);
        loginFrame.setVisible(true);
    }

    public static void blockWindow(String name, boolean blocked){ dashboard.blockWindow(name, blocked); }

    public static boolean isWindowBlocked(String name){
        return dashboard.isWindowBlocked(name);
    }

    public static void main(String[] args){

        setUpLoginFrame();
        // Creates users
        UserManager.initialize();

        // This is to read house layout file submitted by user (right now it is hardcoded to HouseLayout.txt
        //HouseReader.readHouseLayout("Houselayout.txt");
    }
}
