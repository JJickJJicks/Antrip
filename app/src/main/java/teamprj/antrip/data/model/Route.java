package teamprj.antrip.data.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Route {
    private String startName, endName, overviewPolyline;
    private Double startLat, startLng,  endLat, endLng;

    public Route() {
    }

    public Route(String startName, String endName, String overviewPolyline, Double startLat, Double startLng, Double endLat, Double endLng) {
        this.startName = startName;
        this.endName = endName;
        this.overviewPolyline = overviewPolyline;
        this.startLat = startLat;
        this.startLng = startLng;
        this.endLat = endLat;
        this.endLng = endLng;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("startName", startName);
        result.put("endName", endName);
        result.put("overviewPolyline", overviewPolyline);
        result.put("startLat", startLat);
        result.put("startLng", startLng);
        result.put("endName", endName);
        result.put("endLat", endLat);
        result.put("endLng", endLng);

        return result;
    }

    public String getStartName() {
        return startName;
    }

    public String getEndName() {
        return endName;
    }

    public String getOverviewPolyline() {
        return overviewPolyline;
    }

    public Double getStartLat() {
        return startLat;
    }

    public Double getStartLng() {
        return startLng;
    }

    public Double getEndLat() {
        return endLat;
    }

    public Double getEndLng() {
        return endLng;
    }
}
