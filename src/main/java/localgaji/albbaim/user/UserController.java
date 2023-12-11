package localgaji.albbaim.user;

import localgaji.albbaim.__core__.ApiUtil;
import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import localgaji.albbaim.user.userDTO.RequestAuth;
import localgaji.albbaim.user.userDTO.ResponseAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController @RequiredArgsConstructor
@Tag(name = "회원", description = "로그인/회원가입/회원 정보 관련 API")
public class UserController {

    private final LoginService loginService;
    
    @GetMapping("/api/test")
    @Operation(summary = "테스트", description = "ok 테스트")
    public ResponseEntity<ApiUtil.Response<String>> test() {
        return ResponseEntity.ok()
                .body(ApiUtil.success(null));
    }

    @GetMapping("/api/errorTest")
    @Operation(summary = "테스트", description = "에러 테스트")
    public ResponseEntity<ApiUtil.Response<String>> errorTest() {
        throw new CustomException(ErrorType.NOT_OUR_MEMBER);
    }

    @PostMapping("/api/auth/login")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인")
    public ResponseEntity<ApiUtil.Response<ResponseAuth.PostLoginResponse>> postKakaoLogin(@RequestBody RequestAuth.LoginRequest requestBody) {
        return loginService.kakaoLogin(requestBody);
    }

    @PostMapping("/api/auth/join")
    @Operation(summary = "카카오 회원가입", description = "카카오 회원가입")
    public ResponseEntity<ApiUtil.Response<ResponseAuth.PostLoginResponse>> postKakaoSignUp(@RequestBody RequestAuth.SignUpRequest requestBody) {
        return loginService.kakaoSignUp(requestBody);
    }
}
