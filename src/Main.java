import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation Class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private int numberOfRooms;

    public Reservation(String reservationId, String guestName, String roomType, int numberOfRooms) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.numberOfRooms = numberOfRooms;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Rooms: " + numberOfRooms;
    }
}

// Validator Class (Fail-Fast Validation)
class InvalidBookingValidator {

    private static final Set<String> VALID_ROOM_TYPES =
            new HashSet<>(Arrays.asList("Single", "Double"));

    public static void validate(Reservation reservation, Map<String, Integer> inventory)
            throws InvalidBookingException {

        // Validate room type
        if (!VALID_ROOM_TYPES.contains(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type selected!");
        }

        // Validate room count
        if (reservation.getNumberOfRooms() <= 0) {
            throw new InvalidBookingException("Number of rooms must be greater than 0!");
        }

        // Validate inventory existence
        if (!inventory.containsKey(reservation.getRoomType())) {
            throw new InvalidBookingException("Room type not available in inventory!");
        }

        // Validate availability
        int available = inventory.get(reservation.getRoomType());
        if (reservation.getNumberOfRooms() > available) {
            throw new InvalidBookingException("Not enough rooms available!");
        }
    }
}

// Booking Manager
class BookingManager {

    private Map<String, Integer> inventory;

    public BookingManager() {
        inventory = new HashMap<>();
        inventory.put("Single", 5);
        inventory.put("Double", 3);
    }

    public void processBooking(Reservation reservation) {
        try {
            // Validate first (Fail-Fast)
            InvalidBookingValidator.validate(reservation, inventory);

            // Update inventory (only if valid)
            int remaining = inventory.get(reservation.getRoomType()) - reservation.getNumberOfRooms();
            inventory.put(reservation.getRoomType(), remaining);

            System.out.println("Booking Confirmed!");
            System.out.println(reservation);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// Main Class
public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        BookingManager manager = new BookingManager();

        while (true) {
            System.out.println("\n--- Booking Menu ---");
            System.out.println("1. Book Room");
            System.out.println("2. View Inventory");
            System.out.println("3. Exit");

            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {

                case 1:
                    try {
                        System.out.print("Enter Reservation ID: ");
                        String id = scanner.nextLine();

                        System.out.print("Enter Guest Name: ");
                        String name = scanner.nextLine();

                        System.out.print("Enter Room Type (Single/Double): ");
                        String roomType = scanner.nextLine();

                        System.out.print("Enter Number of Rooms: ");
                        int rooms = scanner.nextInt();

                        Reservation reservation =
                                new Reservation(id, name, roomType, rooms);

                        manager.processBooking(reservation);

                    } catch (Exception e) {
                        // Catch unexpected runtime errors
                        System.out.println("Unexpected Error: Invalid input format!");
                        scanner.nextLine(); // clear buffer
                    }
                    break;

                case 2:
                    manager.displayInventory();
                    break;

                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}