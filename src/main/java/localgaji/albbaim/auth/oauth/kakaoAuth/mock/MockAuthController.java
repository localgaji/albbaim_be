package localgaji.albbaim.auth.oauth.kakaoAuth.mock;

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
        String token = request.getCode();
        GetTokenResponse response = new GetTokenResponse(token);
        return ResponseEntity.ok()
                .body(response);
    }

    @GetMapping("/v2/user/me")
    @Operation(summary = "아이디 조회")
    public ResponseEntity<GetKakaoIdResponse> getKakaoId(@RequestHeader("Authorization") String token) {
        long kakaoId = kakaoId(token);
        GetKakaoIdResponse response = new GetKakaoIdResponse(String.valueOf(kakaoId));

        return ResponseEntity.ok().body(response);
    }

    private Long kakaoId(String token) {
        for (char c : token.toCharArray()) {
            if (!Character.isDigit(c)) {
                return 1L;
            }
        }
        return Long.parseLong(token);
    }
}

