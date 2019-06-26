package teamprj.antrip.data;

import teamprj.antrip.data.model.LoggedInUser;

/**
 * 원격 데이터 소스에서 인증 및 사용자 정보를 요청하고 로그인 상태 및 사용자 자격 증명 정보의 메모리 내 캐시를 유지하는 클래스입니다.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null; // 로그인한 유저 정보가 여기에 저장된다

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInUser> login(String email, String password) {
        // handle login
        Result<LoggedInUser> result = dataSource.login(email, password);
        if (result instanceof Result.Success)
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        return result;
    }
}
