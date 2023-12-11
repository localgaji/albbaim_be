package localgaji.albbaim.oauth.kakaoAuth.kakaoAuthTemp;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service @RequiredArgsConstructor
public class KakaoAuthTempService {
    private final KakaoAuthTempRepository kakaoAuthTempRepository;

    public void saveKakaoTemp(String code, Long kakaoId) {
        KakaoAuthTemp kakaoAuthTemp = KakaoAuthTemp.builder()
                .code(code).kakaoId(kakaoId)
                .build();
        kakaoAuthTempRepository.save(kakaoAuthTemp);
    }

    public KakaoAuthTemp findKakaoIdByCode(String code) {
        log.debug("카카오 임시 인증 정보 찾기 시작");
        return kakaoAuthTempRepository.findByCode(code)
                .orElseThrow(()-> new CustomException(ErrorType.ETC_ERROR));
    }

    public void deleteWaiting(KakaoAuthTemp kakaoAuthTemp) {
        log.debug("카카오 임시 인증 정보 제거 시작");
        kakaoAuthTempRepository.delete(kakaoAuthTemp);
    }

}
