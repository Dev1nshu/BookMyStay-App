abstract class Room {

    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    public abstract void displayRoomType();

    public void displayRoomDetails() {
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sq ft");
        System.out.println("Price per Night: $" + pricePerNight);
    }
}


class SingleRoom extends Room {

    public SingleRoom(int beds, int size, double price) {
        super(beds, size, price);
    }

    public void displayRoomType() {
        System.out.println("Room Type: Single Room");
    }
}


class DoubleRoom extends Room {

    public DoubleRoom(int beds, int size, double price) {
        super(beds, size, price);
    }

    public void displayRoomType() {
        System.out.println("Room Type: Double Room");
    }
}


class SuiteRoom extends Room {

    public SuiteRoom(int beds, int size, double price) {
        super(beds, size, price);
    }

    public void displayRoomType() {
        System.out.println("Room Type: Suite Room");
    }
}


class Main {

    public static void main(String[] args) {

        // Static availability variables
        int singleRoomAvailability = 5;
        int doubleRoomAvailability = 3;
        int suiteRoomAvailability = 2;

        // Creating room objects
        Room single = new SingleRoom(1, 150, 1000);
        Room doubleRoom = new DoubleRoom(2, 250, 2000);
        Room suite = new SuiteRoom(3, 400, 5000);

        System.out.println("------ Room Information ------");

        single.displayRoomType();
        single.displayRoomDetails();
        System.out.println("Available: " + singleRoomAvailability);
        System.out.println();

        doubleRoom.displayRoomType();
        doubleRoom.displayRoomDetails();
        System.out.println("Available: " + doubleRoomAvailability);
        System.out.println();

        suite.displayRoomType();
        suite.displayRoomDetails();
        System.out.println("Available: " + suiteRoomAvailability);

        System.out.println("\nApplication Terminated.");
    }
}