package localgaji.albbaim.auth.kakaoAuth;

import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuth;
import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuthRepository;
import localgaji.albbaim.user.User;
import localgaji.albbaim.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static localgaji.albbaim.__utils__.Samples.someKakaoAuth;
import static localgaji.albbaim.__utils__.Samples.someUser;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest @ExtendWith(MockitoExtension.class)
class KakaoAuthRepositoryTest {

    @Autowired
    KakaoAuthRepository kakaoAuthRepository;
    @Autowired
    UserRepository userRepository;  // 이거 치우기

    @DisplayName("조회 성공")
    @Test
    void findByKakaoIdSuccess() {
        // given
        User user = someUser();
        KakaoAuth kakaoAuth = someKakaoAuth(user);
        userRepository.save(user);
        kakaoAuthRepository.save(kakaoAuth);

        // when
        Optional<KakaoAuth> opt = kakaoAuthRepository.findByKakaoId(123L);

        // then
        assertThat(opt.get().getKakaoId()).isEqualTo(123L);
    }

    @DisplayName("조회 실패")
    @Test
    void findByKakaoIdFail() {
        // given

        // when
        Optional<KakaoAuth> opt = kakaoAuthRepository.findByKakaoId(123L);

        // then
        Assertions.assertThatThrownBy(opt::get)
                .isInstanceOf(NoSuchElementException.class);
    }
}
