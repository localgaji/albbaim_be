package localgaji.albbaim.user;

import localgaji.albbaim.auth.user.User;
import localgaji.albbaim.auth.user.UserType;
import localgaji.albbaim.workplace.Workplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        user.updateGroup(workplace);

        // then
        assertThat(user.getWorkplace()).isEqualTo(workplace);
        assertThat(user.getUserType()).isEqualTo(UserType.ADMIN);
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

    private User someUser() {
        return User.builder()
                .userId(1L)
                .userName("라이언")
                .isAdmin(true)
                .build();
    }

    private Workplace someWorkplace() {
        return Workplace.builder()
                .workplaceId(1L)
                .marketName("라이언 월드")
                .marketNumber("1111111111")
                .mainAddress("서울시 성동구 성수대로")
                .detailAddress("1번지")
                .build();
    }
}
