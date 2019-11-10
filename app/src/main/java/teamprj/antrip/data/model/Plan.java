package teamprj.antrip.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Plan {
    private int period;
    private List<String> authority = new ArrayList<>();
    private HashMap<String, ArrayList<Travel>> travel = new HashMap<>();

    public Plan() { }

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
