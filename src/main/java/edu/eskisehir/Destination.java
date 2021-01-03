package edu.eskisehir;
import java.time.LocalDate;

public class Destination {
    private LocalDate date;
    private final City city;
    private int latitude;
    private int longitude;

    public Destination(LocalDate date,City city) {
        this.city=city;
        this.date=date;
    }

    public Destination(City city,int latitude,int longitude) {
        this.city=city;
        this.latitude=latitude;
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public LocalDate getDate() {
        return date;
    }

    public City getCity() {
        return city;
    }

}
