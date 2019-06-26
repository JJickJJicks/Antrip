package teamprj.antrip.data;

import java.io.IOException;

import teamprj.antrip.data.model.LoggedInUser;

/**
 * 로그인 자격 증명을 사용하여 인증을 처리하고 사용자 정보를 검색하는 클래스입니다.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String email, String password) {
        // TODO: 로그인 Data 로드 부분 (Fake 대신 실 DB 연결 필요)
        Fakedata fakedata = new Fakedata();
        String realemail = fakedata.email;
        String realpw = fakedata.password;
        String name = fakedata.name;

        try {
            if (email.equals(realemail) && password.equals(realpw)) {
                LoggedInUser fakeUser = new LoggedInUser(java.util.UUID.randomUUID().toString(), name);
                return new Result.Success<>(fakeUser);
            }
            else
                return new Result.Error(new Exception());

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: 인증 취소
    }
}
