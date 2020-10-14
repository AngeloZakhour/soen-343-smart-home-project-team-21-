import java.util.HashMap;

public class UserManager {
    private static HashMap<String, String> authenticate = new HashMap<String, String>();
    private static HashMap<String, Parent> userParent = new HashMap<String, Parent>();
    private static HashMap<String, Child> userChild = new HashMap<String, Child>();
    private static HashMap<String, Guest> userGuest = new HashMap<String, Guest>();

    public static boolean isUserValid(String username, String password) {
        return authenticate.get(username) != null && authenticate.get(username).equals(password);
    }

    public static User findUser(String username, String password) {
        if(isUserValid(username, password)) {
            if(userParent.get(username) != null) { return userParent.get(username);}
            if(userChild.get(username) != null) { return userChild.get(username);}
            if(userGuest.get(username) != null) { return userGuest.get(username);}
            return null;
        }
        // User it not valid
        return null;
    }

    public static void addUser(String username, String password, String type) {
        if (authenticate.get(username) != null){
            System.out.println("Username already exists");
            return;
        }

        authenticate.put(username, password);

        if(type.equals(UserTypes.PARENT.toString())) {
            userParent.put(username, new Parent(username, password));
        } else if(type.equals(UserTypes.CHILD.toString())) {
            userChild.put(username, new Child(username, password));
        } else if(type.equals(UserTypes.GUEST.toString())){
            userGuest.put(username, new Guest(username, password));
        }

        System.out.println("Successfully added");

    }

    public static void removeUser(String username) {
        if(authenticate.get(username) == null) {
            System.out.println("User doesn't exist");
            return;
        }
        authenticate.remove(username);
        if(userParent.get(username) != null) {
            userParent.remove(username);
        } else if(userChild.get(username) != null) {
            userChild.remove(username);
        } else if(userGuest.get(username) != null) {
            userGuest.remove(username);
        }
        System.out.println("Successfully removed");
    }

    public static void initialize() {
        addUser("Parent1", "passwordabc", UserTypes.PARENT.toString());
        addUser("Parent2", "password123", UserTypes.PARENT.toString());
        addUser("Child1", "abc", UserTypes.CHILD.toString());
        addUser("Child2", "123", UserTypes.CHILD.toString());
        addUser("Guest", "password", UserTypes.GUEST.toString());
    }

    public static String[] getUsernames() {
        System.out.println(authenticate.keySet().toArray(new String[authenticate.size()]));
        return authenticate.keySet().toArray(new String[authenticate.size()]);
    }

    public static void clearUsers() {
        authenticate.clear();
        userParent.clear();
        userChild.clear();
        userGuest.clear();
    }

    public static int sizeAuthenticate() {
        return authenticate.size();
    }

    public static int sizeUserParent() {
        return userParent.size();
    }

    public static int sizeUserChild() {
        return userChild.size();
    }

    public static int sizeUserGuest() {
        return userGuest.size();
    }
}
