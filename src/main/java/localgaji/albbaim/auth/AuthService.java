package localgaji.albbaim.auth;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.user.User;
import localgaji.albbaim.user.UserService;
import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuth;
import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuthService;
import localgaji.albbaim.__core__.auth.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static localgaji.albbaim.auth.authDTO.RequestAuth.*;

@Slf4j @Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final KakaoAuthService kakaoAuthService;
    private final TokenProvider tokenProvider;

    @Transactional(noRollbackFor = CustomException.class)
    public User kakaoLogin(LoginRequest requestBody) {
        KakaoAuth kakaoAuth = kakaoAuthService.findKakaoAuthByCode(requestBody.code());
        return kakaoAuth.getUser();
    }

    @Transactional
    public User kakaoSignUp(SignUpRequest requestBody) {
        User newUser = userService.makeNewUser(requestBody);
        kakaoAuthService.makeNewKakaoUser(requestBody.code(), newUser);
        return newUser;
    }

    public String getToken(User user) {
        return tokenProvider.createToken(user.getUserId());
    }
}
