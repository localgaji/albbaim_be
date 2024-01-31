package localgaji.albbaim.auth.oauth.kakaoAuth;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.auth.oauth.kakaoAuth.kakaoIdCache.KakaoIdCache;
import localgaji.albbaim.auth.oauth.kakaoAuth.kakaoIdCache.KakaoIdCacheService;
import localgaji.albbaim.auth.oauth.kakaoAuth.fetch.KakaoAPIFetcher;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service @RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoAPIFetcher kakaoAPIFetcher;
    private final KakaoAuthRepository kakaoAuthRepository;
    private final KakaoIdCacheService kakaoIdCacheService;

    public void makeNewKakaoUser(String code, User newUser) {
        // 캐시로 카카오 아이디 찾기
        KakaoIdCache kakaoIdCache = kakaoIdCacheService.findKakaoIdByCode(code);

        // 카카오 정보 객체 생성
        KakaoAuth newKakaoAuth = KakaoAuth.builder()
                .user(newUser)
                .kakaoId(kakaoIdCache.getKakaoId())
                .build();
        // 카카오 정보 객체 저장
        kakaoAuthRepository.save(newKakaoAuth);

        // 캐시 삭제
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
