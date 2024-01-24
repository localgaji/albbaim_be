package localgaji.albbaim.auth;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.auth.authDTO.RequestAuth.LoginRequest;
import localgaji.albbaim.auth.authDTO.RequestAuth.SignUpRequest;
import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuth;
import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuthService;
import localgaji.albbaim.user.User;
import localgaji.albbaim.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static localgaji.albbaim.__utils__.Samples.someKakaoAuth;
import static localgaji.albbaim.__utils__.Samples.someUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @DisplayName("카카오 로그인 서비스 성공")
    @Test
    void kakaoLoginSuccess() {
        // given
        LoginRequest request = loginRequest();
        User someUser = someUser();
        KakaoAuth kakaoAuth = someKakaoAuth(someUser);

        when(kakaoAuthService.findKakaoAuthByCode(any(String.class))).thenReturn(kakaoAuth);

        // when
        User user = authService.kakaoLogin(request);

        // then
        assertThat(user).isEqualTo(someUser);
    }

    @DisplayName("카카오 로그인 서비스 실패")
    @Test
    void kakaoLoginFail() {
        // given
        LoginRequest request = loginRequest();

        when(kakaoAuthService.findKakaoAuthByCode(any(String.class)))
                .thenThrow(new CustomException(ErrorType.MEMBER_NOT_FOUND));

        // when, then
        assertThatThrownBy(() -> authService.kakaoLogin(request))
                .isInstanceOf(CustomException.class);
    }

    @DisplayName("카카오 회원가입 서비스 테스트")
    @Test
    void kakaoSingUpSuccess() {
        // given
        SignUpRequest request = signUpRequest();
        User requestUser = request.toEntity();

        when(userService.makeNewUser(any(SignUpRequest.class))).thenReturn(requestUser);
        doNothing().when(kakaoAuthService).makeNewKakaoUser(any(), any(User.class));

        // when
        User newUser = authService.kakaoSignUp(request);

        // then
        assertThat(newUser).isEqualTo(requestUser);
    }

    private LoginRequest loginRequest() {
        return new LoginRequest("CODE");
    }

    private SignUpRequest signUpRequest() {
        return new SignUpRequest("라이언", true, "CODE");
    }
}
