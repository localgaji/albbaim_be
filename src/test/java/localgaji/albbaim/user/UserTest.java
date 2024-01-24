package localgaji.albbaim.user;

import localgaji.albbaim.workplace.Workplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static localgaji.albbaim.__utils__.Samples.*;
import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @BeforeEach

    @DisplayName("유저에 매장 정보 업데이트")
    @Test
    void userUpdateWorkplace() {
        // given
        User user = someUser();
        Workplace workplace = someWorkplace();

        // when
        user.updateWorkplace(workplace);

        // then
        assertThat(user.getWorkplace()).isEqualTo(workplace);
        assertThat(user.getUserType()).isEqualTo(UserType.ADMIN);
        assertThat(user.getWorkplace().getUserList().size()).isEqualTo(1);
    }

    @DisplayName("유저에 매장 정보 없음")
    @Test
    void userNoWorkplace() {
        // given
        User user = someUser();

        // then
        assertThat(user.getWorkplace()).isNull();
        assertThat(user.getUserType()).isEqualTo(UserType.ADMIN_NO_GROUP);
    }

    @DisplayName("유저에 프로필 이미지 업데이트")
    @Test
    void userUpdateProfileImg() {
        // given
        User user = someUser();
        String profileImg = "ABC";

        // when
        user.updateProfileImg(profileImg);

        // then
        assertThat(user.getProfileImg()).isEqualTo(profileImg);
    }
}
