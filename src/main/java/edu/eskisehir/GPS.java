package edu.eskisehir;
import java.time.LocalDate;

public class GPS {
    private final int latitude;
    private final int longitude;
    private final City city;
    private final boolean isGPSon;
    private final LocalDate localDate;

    public GPS(int latitude, int longitude, City city, LocalDate localDate) {
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.localDate = localDate;
        this.isGPSon = true;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public City getCity() {
        return city;
    }

    public boolean isGPSon() {
        return isGPSon;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

}
