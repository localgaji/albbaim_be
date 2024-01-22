package localgaji.albbaim.auth;

import localgaji.albbaim.__core__.ApiUtil;
import localgaji.albbaim.auth.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static localgaji.albbaim.auth.authDTO.RequestAuth.*;
import static localgaji.albbaim.auth.authDTO.ResponseAuth.*;

@RestController @RequiredArgsConstructor
@Tag(name = "로그인/회원가입", description = "로그인/회원가입 관련 API")
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/kakao")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인")
    public ResponseEntity<ApiUtil.Response<PostLoginResponse>> postKakaoLogin(@RequestBody LoginRequest requestBody) {
        User user = authService.kakaoLogin(requestBody);
        return buildLoginResponse(user);
    }

    @PostMapping("/signUp/kakao")
    @Operation(summary = "카카오 회원가입", description = "카카오 회원가입")
    public ResponseEntity<ApiUtil.Response<PostLoginResponse>> postKakaoSignUp(@RequestBody SignUpRequest requestBody) {
        User newUser =  authService.kakaoSignUp(requestBody);
        return buildLoginResponse(newUser);
    }

    private ResponseEntity<ApiUtil.Response<PostLoginResponse>> buildLoginResponse(User user) {
        String token = authService.getToken(user);
        return ResponseEntity.ok()
                .header("Authorization", token)
                .body(ApiUtil.success(
                        new PostLoginResponse(user.getIsAdmin())));
    }

}
