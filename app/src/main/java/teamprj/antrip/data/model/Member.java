package teamprj.antrip.data.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Member {
    private String email, name, birth;
    private int type;

    public Member() {
    }

    public Member(String email, String name, String birth, int type) {
        super();
        this.email = email;
        this.name = name;
        this.birth = birth;
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getBirth() {
        return birth;
    }

    public int getType() {
        return type;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("name", name);
        result.put("birth", birth);
        result.put("type", type);

        return result;
    }
}
