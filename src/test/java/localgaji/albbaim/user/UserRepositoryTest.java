package localgaji.albbaim.user;

import localgaji.albbaim.auth.user.User;
import localgaji.albbaim.auth.user.UserRepository;
import localgaji.albbaim.workplace.Workplace;
import localgaji.albbaim.workplace.WorkplaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static localgaji.albbaim.utils.Samples.someUser;
import static localgaji.albbaim.utils.Samples.someWorkplace;
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
        Workplace workplace = someWorkplace();
        workplaceRepository.save(workplace);

        User user1 = someUser();
        User user2 = someUser();

        userRepository.save(user1);
        userRepository.save(user2);

        user1.updateWorkplace(workplace);
        user2.updateWorkplace(workplace);

        // when, then
        assertThat(user1.getWorkplace().getUserList().size()).isEqualTo(2);
    }

}
