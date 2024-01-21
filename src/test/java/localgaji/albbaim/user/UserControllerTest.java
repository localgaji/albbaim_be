package localgaji.albbaim.user;

import localgaji.albbaim.__core__.auth.AuthUserArgumentResolver;
import localgaji.albbaim.__core__.exception.GlobalErrorHandler;
import localgaji.albbaim.auth.user.User;
import localgaji.albbaim.auth.user.UserController;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    private UserController userController;
    private MockMvc mockMvc;
    @Mock
    private AuthUserArgumentResolver authUserArgumentResolver;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalErrorHandler())
                .setCustomArgumentResolvers(authUserArgumentResolver)
                .build();
    }

    @DisplayName("유저 정보 조회 성공 테스트")
    @Test
    void getMyInfoSuccess() throws Exception {
        // given
        User user = user();
        String token = "Bearer ABC";

        when(authUserArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(authUserArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(user);

        // when
        ResultActions resultActions = mockMvc.perform(get("/user")
                .header(HttpHeaders.AUTHORIZATION, token)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("response.userName", user.getUserName()).exists())
                .andExpect(jsonPath("response.userType", user.getUserType()).exists())
                .andReturn();
    }

    private User user() {
        return User.builder()
                .userId(1L)
                .userName("라이언")
                .isAdmin(true)
                .build();
    }
}