package localgaji.albbaim.auth.kakaoAuth;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuth;
import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuthRepository;
import localgaji.albbaim.auth.oauth.kakaoAuth.KakaoAuthService;
import localgaji.albbaim.auth.oauth.kakaoAuth.fetch.KakaoAPIFetcher;
import localgaji.albbaim.auth.oauth.kakaoAuth.kakaoIdCache.KakaoIdCache;
import localgaji.albbaim.auth.oauth.kakaoAuth.kakaoIdCache.KakaoIdCacheService;
import localgaji.albbaim.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static localgaji.albbaim.utils.Samples.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KakaoAuthServiceTest {
    @InjectMocks
    private KakaoAuthService kakaoAuthService;
    @Mock
    private KakaoAPIFetcher kakaoAPIFetcher;
    @Mock
    private KakaoAuthRepository kakaoAuthRepository;
    @Mock
    private KakaoIdCacheService kakaoIdCacheService;

    @DisplayName("카카오 인증 정보 객체 생성")
    @Test
    void makeNewKakaoUser(){
        // give
        User user = someUser();
        KakaoAuth newKakaoAuth = someKakaoAuth(user);
        KakaoIdCache idCache = someKakaoIdCache();

        when(kakaoIdCacheService.findKakaoIdByCode(any())).thenReturn(idCache);
        when(kakaoAuthRepository.save(any())).thenReturn(newKakaoAuth);
        doNothing().when(kakaoIdCacheService).deleteWaiting(idCache);

        // when
        kakaoAuthService.makeNewKakaoUser(someCode, user);

        // then
    }

    @DisplayName("카카오 인가코드로 카카오 인증 정보 조회 성공")
    @Test
    void findKakaoAuthByCodeSuccess(){
        // given
        User user = someUser();
        KakaoAuth kakaoAuth = someKakaoAuth(user);

        when(kakaoAPIFetcher.codeToKakaoId(any())).thenReturn(someKakaoId);
        when(kakaoAuthRepository.findByKakaoId(any())).thenReturn(Optional.of(kakaoAuth));

        // when
        KakaoAuth gottenKakaoAuth = kakaoAuthService.findKakaoAuthByCode(someCode);

        // then
        assertThat(gottenKakaoAuth).isNotNull();
    }

    @DisplayName("카카오 인가코드로 카카오 인증 정보 조회 실패")
    @Test
    void findKakaoAuthByCodeFail(){
        // given
        when(kakaoAPIFetcher.codeToKakaoId(any())).thenReturn(someKakaoId);
        when(kakaoAuthRepository.findByKakaoId(any())).thenReturn(Optional.empty());
        doNothing().when(kakaoIdCacheService).createKakaoIdCache(someCode, someKakaoId);

        // when, then
        Assertions.assertThatThrownBy(() -> kakaoAuthService.findKakaoAuthByCode(someCode))
                .isInstanceOf(CustomException.class);
    }

}
