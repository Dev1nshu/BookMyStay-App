import java.util.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId;
    }
}


class BookingHistory {

    private Map<String, Reservation> activeBookings = new HashMap<>();
    private Set<String> cancelledBookings = new HashSet<>();

    public void addBooking(Reservation reservation) {
        activeBookings.put(reservation.getReservationId(), reservation);
    }

    public Reservation getBooking(String reservationId) {
        return activeBookings.get(reservationId);
    }

    public void removeBooking(String reservationId) {
        activeBookings.remove(reservationId);
        cancelledBookings.add(reservationId);
    }

    public boolean isCancelled(String reservationId) {
        return cancelledBookings.contains(reservationId);
    }

    public void displayActiveBookings() {
        if (activeBookings.isEmpty()) {
            System.out.println("No active bookings.");
            return;
        }

        System.out.println("\nActive Bookings:");
        for (Reservation r : activeBookings.values()) {
            System.out.println(r);
        }
    }
}


class CancellationService {

    private Map<String, Integer> inventory;
    private Map<String, Stack<String>> availableRooms; // Stack for rollback
    private BookingHistory history;

    public CancellationService(Map<String, Integer> inventory,
                               Map<String, Stack<String>> availableRooms,
                               BookingHistory history) {
        this.inventory = inventory;
        this.availableRooms = availableRooms;
        this.history = history;
    }

    public void cancelBooking(String reservationId) {

        if (history.isCancelled(reservationId)) {
            System.out.println("Cancellation Failed: Booking already cancelled!");
            return;
        }

        Reservation reservation = history.getBooking(reservationId);

        if (reservation == null) {
            System.out.println("Cancellation Failed: Booking does not exist!");
            return;
        }

        // Rollback logic
        String roomType = reservation.getRoomType();
        String roomId = reservation.getRoomId();


        availableRooms.get(roomType).push(roomId);

        inventory.put(roomType, inventory.get(roomType) + 1);

        history.removeBooking(reservationId);

        System.out.println("Booking Cancelled Successfully!");
        System.out.println("Room " + roomId + " returned to pool.");
    }
}

class BookingManager {

    private Map<String, Integer> inventory = new HashMap<>();
    private Map<String, Stack<String>> availableRooms = new HashMap<>();
    private BookingHistory history;

    public BookingManager(BookingHistory history) {
        this.history = history;

        // Initialize inventory
        inventory.put("Single", 2);
        inventory.put("Double", 2);

        // Initialize room stacks
        availableRooms.put("Single", new Stack<>());
        availableRooms.put("Double", new Stack<>());

        // Add room IDs
        availableRooms.get("Single").push("S1");
        availableRooms.get("Single").push("S2");

        availableRooms.get("Double").push("D1");
        availableRooms.get("Double").push("D2");
    }

    public void createBooking(String id, String name, String roomType) {

        if (!inventory.containsKey(roomType) || inventory.get(roomType) == 0) {
            System.out.println("Booking Failed: No rooms available!");
            return;
        }

        String roomId = availableRooms.get(roomType).pop();
        inventory.put(roomType, inventory.get(roomType) - 1);

        Reservation reservation = new Reservation(id, name, roomType, roomId);
        history.addBooking(reservation);

        System.out.println("Booking Confirmed!");
        System.out.println(reservation);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public Map<String, Stack<String>> getAvailableRooms() {
        return availableRooms;
    }
}

// Main Class
public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        BookingHistory history = new BookingHistory();
        BookingManager manager = new BookingManager(history);
        CancellationService cancellationService =
                new CancellationService(manager.getInventory(),
                        manager.getAvailableRooms(),
                        history);

        while (true) {
            System.out.println("\n--- Booking System ---");
            System.out.println("1. Create Booking");
            System.out.println("2. Cancel Booking");
            System.out.println("3. View Active Bookings");
            System.out.println("4. Exit");

            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Enter Reservation ID: ");
                    String id = scanner.nextLine();

                    System.out.print("Enter Guest Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter Room Type (Single/Double): ");
                    String roomType = scanner.nextLine();

                    manager.createBooking(id, name, roomType);
                    break;

                case 2:
                    System.out.print("Enter Reservation ID to cancel: ");
                    String cancelId = scanner.nextLine();

                    cancellationService.cancelBooking(cancelId);
                    break;

                case 3:
                    history.displayActiveBookings();
                    break;

                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}