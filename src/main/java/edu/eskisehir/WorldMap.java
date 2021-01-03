package edu.eskisehir;
import java.time.LocalDate;
import java.util.*;

public class WorldMap {

    Object[][] map = new Object[10][10];
    City city1 = new City(2, 2, "City1");
    City city2 = new City(4, 8, "City2");

    public void placeCity(City city) {
        map[city.getLatitude()][city.getLongitude()] = city;
    }

    public boolean perceiveDestination(Destination destination) {
        destination.getCity().cityMap[destination.getLatitude()][destination.getLongitude()] = "Y";

        if ((destination.getCity() != null)) {
            System.out.println(">Destination is perceived.");
            return true;
        } else {
            System.out.println(">No destination");
            return false;
        }
    }

    public boolean perceiveLocation(GPS gps) {
        gps.getCity().cityMap[gps.getLatitude()][gps.getLongitude()] = "X";

        if (gps.isGPSon()) {
            System.out.println(">Location is perceived:");
            return true;
        } else {
            System.out.println(">No GPS");
            return false;
        }
    }

    public LinkedList<Pharmacy> scan(GPS gps) {
        LinkedList<Pharmacy> pharmaciesOnDuty = new LinkedList<>();
        int currentLatitude = gps.getLatitude();
        int currentLongitude = gps.getLongitude();
        int counter = 2;
        while (true) {
            int latitudeBeginning = currentLatitude - 1;
            int latitudeEnd = latitudeBeginning + counter;
            int longitudeBeginning = currentLongitude - 1;
            int longitudeEnd = longitudeBeginning + counter;

            determineOnDutyPharmacies(gps, pharmaciesOnDuty, latitudeBeginning, latitudeEnd, longitudeBeginning, longitudeEnd);

            if (pharmaciesOnDuty.isEmpty()) {
                counter += 2;
                currentLatitude = latitudeBeginning;
                currentLongitude = longitudeBeginning;
            } else
                break;
        }
        return pharmaciesOnDuty;
    }

    public LinkedList<Pharmacy> scanByDestination(Destination destination) {
        City wantedCity = destination.getCity();
        LocalDate wantedDate = destination.getDate();

        LinkedList<Pharmacy> onDutyPharmacies = new LinkedList<>();

        for (Object[] objects : map) {
            for (Object object : objects) {
                if (object != null && object.equals(wantedCity)) {
                    for (int k = 0; k < wantedCity.cityMap.length; k++) {
                        for (int l = 0; l < wantedCity.cityMap[k].length; l++) {
                            if (wantedCity.cityMap[k][l] != null && (wantedCity.cityMap[k][l] != "X" && wantedCity.cityMap[k][l] != "Y")) {
                                Pharmacy pharmacy = (Pharmacy) wantedCity.cityMap[k][l];
                                for (int m = 0; m < (pharmacy.getOnDutyDays().size()); m++) {
                                    if (pharmacy.getOnDutyDays().get(m).equals(wantedDate))
                                        onDutyPharmacies.add((Pharmacy) wantedCity.cityMap[k][l]);
                                }
                            }
                        }
                    }
                }
            }
        }
        return onDutyPharmacies;
    }

    private void determineOnDutyPharmacies(GPS gps, LinkedList<Pharmacy> pharmaciesOnDuty,
                                           int latitudeBeginning, int latitudeEnd, int longitudeBeginning, int longitudeEnd) {
        int endLat;
        int beginningLat;
        int endLot;
        int beginningLot;
        if (latitudeEnd >= gps.getCity().cityMap.length)
            endLat = gps.getCity().cityMap.length - 1;
        else
            endLat = latitudeEnd;

        beginningLat = Math.max(latitudeBeginning, 0);

        if (longitudeEnd >= gps.getCity().cityMap.length)
            endLot = gps.getCity().cityMap.length - 1;
        else
            endLot = longitudeEnd;

        beginningLot = Math.max(longitudeBeginning, 0);

        for (int i = beginningLat; i <= endLat; i++) {
            for (int j = beginningLot; j <= endLot; j++) {
                if (gps.getCity().cityMap[i][j] != null && (gps.getCity().cityMap[i][j] != "X" && gps.getCity().cityMap[i][j] != "Y")) {
                    Pharmacy pharmacy = (Pharmacy) gps.getCity().cityMap[i][j];
                    if (pharmacy.getOnDuty(gps.getLocalDate()))
                        pharmaciesOnDuty.add(pharmacy);
                }
            }
        }
    }

    public Map<Pharmacy, Double> compare(LinkedList<Pharmacy> onDutyPharmacies, GPS gps) {

        Map<Pharmacy, Double> pharmacyDistances = new HashMap<>();
        for (Pharmacy onDutyPharmacy : onDutyPharmacies) {

            int myLatitude = gps.getLatitude();
            int myLongtitude = gps.getLongitude();

            int targetLatitude = onDutyPharmacy.getLatitude();
            int targetLongtitude = onDutyPharmacy.getLongtitude();
            double distance;

            double up = 0, down = 0, right = 0, left = 0;

            //to find up or down
            if (targetLatitude - myLatitude < 0) {
                up = Math.abs(targetLatitude - myLatitude);
            } else if (targetLatitude - myLatitude > 0) {
                down = targetLatitude - myLatitude;
            }

            //to find left or right
            if (targetLongtitude - myLongtitude < 0) {
                left = Math.abs(targetLongtitude - myLongtitude);
            } else if (targetLongtitude - myLongtitude > 0) {
                right = targetLongtitude - myLongtitude;
            }

            if (left == 0 && right == 0) {
                distance = Math.max(up, down);
            } else if (down == 0 && up == 0) {
                distance = Math.max(left, right);
            } else {
                distance = Math.pow(left, 2) + Math.pow(right, 2) + Math.pow(down, 2) + Math.pow(up, 2);
                distance = Math.sqrt(distance);
            }

            pharmacyDistances.put(onDutyPharmacy, distance);

        }
        return pharmacyDistances;
    }


    public void displayRoad(Pharmacy selectedPharmacy, GPS gps) {
        int myLatitude = gps.getLatitude();
        int myLongitude = gps.getLongitude();

        int targetLatitude = selectedPharmacy.getLatitude();
        int targetLongitude = selectedPharmacy.getLongtitude();

        double up = 0, down = 0, right = 0, left = 0;

        //to find up or down
        if (targetLatitude - myLatitude < 0) {
            up = Math.abs(targetLatitude - myLatitude);
        } else if (targetLatitude - myLatitude > 0) {
            down = targetLatitude - myLatitude;
        }

        //to find left or right
        if (targetLongitude - myLongitude < 0) {
            left = Math.abs(targetLongitude - myLongitude);
        } else if (targetLongitude - myLongitude > 0) {
            right = targetLongitude - myLongitude;
        }

        if (up != 0)
            System.out.println("Go " + up + " up.");
        if (down != 0)
            System.out.println("Go " + down + " down.");
        if (right != 0)
            System.out.println("Go " + right + " right.");
        if (left != 0)
            System.out.println("Go " + left + " left.");

    }


    public Pharmacy markOnMap(Map<Pharmacy, Double> pharmaicesDistances) {

        Pharmacy closestPharmacy = null;

        LinkedList<Double> values = new LinkedList<>(pharmaicesDistances.values());
        Collections.sort(values);
        for (Map.Entry<Pharmacy, Double> iterator : pharmaicesDistances.entrySet()) {
            if (values.get(0).equals(iterator.getValue()))
                closestPharmacy = iterator.getKey();
        }
        return closestPharmacy;
    }

    public LinkedList<Pharmacy> surround(GPS gps, Destination destination) {
        LinkedList<Pharmacy> pharmaciesOnDuty = new LinkedList<>();
        int currentLatitude = gps.getLatitude();
        int currentLongitude = gps.getLongitude();

        int targetLatitude = destination.getLatitude();
        int targetLongitude = destination.getLongitude();
        while (true) {
            int beginningLatitude = Math.min(currentLatitude, targetLatitude);
            int endLatitude = Math.max(currentLatitude, targetLatitude);
            int beginningLongitude = Math.min(currentLongitude, targetLongitude);
            int endLongitude = Math.max(currentLongitude, targetLongitude);

            determineOnDutyPharmacies(gps, pharmaciesOnDuty, beginningLatitude, endLatitude, beginningLongitude, endLongitude);
            if (pharmaciesOnDuty.isEmpty()) {
                currentLatitude -= 1;
                currentLongitude += 1;
                targetLatitude += 1;
                targetLongitude -= 1;
            } else

                break;
        }
        return pharmaciesOnDuty;

    }

    public Map<Pharmacy, Double> compareBetweenToLocations(GPS gps, Destination destination,
                                                           LinkedList<Pharmacy> determinedPharmacies) {
        Map<Pharmacy, Double> pharmacyDistances = new HashMap<>();

        for (Pharmacy determinedPharmacy : determinedPharmacies) {
            int x1 = gps.getLatitude(), y1 = gps.getLongitude(), x2 = destination.getLatitude(), y2 = destination.getLongitude();
            double slopeOfTwoLocation = (y2 - y1) / (double) (x2 - x1);
            double a = (slopeOfTwoLocation * (-1)), b = 1, c = slopeOfTwoLocation * x1 - y1;
            double distance = Math.abs(a * determinedPharmacy.getLatitude() +
                    b * determinedPharmacy.getLongtitude() + c) / Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
            pharmacyDistances.put(determinedPharmacy, distance);
        }
        return pharmacyDistances;
    }

    public City searchCity(String cityName) {
        if (cityName.equals(city1.getName())) {
            return city1;
        } else {
            return city2;
        }

    }

}
