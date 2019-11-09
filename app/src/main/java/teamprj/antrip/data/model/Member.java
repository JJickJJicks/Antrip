package teamprj.antrip.data.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Member {
    private String email;
    private int type;
    private String profile;

    public Member() {
        }

    public Member(String email, int type, String profile) {
            super();
            this.email = email;
            this.type = type;
            this.profile = profile;
    }

    public String getEmail() {
        return email;
    }

    public int getType() {
        return type;
    }

    public String getProfile() { return profile; }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("type", type);
        result.put("profile", profile);

        return result;
    }
}
