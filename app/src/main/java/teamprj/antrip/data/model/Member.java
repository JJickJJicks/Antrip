package teamprj.antrip.data.model;

import java.io.Serializable;

public class Member implements Serializable {
    private String email, name, birth;
    private int type;

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
}
