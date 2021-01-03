package edu.eskisehir;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Map;

public class Test {
    static WorldMap worldMap = new WorldMap();

    /**
     * when user opens his/he GPS this object is created and all his/her information parse into this object
     * If this statement becomes null this means another use case is running
     */
    static GPS person1 = new GPS(3, 5, worldMap.city1, LocalDate.of(2021, 1, 4));
    static GPS person2 = new GPS(3, 5, worldMap.city2, LocalDate.of(2021, 1, 4));
    static GPS person3 = new GPS(8, 1, worldMap.city2, LocalDate.of(2021, 1, 22));

    /**
     * user enters the date and city and by entering
     * date and city a destination object is created. If this statement becomes null this means another use case is running
     */
    static Destination destination = new Destination(LocalDate.of(2021, 1, 22), worldMap.city2);
    static Destination destination2 = new Destination(worldMap.city2, 8, 1);

    public static void main(String[] args) {
        createASimulationWorld(worldMap);


        /**
         * Use Case 1 ----> Finding the nearest pharmacy with navigation.
         */
        System.out.println("┌────────────────────────────────────────────────────────────────┐");
        System.out.println("│ Use Case 1 ----> Finding the nearest pharmacy with navigation: │");
        System.out.println("└────────────────────────────────────────────────────────────────┘");
        System.out.println(">GPS is activated.");
        if (worldMap.perceiveLocation(person1)) {
            City foundCity = worldMap.searchCity(person1.getCity().getName());
            City.printCityMap(foundCity);
            System.out.println(">Scanning...");
            LinkedList<Pharmacy> pharmaciesOnDuty = worldMap.scan(person1);
            System.out.println(">Pharmacies on duty remaining in the area are determined:");
            for (Pharmacy pharmacy : pharmaciesOnDuty) {
                System.out.println(pharmacy.displayInformation());
            }
            System.out.println(">Comparing distances...");
            Map<Pharmacy, Double> pharmaciesDistances = worldMap.compare(pharmaciesOnDuty, person1);
            System.out.println(">The closest pharmacy to your position:");
            Pharmacy closestPharmacy = worldMap.markOnMap(pharmaciesDistances);
            if (closestPharmacy != null) {
                printPharmacyOnMapAndDisplayRoad(closestPharmacy, foundCity);

            }
            else
                System.out.println(">There is no pharmacy found in your city.");
        }


        /**
         * Use case 2 ----> Finding the nearest pharmacy due to the a destination point.
         */
        System.out.println("┌───────────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ Use case 2 ----> Finding the nearest pharmacy due to the a destination point: │");
        System.out.println("└───────────────────────────────────────────────────────────────────────────────┘");

        System.out.println(">GPS is activated.");
        if (worldMap.perceiveDestination(destination2) && worldMap.perceiveLocation(person2) && destination2.getCity().equals(person2.getCity())) {
            City foundCity = worldMap.searchCity(person2.getCity().getName());
            City.printCityMap(foundCity);
            System.out.println(">The application surrounds two locations on the map as one circle.");
            LinkedList<Pharmacy> pharmaciesOnDuty = worldMap.surround(person2, destination2);
            System.out.println(">Pharmacies on duty remaining in the area are determined:");
            for (Pharmacy pharmacy : pharmaciesOnDuty) {
                System.out.println(pharmacy.displayInformation());
            }
            System.out.println(">Comparing distances...");
            Map<Pharmacy, Double> pharmacyDistances = worldMap.compareBetweenToLocations(person2, destination2, pharmaciesOnDuty);
            System.out.println(">The closest pharmacy to your position: ");
            Pharmacy closestPharmacy = worldMap.markOnMap(pharmacyDistances);
            if (closestPharmacy != null) {
                printPharmacyOnMapAndDisplayRoad(closestPharmacy, foundCity);
            } else
                System.out.println(">There is no pharmacy found in your city.");
        }

        /**
         * Use case 3 ----> Finding on duty pharmacies with given date and city.
         */
        System.out.println("┌───────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ Use case 3 ----> Finding on duty pharmacies with given date and city: │");
        System.out.println("└───────────────────────────────────────────────────────────────────────┘");
        System.out.println(">The user enters a date for when to search pharmacies:");
        System.out.println(destination.getDate().toString());
        System.out.println(">The user enters a city for where to search pharmacies:");
        System.out.println(person3.getCity().getName());

        if (worldMap.perceiveDestination(destination)) {
            System.out.println(">Scanning the city on the given date: ");
            LinkedList<Pharmacy> onDutyPharmacies = worldMap.scanByDestination(destination);
            if (!onDutyPharmacies.isEmpty()) {
                System.out.println(">Determined Pharmacies are: ");
                int counter = 1;
                for (Pharmacy onDutyPharmacy : onDutyPharmacies) {
                    System.out.print(counter++ + "-)");
                    System.out.println(onDutyPharmacy.displayInformation());
                }
            } else
                System.out.println(">No pharmacies found according to your destination information. ");
        }


    }

    /**
     * In a real program all these information comes from a worldwide database.
     */
    public static void createASimulationWorld(WorldMap worldMap) {

        worldMap.placeCity(worldMap.city1);
        worldMap.placeCity(worldMap.city2);

        Pharmacy p1 = new Pharmacy(1, 2, "add1", "0500", "P1");
        LinkedList<LocalDate> listOfp1 = new LinkedList<>();
        listOfp1.add(LocalDate.of(2020, 12, 24));
        listOfp1.add(LocalDate.of(2020, 12, 26));
        listOfp1.add(LocalDate.of(2020, 12, 28));
        p1.fillOnDutyDays(listOfp1);

        Pharmacy p2 = new Pharmacy(2, 9, "add2", "0501", "P2");
        LinkedList<LocalDate> listOfp2 = new LinkedList<>();
        listOfp2.add(LocalDate.of(2020, 12, 25));
        listOfp2.add(LocalDate.of(2020, 12, 26));
        listOfp2.add(LocalDate.of(2020, 12, 29));
        p2.fillOnDutyDays(listOfp2);

        Pharmacy p3 = new Pharmacy(6, 1, "add3", "0502", "P3");
        LinkedList<LocalDate> listOfp3 = new LinkedList<>();
        listOfp3.add(LocalDate.of(2020, 12, 30));
        listOfp3.add(LocalDate.of(2021, 1, 1));
        listOfp3.add(LocalDate.of(2021, 1, 3));
        p3.fillOnDutyDays(listOfp3);

        Pharmacy p4 = new Pharmacy(9, 5, "add4", "0503", "P4");
        LinkedList<LocalDate> listOfp4 = new LinkedList<>();
        listOfp4.add(LocalDate.of(2021, 1, 2));
        listOfp4.add(LocalDate.of(2021, 1, 4));
        listOfp4.add(LocalDate.of(2021, 1, 6));
        p4.fillOnDutyDays(listOfp4);

        Pharmacy p5 = new Pharmacy(5, 7, "add5", "0504", "P5");
        LinkedList<LocalDate> listOfp5 = new LinkedList<>();
        listOfp5.add(LocalDate.of(2021, 1, 5));
        listOfp5.add(LocalDate.of(2021, 1, 4));
        listOfp5.add(LocalDate.of(2020, 1, 9));
        p5.fillOnDutyDays(listOfp5);

        Pharmacy p6 = new Pharmacy(1, 4, "add6", "0505", "P6");
        LinkedList<LocalDate> listOfp6 = new LinkedList<>();
        listOfp6.add(LocalDate.of(2021, 1, 11));
        listOfp6.add(LocalDate.of(2021, 1, 8));
        listOfp6.add(LocalDate.of(2021, 1, 4));
        p6.fillOnDutyDays(listOfp6);

        worldMap.city1.placePharmacy(p1);
        worldMap.city1.placePharmacy(p2);
        worldMap.city1.placePharmacy(p3);
        worldMap.city1.placePharmacy(p4);
        worldMap.city1.placePharmacy(p5);
        worldMap.city1.placePharmacy(p6);

        Pharmacy p7 = new Pharmacy(4, 2, "add7", "0506", "P7");
        LinkedList<LocalDate> listOfp7 = new LinkedList<>();
        listOfp7.add(LocalDate.of(2021, 1, 15));
        listOfp7.add(LocalDate.of(2021, 2, 1));
        listOfp7.add(LocalDate.of(2021, 1, 4));
        p7.fillOnDutyDays(listOfp7);

        Pharmacy p8 = new Pharmacy(2, 7, "add8", "0507", "P8");
        LinkedList<LocalDate> listOfp8 = new LinkedList<>();
        listOfp8.add(LocalDate.of(2021, 1, 22));
        listOfp8.add(LocalDate.of(2021, 1, 10));
        listOfp8.add(LocalDate.of(2021, 1, 3));
        p8.fillOnDutyDays(listOfp8);

        Pharmacy p9 = new Pharmacy(9, 7, "add9", "0508", "P9");
        LinkedList<LocalDate> listOfp9 = new LinkedList<>();
        listOfp9.add(LocalDate.of(2021, 1, 22));
        listOfp9.add(LocalDate.of(2021, 1, 12));
        listOfp9.add(LocalDate.of(2021, 1, 3));
        p9.fillOnDutyDays(listOfp9);

        Pharmacy p10 = new Pharmacy(8, 4, "add10", "0509", "P10");
        LinkedList<LocalDate> listOfp10 = new LinkedList<>();
        listOfp10.add(LocalDate.of(2021, 1, 2));
        listOfp10.add(LocalDate.of(2021, 1, 4));
        listOfp10.add(LocalDate.of(2021, 1, 8));
        p10.fillOnDutyDays(listOfp10);

        Pharmacy p11 = new Pharmacy(1, 2, "add11", "0510", "P11");
        LinkedList<LocalDate> listOfp11 = new LinkedList<>();
        listOfp11.add(LocalDate.of(2021, 1, 4));
        listOfp11.add(LocalDate.of(2021, 1, 14));
        listOfp11.add(LocalDate.of(2020, 1, 9));
        p11.fillOnDutyDays(listOfp11);

        Pharmacy p12 = new Pharmacy(5, 5, "add12", "0511", "P12");
        LinkedList<LocalDate> listOfp12 = new LinkedList<>();
        listOfp12.add(LocalDate.of(2021, 1, 11));
        listOfp12.add(LocalDate.of(2021, 1, 4));
        listOfp12.add(LocalDate.of(2021, 1, 13));
        p12.fillOnDutyDays(listOfp12);

        worldMap.city2.placePharmacy(p7);
        worldMap.city2.placePharmacy(p8);
        worldMap.city2.placePharmacy(p9);
        worldMap.city2.placePharmacy(p10);
        worldMap.city2.placePharmacy(p11);
        worldMap.city2.placePharmacy(p12);


    }
    public static void printPharmacyOnMapAndDisplayRoad(Pharmacy closestPharmacy, City foundCity) {
        System.out.println("┌──────────────────────────────┐");
        for (int i = 0; i < 10; i++) {
            System.out.print("│");
            for (int j = 0; j < 10; j++) {
                if (closestPharmacy == foundCity.cityMap[i][j]) {
                    System.out.print(" P ");
                } else if (foundCity.cityMap[i][j] == "X") {
                    System.out.print(" X ");
                } else if (foundCity.cityMap[i][j] == "Y") {
                    System.out.print(" Y ");
                } else {
                    System.out.print(" - ");
                }

            }
            System.out.println("│");
        }
        System.out.println("└──────────────────────────────┘");
        System.out.println(">Displaying closest pharmacy information: ");
        System.out.println(closestPharmacy.displayInformation());
        System.out.println(">How to go to the pharmacy: ");
        worldMap.displayRoad(closestPharmacy, person1);
        System.out.println(">The user goes to that location.");
        System.out.println();
    }
}
