package localgaji.albbaim.auth.oauth.kakaoAuth;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.auth.oauth.kakaoAuth.kakaoIdCache.KakaoIdCache;
import localgaji.albbaim.auth.oauth.kakaoAuth.kakaoIdCache.KakaoIdCacheService;
import localgaji.albbaim.auth.oauth.kakaoAuth.fetch.KakaoAPIFetcher;
import localgaji.albbaim.auth.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service @Slf4j @RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoAPIFetcher kakaoAPIFetcher;
    private final KakaoAuthRepository kakaoAuthRepository;
    private final KakaoIdCacheService kakaoIdCacheService;

    public void makeNewKakaoUser(String code, User newUser) {
        KakaoIdCache kakaoIdCache = kakaoIdCacheService.findKakaoIdByCode(code);

        log.debug("카카오 정보 객체 생성 시작");
        KakaoAuth newKakaoAuth = KakaoAuth.builder()
                .user(newUser)
                .kakaoId(kakaoIdCache.getKakaoId())
                .build();

        log.debug("카카오 정보 저장 시작");
        kakaoAuthRepository.save(newKakaoAuth);

        kakaoIdCacheService.deleteWaiting(kakaoIdCache);
    }

    public KakaoAuth findKakaoAuthByCode(String code) {
        Long kakaoId = kakaoAPIFetcher.codeToKakaoId(code);
        Optional<KakaoAuth> optKakaoAuth = kakaoAuthRepository.findByKakaoId(kakaoId);
        if (optKakaoAuth.isEmpty()) {
            kakaoIdCacheService.createKakaoIdCache(code, kakaoId);
            throw new CustomException(ErrorType.MEMBER_NOT_FOUND);
        }
        return optKakaoAuth.get();
    }
}
