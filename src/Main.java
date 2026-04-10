import java.util.*;

// Represents a booking request
class BookingRequest {
    String guestName;
    String roomType;

    BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "BookingRequest{" + "guestName='" + guestName + '\'' + ", roomType='" + roomType + '\'' + '}';
    }
}

// Manages room inventory
class InventoryService {
    private final Map<String, Integer> roomInventory;

    InventoryService(Map<String, Integer> initialInventory) {
        this.roomInventory = new HashMap<>(initialInventory);
    }

    boolean isAvailable(String roomType) {
        return roomInventory.getOrDefault(roomType, 0) > 0;
    }

    void decrement(String roomType) {
        roomInventory.put(roomType, roomInventory.get(roomType) - 1);
    }

    void printInventory() {
        System.out.println("Current Inventory: " + roomInventory);
    }
}

// Processes booking requests and assigns rooms
class BookingService {
    private final Queue<BookingRequest> bookingQueue;
    private final InventoryService inventoryService;
    private final Map<String, Set<String>> allocatedRooms; // roomType -> set of room IDs
    private int roomIdCounter;

    BookingService(Queue<BookingRequest> bookingQueue, InventoryService inventoryService) {
        this.bookingQueue = bookingQueue;
        this.inventoryService = inventoryService;
        this.allocatedRooms = new HashMap<>();
        this.roomIdCounter = 1; // starting room ID
    }

    void processBookings() {
        while (!bookingQueue.isEmpty()) {
            BookingRequest request = bookingQueue.poll();
            System.out.println("Processing: " + request);

            if (!inventoryService.isAvailable(request.roomType)) {
                System.out.println("No rooms available for type: " + request.roomType + ". Booking failed.\n");
                continue;
            }

            String roomId = generateUniqueRoomId(request.roomType);
            allocateRoom(request.roomType, roomId);
            inventoryService.decrement(request.roomType);
            System.out.println("Reservation confirmed for " + request.guestName +
                    ". Assigned Room ID: " + roomId + "\n");
        }
    }

    private String generateUniqueRoomId(String roomType) {
        return roomType.substring(0, 1).toUpperCase() + String.format("%03d", roomIdCounter++);
    }

    private void allocateRoom(String roomType, String roomId) {
        allocatedRooms.putIfAbsent(roomType, new HashSet<>());
        allocatedRooms.get(roomType).add(roomId);
    }

    void printAllocatedRooms() {
        System.out.println("Allocated Rooms: " + allocatedRooms + "\n");
    }
}

public class Main {
    public static void main(String[] args) {
        // Initial inventory
        Map<String, Integer> initialInventory = new HashMap<>();
        initialInventory.put("Single", 2);
        initialInventory.put("Double", 2);
        initialInventory.put("Suite", 1);

        InventoryService inventoryService = new InventoryService(initialInventory);

        // Booking queue (FIFO)
        Queue<BookingRequest> bookingQueue = new LinkedList<>();
        bookingQueue.add(new BookingRequest("Alice", "Single"));
        bookingQueue.add(new BookingRequest("Bob", "Double"));
        bookingQueue.add(new BookingRequest("Charlie", "Single"));
        bookingQueue.add(new BookingRequest("David", "Suite"));
        bookingQueue.add(new BookingRequest("Eve", "Double"));
        bookingQueue.add(new BookingRequest("Frank", "Single")); // Should fail, only 2 singles

        BookingService bookingService = new BookingService(bookingQueue, inventoryService);

        // Process all bookings
        bookingService.processBookings();

        // Print final allocations and inventory
        bookingService.printAllocatedRooms();
        inventoryService.printInventory();
    }
}