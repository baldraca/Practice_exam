import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.text.ParseException;

/**
 *  LTU Airport AD Management System
 *  Practice Exam
 *
 @author Jonas Wennberg (wenjon-3)
 */

public class Main {
    // Constants
    public static final int MENU_ITEM_1 = 1;
    public static final int MENU_ITEM_2 = 2;
    public static final int MENU_ITEM_3 = 3;
    public static final int MENU_ITEM_4 = 4;
    public static final int MENU_ITEM_5 = 5;
    public static final int MENU_ITEM_Q = -1;
    public static final int ZERO = 0;
    public static final int MAX_MINUTES = 59;
    public static final int MAX_HOURS = 23;
    public static final int TIME_IS_AFTER = 1;
    public static final int ROWS = 100;
    public static final int COLUMNS = 4;
    public static final int FLIGHT_ID = 0;
    public static final int AIRPORT_ID = 1;
    public static final int STA_ID = 2;
    public static final int ATA_ID = 3;
    public static final int STD_ID = 2;
    public static final int ATD_ID = 3;

    private static Scanner userInputScanner = new Scanner(System.in);

    /**
     * Main method taking input arguments from user
     * @param args - Get arguments from cmd prompt
     */
    public static void main(final String[] args) throws ParseException {
        // Variables
        String[][] arrivals = new String[ROWS][COLUMNS];
        String[][] departures = new String[ROWS][COLUMNS];
        String flightNr = "";
        String airport = "";
        String arrivalDate = "";
        String departureDate = "";

        // Run program with menu
        while (true) {
            int userSelection = menu();

            switch (userSelection) {
                case MENU_ITEM_1:
                    System.out.print("Enter flight number: ");
                    flightNr = inputFlightNr();
                    System.out.print("Enter airport of origin: ");
                    airport = inputAirport();
                    System.out.print("Enter scheduled time of arrival: ");
                    arrivalDate = inputDate();
                    System.out.print("");

                    arrivals = registerScheduledArrival(flightNr, airport, arrivalDate, arrivals, departures);

                    break;

                case MENU_ITEM_2:
                    System.out.print("Enter flight number: ");
                    flightNr = inputFlightNr();
                    System.out.print("Enter destination airport: ");
                    airport = inputAirport();
                    System.out.print("Enter scheduled time of departure: ");
                    departureDate = inputDate();
                    System.out.print("");

                    // send in arrays as well
                    departures = registerScheduledDeparture(flightNr, airport, departureDate, arrivals, departures);

                    break;

                case MENU_ITEM_3:
                    System.out.print("Enter flight number for arriving flight: ");
                    flightNr = inputFlightNr();
                    System.out.print("Enter actual time of arrival: ");
                    arrivalDate = inputDate();

                    arrivals = registerActualArrival(flightNr, arrivalDate, arrivals);

                    break;

                case MENU_ITEM_4:
                    System.out.print("Enter flight number for departure flight: ");
                    flightNr = inputFlightNr();
                    System.out.print("Enter actual time of departure: ");
                    departureDate = inputDate();

                    departures = registerActualDeparture(flightNr, departureDate, departures);

                    break;

                case MENU_ITEM_5:
                    PrintOperationsByTime(arrivals, departures);
                    break;

                case MENU_ITEM_Q:
                    System.out.println("Terminating...");
                    return;

                default:
                    System.out.println("Invalid selection. Only integers is allowed!");
            }
        }
    }

    /**
     * Menu method
     * Printing menu for the user and returns choice
     * @return menuChoice
     */
    public static int menu() {

        //Printing menu
        System.out.println("----------------------------------");
        System.out.println("# LTU Airport AD Management System");
        System.out.println("----------------------------------");
        System.out.println("1. Register the scheduled arrival");
        System.out.println("2. Register the scheduled departure");
        System.out.println("3. Register the actual arrival of a flight");
        System.out.println("4. Register the actual departure of a flight");
        System.out.println("5. Print operations summary");
        System.out.println("q. End program");
        System.out.print("Enter your option: ");
        return input();
    }
    /**
     * Input method
     * Printing menu for the user
     * @return userInput
     */
    public static int input() {

        // Read user input
        while (true) {
            if (userInputScanner.hasNextInt()) {
                return userInputScanner.nextInt();
            } else {
                String inString = userInputScanner.next();
                if (inString.equalsIgnoreCase("q")) {
                    return MENU_ITEM_Q;
                }  else {
                    System.out.println("Invalid input. Only integers or \"q\" is allowed!");
                }
            }
        }
    }

    /**
     * inputFlightNr method
     * Input method and validation of flightNumber
     * @return inputFlightNumber
     */
    public static String inputFlightNr() {
        String inputFlightNumber;

        while (true) {
            inputFlightNumber = userInputScanner.next();

            //Validate flightNr has 2 cap letters and 3 integers
            if (!isFlightNrValid(inputFlightNumber)) {
                System.out.println("Invalid input, FlightNr should be with 2 capital letters and 3 integers (ex SK100)");
                continue;
            }
            break;
        }

        return inputFlightNumber;
    }
    /**
     * isFlightNrValid method
     * Validate FlightNr is in right format
     * @param flightNr - String of the flight
     * @return true/false
     */
    public static boolean isFlightNrValid(String flightNr) {
        return flightNr.matches("[A-Z][A-Z][0-9][0-9][0-9]");
    }

    /**
     * inputDate method
     * Input method for date and validation
     * @return userInput
     */
    public static String inputDate() {
        String inputDate;
        while (true) {
            inputDate = userInputScanner.next();

            //Validate time is a valid time
            if (!isDateFormatValid(inputDate)) {
                System.out.println("Invalid input, Time should be in HH:MM format");
                continue;
            }
            if (!isTimeValid(inputDate)) {
                System.out.println("Invalid input, Time should be in HH:MM format");
                continue;
            }
            break;
        }
        // Validate time is HH:MM
        isTimeValid(inputDate);

        return inputDate;
    }

    /**
     * isTimeValid method
     * Validate if Time is in actual time format
     * @param time - String of the time
     * @return true/false
     */
    private static boolean isTimeValid(String time) {

        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3, 5));

        if(hour <= MAX_HOURS && minute <= MAX_MINUTES && hour >= ZERO && minute >= ZERO ) {
            return true;
        }
        return false;
    }

    /**
     * isDateFormatValid method
     * Validate if Time is in actual time format
     * @param time - String of the time
     * @return true/false
     */
    private static boolean isDateFormatValid(String time) {

        return time.matches("[0-9][0-9]:[0-9][0-9]");
    }

    /**
     * inputAirport method
     * Input method for Airport
     * @return airport
     */
    public static String inputAirport() {

        String airport = userInputScanner.next();

        return airport;
    }

    /**
     * registerScheduledArrival method
     * Register the scheduled arrival
     * @param flightNr - Flightnumber of the airplane
     * @param airportOfOrigin - Airport of origin for the plane
     * @param scheduledTimeArrival - Scheduled time of arrival
     * @param arrivals - Array of arrivals
     * @param departures - Array of departures
     * @return userInput
     */
    public static String[][] registerScheduledArrival(String flightNr, String airportOfOrigin, String scheduledTimeArrival, final String[][] arrivals, final String[][] departures) {
        String[][] workingArrivals = arrivals;
        String[][] workingDepartures = departures;
        // Validate

        // Check that flightNr does not exist in arrivals
        for (int i = 0; i < workingArrivals.length; i++) {
            if (flightNr.equals(workingArrivals[i][FLIGHT_ID])) {
                System.out.printf("%s is already in the Arrivals database.%n", flightNr);
                return workingArrivals;
            }
        }
        // Check that flightNr is not on departure
        for (int i = 0; i < workingDepartures.length; i++) {
            if (flightNr.equals(workingDepartures[i][FLIGHT_ID])) {
                System.out.printf("%s is already in the Departures database. Cant be both!%n", flightNr);
                return workingArrivals;
            }

        }

        // Register flight in arrival array
        for (int j = 0; j < workingArrivals.length; j++) {
            if (workingArrivals[j][FLIGHT_ID] == null) {
                workingArrivals[j][FLIGHT_ID] = flightNr;
                workingArrivals[j][AIRPORT_ID] = airportOfOrigin;
                workingArrivals[j][STA_ID] = scheduledTimeArrival;
                workingArrivals[j][ATA_ID] = "";
                break;
            }
        }

        // print
        System.out.printf("Arrival of flight %s from %s is scheduled for %s. %n%n", flightNr, airportOfOrigin, scheduledTimeArrival );

        return workingArrivals;
    }

    /**
     * registerScheduledDeparture method
     * Register the scheduled arrival
     * @param flightNr - Flightnumber of the airplane
     * @param airportOfOrigin - Airport of origin for the plane
     * @param scheduledTimeDeparture - Scheduled time of departure
     * @param arrivals - Array of arrivals
     * @param departures - Array of departures
     * @return userInput
     */
    public static String[][] registerScheduledDeparture(String flightNr, String airportOfOrigin, String scheduledTimeDeparture, final String[][] arrivals, final String[][] departures) {
        String[][] workingArrivals = arrivals;
        String[][] workingDepartures = departures;

        // Validate

        // Check that flightNr does not exist in arrivals
        for (int i = 0; i < workingArrivals.length; i++) {
            if (flightNr.equals(workingArrivals[i][FLIGHT_ID])) {
                System.out.printf("%s is already in the Arrivals database. Cant be both!%n", flightNr);
                return workingDepartures;
            }
        }
        // Check that flightNr is not on departure
        for (int i = 0; i < workingDepartures.length; i++) {
            if (flightNr.equals(workingDepartures[i][FLIGHT_ID])) {
                System.out.printf("%s is already in the Departures database.%n", flightNr);
                return workingDepartures;
            }
        }

        // Register flight in departure array
        for (int j = 0; j < workingDepartures.length; j++) {
            if (workingDepartures[j][FLIGHT_ID] == null) {
                workingDepartures[j][FLIGHT_ID] = flightNr;
                workingDepartures[j][AIRPORT_ID] = airportOfOrigin;
                workingDepartures[j][STD_ID] = scheduledTimeDeparture;
                workingDepartures[j][ATD_ID] = "";
                break;
            }
        }


        // print
        System.out.printf("Departure of flight %s from %s is scheduled for %s. %n%n", flightNr, airportOfOrigin, scheduledTimeDeparture );

        return workingDepartures;
    }

    /**
     * registerActualArrival method
     * Register the actual arrival
     * @param flightNr - Flightnumber of the airplane
     * @param actualArrival - Scheduled time of departure
     * @param arrivals - Array of arrivals
     * @return userInput
     */
    public static String[][] registerActualArrival(String flightNr, String actualArrival, final String[][] arrivals) {
        String[][] workingArrivals = arrivals;
        boolean flightExists = false;

        // Validate if flight exists and register time if it does. Otherwise print error and go back to menu.

        for (int i = 0; i < workingArrivals.length; i++) {
            if (flightNr.equals(workingArrivals[i][FLIGHT_ID])) {
                workingArrivals[i][ATA_ID] = actualArrival;
                System.out.printf("Flight %s from %s has arrived at %s.%n", flightNr, workingArrivals[i][AIRPORT_ID], actualArrival);
                flightExists = true;
                break;
            }
        }
        if (!flightExists) {
            System.out.printf("Flight %s does not exist. Can not set an actual arrival time.%n", flightNr);
        }

        return workingArrivals;
    }

    /**
     * registerActualDeparture method
     * Register the actual departure
     * @param flightNr - Flightnumber of the airplane
     * @param actualDeparture - Scheduled time of departure
     * @param departure - Array of departures
     * @return userInput
     */
    public static String[][] registerActualDeparture(String flightNr, String actualDeparture, final String[][] departure) {
        String[][] workingDepartures = departure;
        boolean flightExists = false;

        // Validate if flight exists and register time if it does. Otherwise print error and go back to menu.

        for (int i = 0; i < workingDepartures.length; i++) {
            if (flightNr.equals(workingDepartures[i][FLIGHT_ID])) {
                workingDepartures[i][ATD_ID] = actualDeparture;
                System.out.printf("Flight %s to %s has departured at %s.%n", flightNr, workingDepartures[i][AIRPORT_ID], actualDeparture);
                flightExists = true;
                break;
            }
        }
        if (!flightExists) {
            System.out.printf("Flight %s does not exist. Can not set an actual departure time.%n", flightNr);
        }

        return workingDepartures;
    }

    /**
     * printOperations method
     * This method prints a summary of all operations
     * @param arrivals - Array of arrivals
     * @param departures - Array of departures
     */
    public static void printOperations(final String[][] arrivals, final String[][] departures) throws ParseException {
        // Print Arrivals
        System.out.println("");
        System.out.println("Arrivals:");
        System.out.printf("%-15s %-15s %-15s %-15s%n", "Flight", "From", "STA", "ATA");
        for (int i = 0; i < arrivals.length; i++) {
            if (arrivals[i][FLIGHT_ID] != null) {
                System.out.printf("%-15s %-15s %-15s %-15s%n", arrivals[i][FLIGHT_ID], arrivals[i][AIRPORT_ID], arrivals[i][STA_ID], arrivals[i][ATA_ID]);
            } else {
                break;
            }
        }

        System.out.println("");

        // Print Departures
        System.out.println("Departures:");
        System.out.printf("%-15s %-15s %-15s %-15s%n", "Flight", "To", "STD", "ATD");
        for (int i = 0; i < departures.length; i++) {
            if (departures[i][FLIGHT_ID] != null) {
                System.out.printf("%-15s %-15s %-15s %-15s%n", departures[i][FLIGHT_ID], departures[i][AIRPORT_ID], departures[i][STD_ID], departures[i][ATD_ID]);
            } else {
                break;
            }
        }
        System.out.println("");

        // calculate total flights and print
        int amountOfFlights = getNrOfFlights(arrivals) + getNrOfFlights(departures);
        System.out.printf("Total number of flights: %d %n", amountOfFlights);

        // Calculate amount of delayed flights and print
        int amountOfDelays = calculateAmountOfDelays(arrivals, departures);
        System.out.printf("Total number of delayed flights: %d %n", amountOfDelays);
    }

    /**
     * PrintOperationsByTime method
     * This method prints a summary of all operations with order of time
     * @param arrivals - Array of arrivals
     * @param departures - Array of departures
     */
    public static void PrintOperationsByTime(String[][] arrivals, String[][] departures) throws ParseException {
        //Copy of the original array
        String[][] tempArrivals = new String[ROWS][COLUMNS];
        String[][] tempDepartures = new String[ROWS][COLUMNS];

        // Get amount of arrivals and departures
        int amountOfArrivals = getNrOfFlights(arrivals);
        int amountOfDepartures = getNrOfFlights(departures);

        // Copy available flights to tmp arrays
        System.arraycopy(arrivals, 0, tempArrivals, 0, amountOfArrivals);
        System.arraycopy(departures, 0, tempDepartures, 0, amountOfDepartures);

        Date date1;
        Date date2;

        // Sort arrivals on STA time
        for(int i = 0; i < amountOfArrivals; i++) {
            for(int j = 0; j < amountOfArrivals - 1; j++) {
                date1 = new SimpleDateFormat("HH:MM").parse(tempArrivals[j][STA_ID]);
                date2 = new SimpleDateFormat("HH:MM").parse(tempArrivals[j + 1][STA_ID]);

                if(date1.compareTo(date2) > 0) {
                    String[] temp = tempArrivals[j];
                    tempArrivals[j] = tempArrivals[j + 1];
                    tempArrivals[j + 1] = temp;
                }
            }
        }
        // Sort departures on STD time
        for(int i = 0; i < amountOfDepartures; i++) {
            for(int j = 0; j < amountOfDepartures - 1; j++) {
                date1 = new SimpleDateFormat("HH:MM").parse(tempDepartures[j][STA_ID]);
                date2 = new SimpleDateFormat("HH:MM").parse(tempDepartures[j + 1][STA_ID]);

                if(date1.compareTo(date2) > 0) {
                    String[] temp = tempDepartures[j];
                    tempDepartures[j] = tempDepartures[j + 1];
                    tempDepartures[j + 1] = temp;
                }
            }
        }

        printOperations(tempArrivals, tempDepartures);
    }

    /**
     * getNrOfFlights method
     * This method prints a summary of all operations with order of time
     * @param database - Array of flight-database
     * @return counter - amount of flights in database
     */
    public static int getNrOfFlights(String[][] database) {
        int counter = 0;
        // Count the amount of available fligths in Database
        for (int i = 0; i < database.length; i++) {
            if (database[i][FLIGHT_ID] != null) {
                counter++;
            }
        }
        return counter;
    }


    /**
     * calculateAmountOfDelays method
     * This method prints a summary of all operations with order of time
     * @param arrivals - Array of arrivals
     * @param departures - Array of departures
     * @return amountOfDelays - amount of delayed flights
     */
    public static int calculateAmountOfDelays(String[][] arrivals, String[][] departures) throws ParseException {

        Date date1;
        Date date2;

        int amountOfDelays = 0;

        int amountOfArrivals = getNrOfFlights(arrivals);
        int amountOfDepartures = getNrOfFlights(departures);

        //Look for delays in Arrivals db
        for (int i = 0; i < amountOfArrivals; i++) {
            if (!arrivals[i][ATA_ID].equals("")) {
                date1 = new SimpleDateFormat("HH:MM").parse(arrivals[i][STA_ID]);
                date2 = new SimpleDateFormat("HH:MM").parse(arrivals[i][ATA_ID]);

                if (date2.compareTo(date1) == TIME_IS_AFTER) {
                    amountOfDelays++;
                }
            }
        }
        // Look for delays in Departures db
        for (int i = 0; i < amountOfDepartures; i++) {
            if (!departures[i][ATD_ID].equals("")) {
                date1 = new SimpleDateFormat("HH:MM").parse(departures[i][STD_ID]);
                date2 = new SimpleDateFormat("HH:MM").parse(departures[i][ATD_ID]);
                if (date2.compareTo(date1) == TIME_IS_AFTER) {
                    amountOfDelays++;
                }
            }
        }
        return amountOfDelays;
    }

}