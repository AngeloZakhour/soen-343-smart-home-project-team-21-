import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SmartHomeDashboard extends JFrame{
    private JPanel mainPanel;
    private JButton onOff;
    private JPanel Simulation;
    private JButton editUserLocation;
    private JTabbedPane tabbedPane1;
    private JPanel MIDPanel;
    private JPanel HouseLayout;
    private JPanel Console;
    private JTextArea consoleText;
    private JLabel Image;
    private JLabel Type;
    private JLabel Username;
    private JComboBox<String> comboDate;
    private JComboBox<String> comboDay;
    private JComboBox<String> comboMonth;
    private JComboBox<String> comboYear;
    private JLabel dayOfWeek;
    private JLabel Month;
    private JLabel Year;
    private JSpinner hourSpinner;
    private JSpinner minuteSpinner;
    private JSpinner secondSpinner;
    private JLabel hourLabel;
    private JLabel minuteLabel;
    private JLabel secondsLabel;
    private JLabel setTimeLabel;
    private JLabel setDateLabel;
    private JLabel editUserLabel;
    private JLabel selectUserLabel;
    private JComboBox<String> comboUsers;
    private JButton addUserButton;
    private JLabel userAccessLabel;
    private JComboBox<String> comboBox2;
    private JButton editUsrButton;
    private JButton addAccessButton;
    private JButton removeAccessButton;
    private JComboBox<String> comboLocation;
    private JLabel dateLabel;
    private JLabel currentLocLabel;
    private JLabel timeLabel;
    private JButton LogOutButton;
    private JPanel SHS;
    private JPanel SHC;
    private JPanel SHP;
    private JPanel SHH;
    private JList<SmartObjectType> listItems;
    private JList<String> listOpenClose;
    private JSpinner outSideTemp;
    private JLabel imageLayout;
    private JLabel Outsidetemplabel;
    private JLabel outsidetempvalue;
    private JLabel itemsLabel;
    private JPanel openClosePanel;
    private Timer timer;
    private House house;
    private boolean welcomemessagedisplayed = false;


    private static Edit editFrame = new Edit("Edit");

    // Boundaries of the Edit window.
    private static final int xEdit = 300;
    private static final int yEdit = 200;
    private static final int widthEdit = 600;
    private static final int heightEdit = 300;

    /**
     * Parameterised constructor.
     *
     * @param title String title of the dashboard frame
     * @param type String type of user that logged in.
     * @param username String username of the user that logged in.
     */
    public SmartHomeDashboard(String title, String type, String username) {
        // Set up dashboard with correct parameters
        super(title);
        Type.setText(type);
        Username.setText(username);
        house = HouseReader.loadhouse("Houselayout");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();

        setUpDashboardOptions();
        addActionListeners();
    }

    /**
     * Method that sets up all the comboboxes and spinner in the SHC and SHH tabs when dashboard is created.
     */
    public void setUpDashboardOptions() {
        //Setting up items in "Date" section comboboxes
        String[] weekDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] months = {"January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"};


        comboDay.addItem("Something"); //Without this the first the for loop does not add any item to comboDay.
        comboDay.removeAllItems();

        for(String x : weekDays) comboDay.addItem(x);
        for(String x : months) comboMonth.addItem(x);
        for(int i=1; i<32; i++) comboDate.addItem(""+i);
        for(int i=2020; i>1999; i--) comboYear.addItem(""+i);

        //Setting the "Location" combobox
        String[] locations = house.getRoomNames();
        for(String x : locations) comboLocation.addItem(x);
        comboLocation.addItem("Outside");

        //Setting "Time" spinners
        hourSpinner.setModel(new SpinnerNumberModel(0.0, 0.0, 23.0, 1));
        minuteSpinner.setModel(new SpinnerNumberModel(0.0, 0.0, 59, 1));
        secondSpinner.setModel(new SpinnerNumberModel(0.0, 0.0, 59, 1));
        outSideTemp.setModel(new SpinnerNumberModel(0,-90,57,1 ));

        updateUsers();

    }

    public void updateUsers(){
        comboUsers.removeAllItems();
        if(Type.getText().equalsIgnoreCase("parent")){
            String[] users = UserManager.getUsernames();
            for (String user : users) {
                comboUsers.addItem(user);
            }
        }
        else{
            comboUsers.addItem(Username.getText());
            addUserButton.setEnabled(false);
        }
    }

    /**
     * Adds all action listeners to attributes of the class.
     */
    public void addActionListeners() {
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddUser("Add User").setVisible(true);

            }
        });
        editUserLocation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setUpEditFrame();
            }
        });
        onOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(onOff.isSelected()) {
                    onOff.setSelected(false);
                    tabbedPane1.setEnabledAt(0, true);
                    tabbedPane1.setEnabledAt(3, true);
                    tabbedPane1.setEnabledAt(1, false);
                    tabbedPane1.setSelectedIndex(0);
                    timer.stop();

                    String currentTime = timeLabel.getText();
                    int[] times =timetest.Breakdowntime(currentTime);
                    hourSpinner.setValue((double)times[2]);
                    minuteSpinner.setValue((double)times[1]);
                    secondSpinner.setValue((double)times[0]);
                }
                else {
                    tabbedPane1.setEnabledAt(1, true);
                    tabbedPane1.setEnabledAt(0, false);
                    tabbedPane1.setEnabledAt(3, false);
                    tabbedPane1.setSelectedIndex(1);
                    onOff.setSelected(true);
                    setUpSimulation();
                    timer.start();
                    int temp = (int)outSideTemp.getValue();
                    outsidetempvalue.setText(temp +"°C");
                }
            }
        });
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s =timeLabel.getText();
                s = timetest.updatetime(s);
                timeLabel.setText(s);
            }
        });
        editUsrButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new EditUserProfile("Edit User Profile",Type.getText(), comboUsers.getSelectedItem().toString(),Username.getText()).setVisible(true);
            }
        });
        LogOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.logoutClicked();
            }
        });
    }

    /**
     * Method that sets all the necessary parameters when simulation starts.
     */
    public void setUpSimulation(){
        String date, day, month, year, hour, minute, second;
        int hourInt, minuteInt, secondInt;

        //Setting up date label
        date = comboDate.getItemAt(comboDate.getSelectedIndex());
        day = comboDay.getItemAt(comboDay.getSelectedIndex());
        month = comboMonth.getItemAt(comboMonth.getSelectedIndex());
        year = comboYear.getItemAt(comboYear.getSelectedIndex());

        dateLabel.setText(day.substring(0, 3) + " " + month.substring(0, 3) + " " + date + " " + year);

        //Setting current location
        currentLocLabel.setText(comboLocation.getItemAt(comboLocation.getSelectedIndex()));
        UserManager.changeUserLocation(Username.getText(),currentLocLabel.getText());
        
        //Setting time
        hourInt = (int)Math.round((double)hourSpinner.getValue());
        minuteInt = (int)Math.round((double)minuteSpinner.getValue());
        secondInt = (int)Math.round((double)secondSpinner.getValue());

        hour = ((hourInt < 10) ? "0"+hourInt : ""+hourInt);
        minute = ((minuteInt < 10) ? "0"+minuteInt : ""+minuteInt);
        second = ((secondInt < 10) ? "0"+secondInt : ""+secondInt);

        timeLabel.setText(hour + ":" + minute + ":" + second);

        setUpSHCItems();
        if (!welcomemessagedisplayed) {
            printToConsole("Welcome to your new Smart home " + Username.getText() + "!");
            welcomemessagedisplayed = true;
        }
    }

    /**
     * Updates the SHC with the items in the room the user is currently located.
     */
    public void setUpSHCItems(){

        //Checking if user is child, guest, or parent to see what items should be displayed in the SHC.
        if(Type.getText().equalsIgnoreCase("Child") || Type.getText().equalsIgnoreCase("Guest")){


            String currentLocation = currentLocLabel.getText();
            String[] roomNames = house.getRoomNames();
            Room currentRoom = null;
            List<SmartObjectType> roomItems;
            List<JCheckBox> openChecks = new ArrayList<JCheckBox>();

            if(currentLocation.equalsIgnoreCase("Outside")){
                listItems.setVisible(false);
                itemsLabel.setText("Items " + currentLocation);
                setUpSHCOpenClose();
                return;
            }else listItems.setVisible(true);

            //Figuring out which room object we are currently in.
            for (int i = 0; i < roomNames.length; i++) {
                if (roomNames[i].equalsIgnoreCase(currentLocation)) {
                    currentRoom = house.rooms[i];
                    break;
                }
            }
            if (currentRoom != null) {

                //Getting all the different item categories in the room and displaying them.
                roomItems = currentRoom.getItemMapKeys();
                SmartObjectType[] roomItemsArr = new SmartObjectType[roomItems.size()];

                for (int i = 0; i < roomItems.size(); i++) {
                    roomItemsArr[i] = roomItems.get(i);
                }

                listItems.setListData(roomItemsArr);

                if (listItems.getFirstVisibleIndex() != -1) {
                    listItems.setSelectedIndex(0);
                }

            }

            //Changing the items label to reflect current location.
            itemsLabel.setText("Items in " + currentLocation);
        }
        else{
            //Getting all the different item categories in the room and displaying them.
            List<SmartObjectType> houseItems = house.getHouseItemsKeys();
            SmartObjectType[] houseItemsArr = new SmartObjectType[houseItems.size()];

            for (int i = 0; i < houseItems.size(); i++) {
                houseItemsArr[i] = houseItems.get(i);
            }

            listItems.setListData(houseItemsArr);

            if (listItems.getFirstVisibleIndex() != -1) {
                listItems.setSelectedIndex(0);
            }

            //Changing the items back to items in case it was changed for child user.
            itemsLabel.setText("Items");


        }

        setUpSHCOpenClose();

    }

    /**
     * Method that sets all the items in open/close that are related to the selected category of items in SHC.
     */
    public void setUpSHCOpenClose(){

        //Checking if user is child, guest, or parent to see what items should be displayed in the SHC.
        if(Type.getText().equalsIgnoreCase("Child") || Type.getText().equalsIgnoreCase("Guest")){
            String currentLocation = currentLocLabel.getText();
            String[] roomNames = house.getRoomNames();
            Room currentRoom = null;

            if(currentLocation.equalsIgnoreCase("Outside")){
                openClosePanel.removeAll();
                return;
            }


            for (int i = 0; i < roomNames.length; i++) {
                if (roomNames[i].equalsIgnoreCase(currentLocation)) {
                    currentRoom = house.rooms[i];
                    break;
                }
            }

            if (currentRoom != null) {
                SmartObjectType selectedItem = listItems.getSelectedValue();
                List<String> items = currentRoom.getItemMapValue(selectedItem);

                openClosePanel.removeAll();
                if (items!= null) {
                    openClosePanel.setLayout(new GridLayout(items.size(), 1));

                    JCheckBox[] itemsArr = new JCheckBox[items.size()];
                    for (int i = 0; i < items.size(); i++) {
                        itemsArr[i] = new JCheckBox(items.get(i));
                        itemsArr[i].setSelected(isObjectOpen(items.get(i)));
                        int index = i;
                        itemsArr[i].addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (!house.openCloseObject(itemsArr[index].getText(), itemsArr[index].isSelected())) {
                                    itemsArr[index].setSelected(!itemsArr[index].isSelected());
                                    printToConsole(itemsArr[index].getText() + " is blocked and cannot be opened/closed.");
                                } else {
                                    if (itemsArr[index].isSelected())
                                        printToConsole(itemsArr[index].getText() + " was opened.");
                                    else printToConsole(itemsArr[index].getText() + " was closed.");
                                }
                            }
                        });
                        openClosePanel.add(itemsArr[i]);
                    }
                }


            }
        }
        else{
            SmartObjectType selectedItem = listItems.getSelectedValue();
            List<String> items = house.getHouseItemValue(selectedItem);

            openClosePanel.removeAll();
            openClosePanel.setLayout(new GridLayout(items.size(), 1));

            JCheckBox[] itemsArr = new JCheckBox[items.size()];
            for(int i=0; i<items.size(); i++){
                itemsArr[i] = new JCheckBox(items.get(i));
                itemsArr[i].setSelected(isObjectOpen(items.get(i)));
                int index = i;
                itemsArr[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(!house.openCloseObject(itemsArr[index].getText(), itemsArr[index].isSelected())){
                            itemsArr[index].setSelected(!itemsArr[index].isSelected());
                            printToConsole(itemsArr[index].getText() + " is blocked and cannot be opened/closed.");
                        }
                        else{
                            if(itemsArr[index].isSelected()) printToConsole(itemsArr[index].getText() + " was opened.");
                            else  printToConsole(itemsArr[index].getText() + " was closed.");
                        }
                    }
                });
                openClosePanel.add(itemsArr[i]);
            }
        }
    }

    /**
     * Methods that sets up the Edit simulation frame.
     */
    public void setUpEditFrame(){
        editFrame.setBounds(xEdit, yEdit, widthEdit, heightEdit);
        editFrame.setUpEditOptions(house.getRoomNames(), house.getHouseItemValue(SmartObjectType.WINDOW), UserManager.getUsernames(),Username.getText());
        editFrame.setVisible(true);
    }

    /**
     * Method that blocks/unblocks a given window
     *
     * @param name String that is the name of the window
     * @param blocked boolean that is the desired state of the window
     */
    public void blockWindow(String name, boolean blocked){
        house.blockWindow(name, blocked);
        if(blocked) printToConsole(name + " status was changed to \"blocked\"");
        else printToConsole(name + " status was changed to \"unblocked\"");

    }

    /**
     * Checks if window is blocked by an object.
     *
     * @param name a String containing the name of the window being checked.
     * @return boolean true if window is blocked false otherwise.
     */
    public boolean isWindowBlocked(String name){
        for(Room room : house.getRoomsList()){
            for(Smartobj obj : room.getSmartObjects()){
                if(obj.getName().equalsIgnoreCase(name) && obj.getType() == SmartObjectType.WINDOW){
                    Window window = (Window)obj;
                    return window.isBlocked();
                }
            }
        }
        return false;
    }

    /**
     * Checks if object is open or closed
     *
     * @param name String name of the object
     * @return boolean true if item is open false otherwise
     */
    public boolean isObjectOpen(String name){
        for(Room room : house.getRoomsList()){
            for(Smartobj obj : room.getSmartObjects()){
                if(obj.getName().equalsIgnoreCase(name)){
                    switch(obj.getType()){
                        case WINDOW:
                            Window object = (Window)obj;
                            return object.isOpen();
                    }
                }
            }
        }
        return false;
    }

    /**
     * Prints updates to the console inside the Dashboard.
     *
     * @param text String that is the text to be printed on the console.
     */
    public void printToConsole(String text){
        String[] currentTime = timeLabel.getText().split(":");
        String current_Time_Formatted = "";

        if(onOff.isSelected()){
            current_Time_Formatted = "["+currentTime[0] + ":" + currentTime[1]+"]:";
        }

        consoleText.append("\n" + current_Time_Formatted + text);
        consoleText.setRows(consoleText.getRows()+ 1);
    }
}
