package teamprj.antrip.data.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Directions {
    private String origin, destination, key;

    public Directions() {
    }

    public Directions(String origin, String destination, String key) {
        super();
        this.origin = origin;
        this.destination = destination;
        this.key = key;
    }



    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("origin", origin);
        result.put("destination", destination);

        return result;
    }
}
