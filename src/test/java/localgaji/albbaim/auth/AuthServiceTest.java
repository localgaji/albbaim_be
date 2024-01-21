package localgaji.albbaim.auth;

import localgaji.albbaim.auth.authDTO.RequestAuth.LoginRequest;
import localgaji.albbaim.auth.authDTO.RequestAuth.SignUpRequest;
import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuth;
import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuthService;
import localgaji.albbaim.auth.user.User;
import localgaji.albbaim.auth.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    private AuthService authService;
    @Mock
    private UserService userService;
    @Mock
    private KakaoAuthService kakaoAuthService;

    @DisplayName("카카오 로그인 서비스 테스트")
    @Test
    void kakaoLoginSuccess() throws Exception {
        // given
        LoginRequest request = loginRequest();
        KakaoAuth kakaoAuth = kakaoAuth();
        when(kakaoAuthService.findKakaoAuthByCode(any(String.class))).thenReturn(kakaoAuth);

        // when
        User user = authService.kakaoLogin(request);

        // then
        assertThat(user.getUserId()).isEqualTo(1L);
    }

    @DisplayName("카카오 회원가입 서비스 테스트")
    @Test
    void kakaoSingUpSuccess() throws Exception {
        // given
        SignUpRequest request = signUpRequest();
        User requestUser = request.toEntity();
        when(userService.makeNewUser(any(SignUpRequest.class))).thenReturn(requestUser);
        doNothing().when(kakaoAuthService).makeNewKakaoUser(any(), any(User.class));

        // when
        User newUser = authService.kakaoSignUp(request);

        // then
        assertThat(newUser.getUserId()).isEqualTo(requestUser.getUserId());
    }

    private LoginRequest loginRequest() {
        return new LoginRequest("KAKAO CODE");
    }

    private SignUpRequest signUpRequest() {
        return new SignUpRequest("라이언", true, "KAKAO CODE");
    }

    private KakaoAuth kakaoAuth() {
        return KakaoAuth.builder()
                .kakaoId(12345678L)
                .user(user())
                .build();
    }

    private User user() {
        return User.builder()
                .userId(1L)
                .userName("라이언")
                .isAdmin(true)
                .build();
    }
}
