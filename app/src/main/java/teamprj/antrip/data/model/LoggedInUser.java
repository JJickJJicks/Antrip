package teamprj.antrip.data.model;

/**
 * LoginRepository에서 검색 한 로그인 한 사용자의 사용자 정보를 캡처하는 데이터 클래스
 */
public class LoggedInUser {

    private String email; // 로그인 할 때 썼던 Email
    private String name; // 이후에 사용되는 닉네임

    public LoggedInUser(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
