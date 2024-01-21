package localgaji.albbaim.user;

import localgaji.albbaim.auth.user.User;
import localgaji.albbaim.auth.user.UserRepository;
import localgaji.albbaim.workplace.Workplace;
import localgaji.albbaim.workplace.WorkplaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkplaceRepository workplaceRepository;  // 이거 없애기

    @DisplayName("매장 소속 유저 조회")
    @Test
    void findByWorkplaceTest() {
        // given
        Workplace workplace = workplace();
        workplaceRepository.save(workplace);

        User user1 = user();
        User user2 = user();

        userRepository.save(user1);
        userRepository.save(user2);

        user1.updateGroup(workplace);
        user2.updateGroup(workplace);

        // when
        Optional<List<User>> opt = userRepository.findByWorkplace(workplace);

        // then
        assertThat(opt.get().size()).isEqualTo(2);
    }

    private Workplace workplace() {
        return Workplace.builder()
                .marketName("라이언월드")
                .build();
    }

    private User user() {
        return User.builder()
                .userName("라이언")
                .isAdmin(true)
                .build();
    }

}
