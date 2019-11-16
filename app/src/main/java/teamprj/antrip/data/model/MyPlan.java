package teamprj.antrip.data.model;

import java.io.Serializable;

public class MyPlan implements Serializable {
    private String TripName;

    public MyPlan(String tripName, String period, String end_date, String start_date, String save) {
        TripName = tripName;
    }

    public MyPlan() {
    }

    public String getTripName() {
        return TripName;
    }

    public void setTripName(String tripName) {
        TripName = tripName;
    }
}
