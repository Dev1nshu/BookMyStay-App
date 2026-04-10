import java.util.*;

class Room {
    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }
}

class Inventory {
    private Map<String, Integer> availability;

    public Inventory() {
        availability = new HashMap<>();
    }

    public void addRoom(String type, int count) {
        availability.put(type, count);
    }

    public int getAvailability(String type) {
        return availability.getOrDefault(type, 0);
    }

    public Set<String> getRoomTypes() {
        return availability.keySet();
    }
}

class SearchService {
    private Inventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(Inventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public void searchAvailableRooms() {
        for (String type : inventory.getRoomTypes()) {
            int count = inventory.getAvailability(type);
            if (count > 0) {
                Room room = roomCatalog.get(type);
                System.out.println("Room Type: " + room.getType());
                System.out.println("Price: $" + room.getPrice());
                System.out.println("Amenities: " + room.getAmenities());
                System.out.println("Available Rooms: " + count);
                System.out.println("----------------------------");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {

        Inventory inventory = new Inventory();

        inventory.addRoom("Single", 5);
        inventory.addRoom("Double", 0);
        inventory.addRoom("Suite", 2);

        Map<String, Room> roomCatalog = new HashMap<>();

        roomCatalog.put("Single", new Room("Single", 100.0, "WiFi, TV"));
        roomCatalog.put("Double", new Room("Double", 150.0, "WiFi, TV, Mini Bar"));
        roomCatalog.put("Suite", new Room("Suite", 300.0, "WiFi, TV, Mini Bar, Jacuzzi"));

        SearchService searchService = new SearchService(inventory, roomCatalog);

        System.out.println("Available Rooms:");
        System.out.println("=================");
        searchService.searchAvailableRooms();
    }
}