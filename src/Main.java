/*
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 * @author devanshu
 * @version 11.0
 */

import java.util.*;

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    public boolean hasRooms(String type) {
        return inventory.getOrDefault(type, 0) > 0;
    }

    public void reduceInventory(String type) {
        inventory.put(type, inventory.get(type) - 1);
    }

    public void showInventory() {
        System.out.println("Remaining Inventory:");
        inventory.forEach((type, count) ->
                System.out.println(type.split(" ")[0] + ": " + count));
    }
}

class RoomAllocationService {
    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> assignedRoomsByType = new HashMap<>();

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String type = reservation.getRoomType();
        if (inventory.hasRooms(type)) {
            String roomId = generateRoomId(type);
            allocatedRoomIds.add(roomId);
            assignedRoomsByType.computeIfAbsent(type, k -> new HashSet<>()).add(roomId);
            inventory.reduceInventory(type);
            System.out.println("Booking confirmed for Guest: " + reservation.getGuestName() + ", Room ID: " + roomId);
        }
    }

    private String generateRoomId(String roomType) {
        int count = assignedRoomsByType.getOrDefault(roomType, new HashSet<>()).size() + 1;
        return roomType.split(" ")[0] + "-" + count;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> requestQueue = new LinkedList<>();

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}

class ConcurrentBookingProcessor implements Runnable {
    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(BookingRequestQueue q, RoomInventory i, RoomAllocationService a) {
        this.bookingQueue = q;
        this.inventory = i;
        this.allocationService = a;
    }

    @Override
    public void run() {
        while (true) {
            Reservation reservation = null;

            synchronized (bookingQueue) {
                if (bookingQueue.hasPendingRequests()) {
                    reservation = bookingQueue.getNextRequest();
                } else {
                    break;
                }
            }

            if (reservation != null) {
                synchronized (inventory) {
                    allocationService.allocateRoom(reservation, inventory);
                }
            }

            try { Thread.sleep(50); } catch (InterruptedException e) { break; }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Concurrent Booking Simulation\n" + "=".repeat(30));

        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();
        bookingQueue.addRequest(new Reservation("Abhi", "Single Room"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Double Room"));
        bookingQueue.addRequest(new Reservation("Kural", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Subha", "Single Room"));

        Thread t1 = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService));
        Thread t2 = new Thread(new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService));

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        System.out.println("\n--- Final Status ---");
        inventory.showInventory();
    }
}