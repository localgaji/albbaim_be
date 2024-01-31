package localgaji.albbaim.user;

import localgaji.albbaim.auth.authDTO.RequestAuth.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 추가")
    void makeNewUser() {
        // given
        SignUpRequest request = new SignUpRequest("라이언", true, "CODE");
        User newUser = request.toEntity();
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // when
        User madeUser = userService.makeNewUser(request);

        // then
        assertThat(madeUser).usingRecursiveComparison().isEqualTo(newUser);
    }

}


