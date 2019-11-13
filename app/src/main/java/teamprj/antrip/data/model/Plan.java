package teamprj.antrip.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Plan {
    private int period;
    private List<String> authority = new ArrayList<>();
    private HashMap<String, ArrayList<Travel>> travel = new HashMap<>();
    private String start_date;
    private String end_date;
    private boolean save;

    public Plan() { }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public List<String> getAuthority() {
        return authority;
    }

    public void setAuthority(List<String> authority) {
        this.authority = authority;
    }

    public HashMap<String, ArrayList<Travel>> getTravel() {
        return travel;
    }

    public void setTravel(HashMap<String, ArrayList<Travel>> travel) {
        this.travel = travel;
    }
}
