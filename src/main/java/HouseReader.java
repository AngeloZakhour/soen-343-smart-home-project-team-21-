import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
reads a house layout file (no error checking) and puts the information into a house class, its corresponding room arrays and
the rooms corresponding smartobj array

@version 0.1
@since 2020-09-30
 */
public class HouseReader {
    /**
     * loads information from a house.txt file and converts it into a house object
     * @param filename the file path to the houselayout txt file
     * @return House returns a house object loaded with the rooms and smartobj
     */
    public static House readAndLoadHouse(String filename) {

        //try, catch and lines 12,13,14,15,18-20 were taken from https://www.w3schools.com/java/java_files_read.asp
        //these variables are storages for the objects
        House mainhouse = null;
        Room[] listofrooms;
        Smartobj[] smartobjByRoom;
        //temporary string for holding return values
        String s = null;
        //temp variables for storing values to be sent to constructors
        String temphousename = null;
        int tempnumrooms = 0;
        String temproomname = null;
        double temproomwidth = 0.0;
        double temproomtempreture = 0.0;
        double temproomlength = 0.0;
        int tempnumsmartobj;
        String smartobjType = null;
        String smartobjName = null;
        int smartobjtypenum = 0;
        try {
            File houseFile = new File(filename);
            Scanner lineReader = new Scanner(houseFile);

            if (lineReader.hasNextLine()){
                s = lineReader.nextLine();
                s = s.substring(s.indexOf(':')+2);
                temphousename = s;
                s = lineReader.nextLine();
                s = s.substring(s.indexOf(':')+2);

                listofrooms = new Room[Integer.parseInt(s)];

                for (int i= 0; i < listofrooms.length; i++){
                    s = lineReader.nextLine();
                    temproomname = s.substring(s.indexOf(':')+2).replaceAll("\\s+","").toUpperCase();
                    temproomname = LocationType.valueOf(temproomname).toString();
                    s = lineReader.nextLine();
                    temproomtempreture = Double.parseDouble(s.substring(s.indexOf(':')+2));
                    s = lineReader.nextLine();
                    temproomwidth = Double.parseDouble(s.substring(s.indexOf(':')+2));
                    s = lineReader.nextLine();
                    temproomlength = Double.parseDouble(s.substring(s.indexOf(':')+2));
                    s = lineReader.nextLine();

                    smartobjByRoom = new Smartobj[Integer.parseInt(s.substring(s.indexOf(':')+2))];

                    for (int j = 0; j< smartobjByRoom.length; j++){
                        s = lineReader.nextLine();
                        smartobjType = s.substring(s.indexOf(':')+2);
                        s = lineReader.nextLine();
                        smartobjName = s.substring(s.indexOf(':')+2);
                        //to be exapanded with more types and if statements
                        smartobjByRoom[j] = smartobjType.equalsIgnoreCase("window")? new Window(smartobjName) : null;

                        if(smartobjByRoom == null) {
                            System.out.println("an error has occured in assigning the type of the smart object " + smartobjName );
                        }
                    }
                    listofrooms[i] = new Room(smartobjByRoom,temproomname,temproomtempreture,temproomwidth,temproomlength);
                }
                mainhouse = new House(listofrooms,temphousename);
            }

            lineReader.close();

            // This is commented out for now
            //loadHouseLayout(mainhouse);
        }
        catch (FileNotFoundException e) {
            System.out.println("an error has occured, file may be missing or misspelled");
            e.printStackTrace();
            return null;
        }
        return  mainhouse;
    }

    public static void loadHouseLayout(House mainHouse) {
        // Created frame just to test we would want this to appear in the JPanel HouseLayout
        JFrame frame = new JFrame();
        frame.add(new DynamicLayout(mainHouse.getRoomsList()));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}



