package teamprj.antrip.data.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Member {
    private String email;
    private int type;

    public Member() {
        }

    public Member(String email, int type) {
            super();
            this.email = email;
            this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public int getType() {
        return type;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("type", type);

        return result;
    }
}
