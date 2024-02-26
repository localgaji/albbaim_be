package localgaji.albbaim.auth.oauth.kakaoAuth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static localgaji.albbaim.auth.oauth.kakaoAuth.fetch.RequestKakaoAPI.*;
import static localgaji.albbaim.auth.oauth.kakaoAuth.fetch.ResponseKakaoAPI.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "카카오 oauth 서버 모킹", description = "")
@RequestMapping("/mock")
public class MockAuthController {
    @PostMapping("/oauth/token")
    @Operation(summary = "토큰발급")
    public ResponseEntity<GetTokenResponse> getKakaoToken(GetTokenRequest request) {

        GetTokenResponse response = new GetTokenResponse("token");
        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/v2/user/me")
    @Operation(summary = "아이디 조회")
    public ResponseEntity<GetKakaoIdResponse> getKakaoId() {
        int randomKakaoId = (int) (Math.random() * 2000000) + 1;
        GetKakaoIdResponse response = new GetKakaoIdResponse(String.valueOf(randomKakaoId));

        return ResponseEntity.ok()
                .body(response);
    }

}
