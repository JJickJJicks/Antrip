package teamprj.antrip.data;

import java.io.IOException;

import teamprj.antrip.data.model.LoggedInUser;

/**
 * 로그인 자격 증명을 사용하여 인증을 처리하고 사용자 정보를 검색하는 클래스입니다.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String email, String password) {

        try {
            // TODO: loggedInUser 인증 처리
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: 인증 취소
    }
}
