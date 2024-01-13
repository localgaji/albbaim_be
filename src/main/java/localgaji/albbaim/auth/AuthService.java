package localgaji.albbaim.auth;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.auth.user.User;
import localgaji.albbaim.auth.user.UserService;
import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuth;
import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuthService;
import localgaji.albbaim.auth.token.TokenProvider;
import localgaji.albbaim.auth.user.userDTO.RequestAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j @Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final KakaoAuthService kakaoAuthService;
    private final TokenProvider tokenProvider;

    @Transactional(noRollbackFor = CustomException.class)
    public User kakaoLogin(RequestAuth.LoginRequest requestBody) {
        KakaoAuth kakaoAuth = kakaoAuthService.findKakaoAuthByCode(requestBody.code());
        return kakaoAuth.getUser();
    }

    @Transactional
    public User kakaoSignUp(RequestAuth.SignUpRequest requestBody) {
        User newUser = userService.makeNewUser(requestBody);
        kakaoAuthService.makeNewKakaoUser(requestBody.code(), newUser);
        return newUser;
    }

    public String getToken(User user) {
        return tokenProvider.createToken(user.getUserId());
    }
}
