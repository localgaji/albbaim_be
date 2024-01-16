package localgaji.albbaim.auth.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import localgaji.albbaim.__core__.ApiUtil;
import localgaji.albbaim.__core__.auth.AuthUser;
import localgaji.albbaim.auth.user.userDTO.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "회원 정보", description = "회원 정보 조회/수정 관련 API")
@RequestMapping("/user")
public class UserController {
    @GetMapping
    @Operation(summary = "내 정보 조회")
    public ResponseEntity<ApiUtil.Response<ResponseUser.GetMyInfoResponse>> getMyInfo(@AuthUser User user) {
        ResponseUser.GetMyInfoResponse responseBody = new ResponseUser.GetMyInfoResponse(user);
        return ResponseEntity.ok().body(ApiUtil.success(responseBody));
    }
}
