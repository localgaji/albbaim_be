package localgaji.albbaim.user;

import localgaji.albbaim.__core__.ApiUtil;
import localgaji.albbaim.oauth.kakaoAuth.KakaoAuth;
import localgaji.albbaim.oauth.kakaoAuth.KakaoAuthService;
import localgaji.albbaim.user.token.TokenProvider;
import localgaji.albbaim.user.userDTO.RequestAuth;
import localgaji.albbaim.user.userDTO.ResponseAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j @Service @RequiredArgsConstructor
public class LoginService {

    private final UserService userService;
    private final KakaoAuthService kakaoAuthService;
    private final TokenProvider tokenProvider;

    public ResponseEntity<ApiUtil.Response<ResponseAuth.PostLoginResponse>> kakaoLogin(RequestAuth.LoginRequest requestBody) {
        KakaoAuth kakaoAuth = kakaoAuthService.findAuthData(requestBody.code());
        User user = userService.findUserByKakaoAuth(kakaoAuth);
        return buildLoginResponse(user);
    }

    public ResponseEntity<ApiUtil.Response<ResponseAuth.PostLoginResponse>> kakaoSignUp(RequestAuth.SignUpRequest requestBody) {
        User newUser = userService.makeNewUser(requestBody);
        kakaoAuthService.makeNewKakaoUser(requestBody.code(), newUser);
        return buildLoginResponse(newUser);
    }

    private ResponseEntity<ApiUtil.Response<ResponseAuth.PostLoginResponse>> buildLoginResponse(User user) {
        String token = getToken(user);
        return ResponseEntity.ok()
                .header("Authorization", token)
                .body(ApiUtil.success(new ResponseAuth.PostLoginResponse(user.getIsAdmin())));
    }

    private String getToken(User user) {
        return tokenProvider.createToken(user);
    }
}
