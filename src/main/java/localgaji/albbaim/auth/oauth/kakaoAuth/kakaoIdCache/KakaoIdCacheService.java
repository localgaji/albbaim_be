package localgaji.albbaim.auth.oauth.kakaoAuth.kakaoIdCache;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class KakaoIdCacheService {

    private final KakaoIdCacheRepository kakaoIdCacheRepository;

    // 카카오 인증 캐시 생성
    public void createKakaoIdCache(String code, Long kakaoId) {
        KakaoIdCache newKakaoIdCache = KakaoIdCache.builder()
                .code(code).kakaoId(kakaoId)
                .build();
        kakaoIdCacheRepository.save(newKakaoIdCache);
    }

    // 카카오 인증 캐시 조회
    public KakaoIdCache findKakaoIdByCode(String code) {
        return kakaoIdCacheRepository.findByCode(code)
                .orElseThrow(()-> new CustomException(ErrorType.ETC_ERROR));
    }

    // 카카오 인증 캐시 삭제
    public void deleteWaiting(KakaoIdCache kakaoIdCache) {
        kakaoIdCacheRepository.delete(kakaoIdCache);
    }

}
