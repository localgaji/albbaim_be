package localgaji.albbaim.utils;

import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuth;
import localgaji.albbaim.auth.oauth.kakaoAuth.kakaoIdCache.KakaoIdCache;
import localgaji.albbaim.auth.user.User;
import localgaji.albbaim.workplace.Workplace;

public class Samples {
    public static User someUser() {
        return User.builder()
                .userName("라이언")
                .isAdmin(true)
                .build();
    }

    public static Workplace someWorkplace() {
        return Workplace.builder()
                .marketName("라이언월드")
                .build();
    }

    public static KakaoAuth someKakaoAuth(User user) {
        return KakaoAuth.builder()
                .kakaoId(123L)
                .user(user)
                .build();
    }

    public static KakaoIdCache someKakaoIdCache() {
        return KakaoIdCache.builder()
                .kakaoId(someKakaoId)
                .code(someCode)
                .build();
    }

    public static String someCode = "CODE";
    public static Long someKakaoId = 123L;
}
