package localgaji.albbaim.auth.oauth.kakaoAuth.kakaoIdCache;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service @RequiredArgsConstructor
public class KakaoIdCacheService {

    private final KakaoIdCacheRepository kakaoIdCacheRepository;

    public void createKakaoIdCache(String code, Long kakaoId) {
        KakaoIdCache newKakaoIdCache = KakaoIdCache.builder()
                .code(code).kakaoId(kakaoId)
                .build();
        kakaoIdCacheRepository.save(newKakaoIdCache);
    }

    public KakaoIdCache findKakaoIdByCode(String code) {
        log.debug("카카오 임시 인증 정보 찾기 시작");
        return kakaoIdCacheRepository.findByCode(code)
                .orElseThrow(()-> new CustomException(ErrorType.ETC_ERROR));
    }

    public void deleteWaiting(KakaoIdCache kakaoIdCache) {
        log.debug("카카오 임시 인증 정보 제거 시작");
        kakaoIdCacheRepository.delete(kakaoIdCache);
    }

}
