import java.text.ParseException;
import java.util.Scanner;

public class Main {
    public static Scanner userInput = new Scanner(System.in);

    // Constants
    static final int NUM_ARRIVALS = 10; // There can be maximum 10 arrivals
    static final int NUM_DEPARTURES = 10; // There can be maximum 10 departures


    public static void main(String[] args) throws ParseException {

        /*
         * arrivals is a 2D array that holds the following information:
         *
         * The first column holds the flight number,
         * The second column holds the origin
         * The third column holds the STA
         * The fourth column holds the ATA
         */
        String[][] arrivals = new String[NUM_ARRIVALS][4];

        /*
         * departures is a 2D array that holds the following information:
         *
         * The first column holds the flight number,
         * The second column holds the destination
         * The third column holds the STD
         * The fourth column holds the ATD
         */
        String[][] departures = new String[NUM_DEPARTURES][4];

        //Track the number of arrivals and departures
        int numArrivals = 0;
        int numDepartures = 0;

        loadTestData(arrivals, departures);

        numArrivals = 3;
        numDepartures = 3;

        while (true) {
            switch (menu()) {
                case 1:
                    System.out.println("Register the scheduled arrival");
                    //if the arrival was added successfully, increment courseCount
                    if (registerScheduledArrival(arrivals, departures, numArrivals, numDepartures)) {
                        numArrivals++;
                    }
                    break;
                case 2:
                    System.out.println("Register the scheduled departure");
                    //if the departure was added successfully, increment courseCount
                    if (registerScheduledDeparture(arrivals, departures, numArrivals, numDepartures)) {
                        numDepartures++;
                    }
                    break;

                case 3:
                    System.out.println("Register the actual arrival of a flight");
                    registerActualArrival(arrivals, numArrivals);
                    break;

                case 4:
                    System.out.println("Register the actual departure of a flight");
                    registerActualDeparture(departures, numDepartures);
                    break;

                case 5:
                    System.out.println("Print operations summary");
                    operationSummary(arrivals, departures, numArrivals, numDepartures);
                    break;

                case -1:
                    System.exit(0);
            }
        }
    }

    /*
     * This method registers the actual departure of a flight
     *
     * @param departures is the 2D array that holds the departures
     * @param numDepartures is the number of departures
     *
     * @return void
     */
    private static void registerActualDeparture(String[][] departures, int numDepartures) {
        // Ask user for flight number
        System.out.println("Enter the flight number of the departing flight: ");
        String flightNumber = userInput.nextLine();

        //check if flight number exists in arrivals
        int index = flightNumberExists(flightNumber, departures, numDepartures);
        if (index == -1) {
            System.out.println("Flight number does not exist");
            return;
        }

        //Ask user for the actual arrival time
        System.out.println("Enter the actual time of departure: ");
        String actualArrivalTime = userInput.nextLine();

        // check if the time is in valid format
        if (!isFlightTimeFormatValid(actualArrivalTime)) {
            System.out.println("Invalid time format");
            return;
        }

        departures[index][3] = actualArrivalTime;
    }

    /*
     * This method registers the actual arrival of a flight
     *
     * @param arrivals is the 2D array that holds the arrivals
     * @param numArrivals is the number of arrivals
     *
     * @return void
     */
    private static void registerActualArrival(String[][] arrivals, int numArrivals) {
        // Ask user for flight number
        System.out.println("Enter the flight number of the arriving flight: ");
        String flightNumber = userInput.nextLine();

        //check if flight number exists in arrivals
        int index = flightNumberExists(flightNumber, arrivals, numArrivals);
        if (index == -1) {
            System.out.println("Flight number does not exist");
            return;
        }

        //Ask user for the actual arrival time
        System.out.println("Enter the actual time of arrival: ");
        String actualArrivalTime = userInput.nextLine();

        // check if the time is in valid format
        if (!isFlightTimeFormatValid(actualArrivalTime)) {
            System.out.println("Invalid time format");
            return;
        }

        arrivals[index][3] = actualArrivalTime;

    }

    private static void loadTestData(String[][] arrivals, String[][] departures) {
        arrivals[0][0] = "SK137";
        arrivals[0][1] = "Stockholm";
        arrivals[0][2] = "10:40";
        arrivals[0][3] = "10:57";

        arrivals[1][0] = "LH130";
        arrivals[1][1] = "Munich";
        arrivals[1][2] = "12:45";
        arrivals[1][3] = "12:40";

        arrivals[2][0] = "DY099";
        arrivals[2][1] = "Oslo";
        arrivals[2][2] = "12:30";
        arrivals[2][3] = "";

        departures[0][0] = "EK345";
        departures[0][1] = "Dubai";
        departures[0][2] = "07:35";
        departures[0][3] = "08:20";

        departures[1][0] = "MH020";
        departures[1][1] = "Kuala Lumpur";
        departures[1][2] = "14:25";
        departures[1][3] = "";

        departures[2][0] = "SQ340";
        departures[2][1] = "Singapore";
        departures[2][2] = "14:00";
        departures[2][3] = "13:52";

    }

    /*
     * This method prints the operational summary of the airport arrivals and departures for the day
     *
     * @param arrivals is a 2D array that holds the arrival information
     * @param departures is a 2D array that holds the departure information
     * @param numArrivals is the number of arrivals
     * @param numDepartures is the number of departures
     *
     * @return void
     */
    private static void operationSummary(String[][] arrivals, String[][] departures, int numArrivals, int numDepartures) {
        System.out.println("LTU Airport Operations Summary");
        System.out.println(); //empty line for esthetics

        System.out.println("Arrivals:");
        printOrderedSummary(arrivals, numArrivals, true);
        System.out.println(); //empty line for esthetics


        System.out.println("Departures:");
        printOrderedSummary(departures, numDepartures, false);
        System.out.println(); //empty line for esthetics

        System.out.println("Total number of flights: " + (numArrivals + numDepartures));
        System.out.println("Total number of delayed flights: " + (countDelayedFlights(arrivals, numArrivals) + countDelayedFlights(departures, numDepartures)));

    }

    /*
     * This method calculate number of delayed flights, it is used in operationSummary method.
     * It is generic method that can be used for both arrivals and departures
     *
     * @param fligtDetails is a 2D array that holds the flight information
     * @param numOfFlights is the number of flights in the array
     *
     * @return int number of delayed flights
     */
    private static int countDelayedFlights(String[][] fligtDetails, int numOfFlights) {
        int count = 0;
        //loop through all flights and count the delayed ones, index 3 is more than index 2
        for (int i = 0; i < numOfFlights; i++) {
            if (fligtDetails[i][3].compareTo(fligtDetails[i][2]) > 0) {
                count++;
            }
        }
        return count;
    }

    /*
     * This method prints the ordered summary of the airport. It is a generic method that can be used for both arrivals and departures
     *
     * @param flightDetails is a 2D array that holds the flight information
     * @param numOfFlights is the number of flights in the array
     *
     * @return void
     */
    private static void printOrderedSummary(String[][] flightDetails, int numOfFlights, boolean isArrival) {

        String[][] tempFlightDetails = new String[numOfFlights][4];
        System.arraycopy(flightDetails, 0, tempFlightDetails, 0, numOfFlights);

        //buublesort index 2
        for (int i = 0; i < numOfFlights; i++) {
            for (int j = 0; j < numOfFlights - 1; j++) {
                if (tempFlightDetails[j][2].compareTo(tempFlightDetails[j + 1][2]) > 0) {
                    String[] temp = tempFlightDetails[j];
                    tempFlightDetails[j] = tempFlightDetails[j + 1];
                    tempFlightDetails[j + 1] = temp;
                }
            }
        }

        if(isArrival) {
            System.out.printf("%-10s %-15s %-10s %-10s %n", "Flight", "From", "STA", "ATA");
        }
        else {
            System.out.printf("%-10s %-15s %-10s %-10s %n", "Flight", "To", "STD", "ATD");
        }
        //Actual order
        for (int i = 0; i < numOfFlights; i++) {
            System.out.printf("%-10s %-15s %-10s %-10s %n", flightDetails[i][0], flightDetails[i][1], flightDetails[i][2], flightDetails[i][3]);
        }
        System.out.println(); // newline for astetics

        for (int i = 0; i < numOfFlights; i++) {
            System.out.printf("%-10s %-15s %-10s %-10s %n", tempFlightDetails[i][0], tempFlightDetails[i][1], tempFlightDetails[i][2], tempFlightDetails[i][3]);
        }
    }

    /*
     * Thus method registers the scheduled departure of a flight
     *
     * @param arrivals is a 2D array that holds the arrival information
     * @param departures is a 2D array that holds the departure information to the which the new departure is added
     * @param numArrivals is the number of arrivals currently in the arrivals array
     * @param numDepartures is the number of departures currently in the departures array
     *
     * @return boolean false if the departure was not added successfully (validation failed), true if the departure was added successfully
     */
    private static boolean registerScheduledDeparture(String[][] arrivals, String[][] departures, int numArrivals, int numDepartures) {
        System.out.print("Enter flight number: ");
        String flightNumber = userInput.nextLine();

        if (!isFlightNumberFormatValid(flightNumber)) {
            System.out.println("Wrong format on flight number");
            return false;
        }

        if (flightNumberExists(flightNumber, arrivals, numArrivals) != -1 || flightNumberExists(flightNumber, departures, numDepartures) != -1) {
            System.out.println("Flight number already exists");
            return false;
        }

        System.out.print("Enter Flight destination: ");
        String flightDestination = userInput.nextLine();

        System.out.print("Enter Scheduled Departure Time: ");
        String std = userInput.nextLine();

        if (!isFlightTimeFormatValid(std)) {
            System.out.println("Wrong format on flight time");
            return false;
        }

        departures[numDepartures][0] = flightNumber;
        departures[numDepartures][1] = flightDestination;
        departures[numDepartures][2] = std;
        departures[numDepartures][3] = "";

        return true;
    }

    /*
     * This method checks if a flight time format is of form HH:MM, where HH is the hour and MM is the minute
     *
     * @param flightTime is the flight time to be checked
     *
     * @return boolean true if the flight time is of form HH:MM, false otherwise
     */
    private static boolean isFlightTimeFormatValid(String flightTime) {
        return flightTime.length() == 5 &&
                Character.isDigit(flightTime.charAt(0)) &&
                Character.isDigit(flightTime.charAt(1)) &&
                flightTime.charAt(2) == ':' &&
                Character.isDigit(flightTime.charAt(3)) &&
                Character.isDigit(flightTime.charAt(4));
    }

    /*
     * Thus method registers the scheduled arrival of a flight
     *
     * @param arrivals is a 2D array that holds the arrival information to the which the new arrival is added
     * @param departures is a 2D array that holds the departure information
     * @param numArrivals is the number of arrivals currently in the arrivals array
     * @param numDepartures is the number of departures currently in the departures array
     *
     * @return boolean false if the arrival was not added successfully (validation failed), true if the arrival was added successfully
     */
    private static boolean registerScheduledArrival(String[][] arrivals, String[][] departures, int numArrivals, int numDepartures) {

        System.out.print("Enter flight number: ");
        String flightNumber = userInput.nextLine();

        if (!isFlightNumberFormatValid(flightNumber)) {
            System.out.println("Wrong format on flight number");
            return false;
        }

        if (flightNumberExists(flightNumber, arrivals, numArrivals) != -1 || flightNumberExists(flightNumber, departures, numDepartures) != -1) {
            System.out.println("Flight number already exists");
            return false;
        }
        System.out.print("Enter flight origin: ");
        String flightOrigin = userInput.nextLine();

        System.out.print("Enter Scheduled Arrival Time: ");
        String sta = userInput.nextLine();

        if (!isFlightTimeFormatValid(sta)) {
            System.out.println("Wrong format on flight time");
            return false;
        }

        //HH >= 00 and <= 23
        //MM

        arrivals[numArrivals][0] = flightNumber;
        arrivals[numArrivals][1] = flightOrigin;
        arrivals[numArrivals][2] = sta;
        arrivals[numArrivals][3] = "";

        return true;

    }

    /*
     * This method checks if a flight number exists in a 2D array of flight details, generic function that can check both arrivals and departures arrays
     *
     * @param flightNumber is the flight number to be checked
     * @param flightDetails is the 2D array of flight details to be checked
     * @param numFlights is the number of flights currently in the flightDetails array
     *
     * @return int index in the array if the flight number exists in the flightDetails array, -1 otherwise
     */
    private static int flightNumberExists(String flightNumber, String[][] flightDetails, int numFlights) {
        int flightNumberIndex = -1;

        for (int i = 0; i < numFlights; i++) {
            if (flightDetails[i][0].equals(flightNumber)) {
                flightNumberIndex = i;
                break;
            }
        }

        return flightNumberIndex;
    }

    /*
     * This method checks if a flight number format is of form XX999, where X is a capital letter and 9 is a digit
     *
     * @param flightNumber is the flight number to be checked
     *
     * @return boolean true if the flight number is of form XX999, false otherwise
     */
    private static boolean isFlightNumberFormatValid(String flightNumber) {
        return flightNumber.length() == 5 &&
                Character.isUpperCase(flightNumber.charAt(0)) &&
                Character.isUpperCase(flightNumber.charAt(1)) &&
                Character.isDigit(flightNumber.charAt(2)) &&
                Character.isDigit(flightNumber.charAt(3)) &&
                Character.isDigit(flightNumber.charAt(4));
    }

    /*
     * This method prints the menu and asks the user to choose an option
     *
     * @return int the option chosen by the user
     */
    public static int menu() {
        System.out.print("----------------------------------\n# LTU Airport AD Management System\n----------------------------------\n");
        System.out.println("1. Register the scheduled arrival");
        System.out.println("2. Register the scheduled departure");
        System.out.println("3. Register the actual arrival of a flight");
        System.out.println("4. Register the actual departure of a flight");
        System.out.println("5. Print operations summary");
        System.out.println("q. End program");
        System.out.print("Enter your option: ");

        return input();

    }

    /*
     * This method asks the user to input a number
     *
     * @return int the number input by the user
     */
    public static int input() {
        int number = 0;

        while (true) {
            if (userInput.hasNextInt()) {
                String temp = userInput.nextLine();
                number = Math.abs(Integer.parseInt(temp));
                if (number > 0) {
                    break;
                }
            } else if (userInput.hasNext()) {
                String inString = userInput.nextLine();
                // Q and q
                if (inString.equalsIgnoreCase("q")) {
                    number = -1;
                    break;
                }
            }
        }
        return number;
    }
}