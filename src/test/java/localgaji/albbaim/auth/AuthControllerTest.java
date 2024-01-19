package localgaji.albbaim.auth;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.__core__.exception.GlobalErrorHandler;
import localgaji.albbaim.auth.authDTO.RequestAuth.LoginRequest;
import localgaji.albbaim.auth.authDTO.RequestAuth.SignUpRequest;
import localgaji.albbaim.auth.user.User;
import localgaji.albbaim.utils.JsonRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks
    private AuthController authController;
    @Mock
    private AuthService authService;
    private JsonRequest jsonRequest;

    @BeforeEach
    public void init() {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalErrorHandler())
                .build();
        jsonRequest = new JsonRequest(mockMvc);
    }

    @DisplayName("카카오 로그인 성공 테스트")
    @Test
    void kakaoLoginSuccess() throws Exception {
        // given
        User user = user();
        String token = "Bearer ABC";
        LoginRequest request = loginRequest();

        when(authService.kakaoLogin(any(LoginRequest.class))).thenReturn(user);
        when(authService.getToken(any(User.class))).thenReturn(token);

        // when
        ResultActions resultActions = jsonRequest.sendPostRequest("/auth/login/kakao", request);

        // then
        resultActions.andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.AUTHORIZATION, token))
                    .andExpect(jsonPath("response.isAdmin", user.getIsAdmin()).exists())
                    .andReturn();
    }

    @DisplayName("카카오 로그인 실패 테스트")
    @Test
    void kakaoLogin404() throws Exception {
        // given
        LoginRequest request = loginRequest();
        when(authService.kakaoLogin(any(LoginRequest.class)))
                .thenThrow(new CustomException(ErrorType.MEMBER_NOT_FOUND));

        // when
        ResultActions resultActions = jsonRequest.sendPostRequest("/auth/login/kakao", request);

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("error.errorCode").exists());
    }

    @DisplayName("카카오 회원가입 성공 테스트")
    @Test
    void kakaoSignUpSuccess() throws Exception {
        // given
        User user = user();
        String token = "Bearer ABC";
        SignUpRequest request = signUpRequest();

        when(authService.kakaoSignUp(any(SignUpRequest.class))).thenReturn(user);
        when(authService.getToken(any(User.class))).thenReturn(token);

        // when
        ResultActions resultActions = jsonRequest.sendPostRequest("/auth/signUp/kakao", request);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.AUTHORIZATION, token))
                .andExpect(jsonPath("response.isAdmin", user.getIsAdmin()).exists())
                .andReturn();
    }
    private SignUpRequest signUpRequest() {
        return new SignUpRequest("라이언", true, "zkzkdhzhem");
    }

    private LoginRequest loginRequest() {
        return new LoginRequest("zkzkdhzhem");
    }

    private User user() {
        return User.builder()
                .userId(1L)
                .userName("라이언")
                .isAdmin(true)
                .build();
    }

}
