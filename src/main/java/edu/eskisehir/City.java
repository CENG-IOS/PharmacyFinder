package edu.eskisehir;

public class City {
    private final int latitude;
    private final int longitude;
    private final String name;

    Object[][] cityMap = new Object[10][10];

    public City(int latitude, int longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public void placePharmacy(Pharmacy pharmacy) {
        cityMap[pharmacy.getLatitude()][pharmacy.getLongtitude()] = pharmacy;
    }

    /**
     * X is for person's position
     * Y is for the destination
     * P is for pharmacies
     * - for emptiness.
     */
    public String getPosition(int latitude, int longitude) {
        if (cityMap[latitude][longitude] == null) {
            return "-";
        } else if (cityMap[latitude][longitude] == "X") {
            return "X";
        } else if (cityMap[latitude][longitude] == "Y") {
            return "Y";
        } else {
            return "P";
        }
    }

    public static void printCityMap(City city) {

        System.out.println("┌──────────────────────────────┐");
        for (int i = 0; i < 10; i++) {
            System.out.print("│ ");
            for (int j = 0; j < 10; j++) {
                if (j == 9) {
                    System.out.print(city.getPosition(i, j) + " ");
                } else
                    System.out.print(city.getPosition(i, j) + "  ");
            }
            System.out.println("│");
        }
        System.out.println("└──────────────────────────────┘");

    }

}
