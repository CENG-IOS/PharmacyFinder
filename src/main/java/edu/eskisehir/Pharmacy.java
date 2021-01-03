package edu.eskisehir;
import java.time.LocalDate;
import java.util.LinkedList;

public class Pharmacy {
    private final int latitude;
    private final int longitude;
    private final String address;
    private final String phoneNumber;
    private final String name;
    private boolean onDuty;
    private final LinkedList<LocalDate> onDutyDays = new LinkedList<>();

    //Consturctor without onDuty and onDutyDays attributes.
    public Pharmacy(int latitude, int longtitude, String address, String phoneNumber,
                    String name) {
        this.latitude = latitude;
        this.longitude = longtitude;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongtitude() {
        return longitude;
    }

    public LinkedList<LocalDate> getOnDutyDays() {
        return onDutyDays;
    }

    public void fillOnDutyDays(LinkedList<LocalDate> localDate) {
        onDutyDays.addAll(localDate);
    }

    public String displayInformation() {
        return "Name: " + name + " Address: " + address + " Phone Number: " + phoneNumber;
    }

    public boolean getOnDuty(LocalDate currentDay) {
        if (onDutyDays.contains(currentDay))
            onDuty = true;
        return onDuty;
    }


    @Override
    public String toString() {
        return name + " {" +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", onDuty=" + onDuty +
                ", onDutyDays=" + onDutyDays +
                '}';
    }

}
