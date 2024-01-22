package localgaji.albbaim.user;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.auth.authDTO.RequestAuth.SignUpRequest;
import localgaji.albbaim.auth.user.User;
import localgaji.albbaim.auth.user.UserRepository;
import localgaji.albbaim.auth.user.UserService;
import localgaji.albbaim.workplace.Workplace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static localgaji.albbaim.utils.Samples.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    @DisplayName("유저에 매장 추가")
    void addWorkplace() {
        // given
        User user = someUser();
        Workplace workplace = someWorkplace();
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        userService.addWorkplace(user, workplace);

        // then
        assertThat(user.getWorkplace()).isEqualTo(workplace);
    }

    @Test
    @DisplayName("매장에 가입된 유저 찾기 성공")
    void findUsersByWorkplace() {
        // given
        User user1 = someUser();
        User user2 = someUser();
        List<User> users = new ArrayList<>(Arrays.asList(user1, user2));
        Workplace workplace = someWorkplace();

        when(userRepository.findByWorkplace(any(Workplace.class)))
                .thenReturn(Optional.of(users));

        // when
        List<User> foundUsers = userService.findUsersByWorkplace(workplace);

        // then
        assertThat(users.size()).isEqualTo(foundUsers.size());
    }

    @Test
    @DisplayName("매장에 가입된 유저 찾기 실패")
    void findUsersByWorkplaceFail() {
        // given
        Workplace workplace = someWorkplace();

        when(userRepository.findByWorkplace(any(Workplace.class)))
                .thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> userService.findUsersByWorkplace(workplace))
                .isInstanceOf(CustomException.class);
    }
}
