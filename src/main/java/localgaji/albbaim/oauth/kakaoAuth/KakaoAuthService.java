package localgaji.albbaim.oauth.kakaoAuth;

import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.oauth.kakaoAuth.kakaoAuthTemp.KakaoAuthTemp;
import localgaji.albbaim.oauth.kakaoAuth.kakaoAuthTemp.KakaoAuthTempService;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service @Slf4j @RequiredArgsConstructor
public class KakaoAuthService {

    private final KakaoAuthRepository kakaoAuthRepository;
    @Autowired
    private final KakaoAuthAPIController kakaoAuthAPIController;

    private final KakaoAuthTempService kakaoAuthTempService;

    public void makeNewKakaoUser(String code, User newUser) {
        KakaoAuthTemp kakaoAuthTemp = kakaoAuthTempService.findKakaoIdByCode(code);

        log.debug("카카오 정보 객체 생성 시작");
        KakaoAuth kakaoAuth = KakaoAuth.builder()
                .user(newUser)
                .kakaoId(kakaoAuthTemp.getKakaoId())
                .build();

        log.debug("카카오 정보 저장 시작");
        kakaoAuthRepository.save(kakaoAuth);

        kakaoAuthTempService.deleteWaiting(kakaoAuthTemp);
    }

    public KakaoAuth findAuthData(String code) {
        Long kakaoId = kakaoAuthAPIController.codeToKakaoId(code);

        Optional<KakaoAuth> optKakaoAuth = kakaoAuthRepository.findByKakaoId(kakaoId);

        if (optKakaoAuth.isEmpty()) {
            kakaoAuthTempService.saveKakaoTemp(code, kakaoId);
            throw new CustomException(ErrorType.NOT_OUR_MEMBER);
        }
        return optKakaoAuth.get();
    }
}
