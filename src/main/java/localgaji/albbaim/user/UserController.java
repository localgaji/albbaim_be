package localgaji.albbaim.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import localgaji.albbaim.__core__.ApiUtil;
import localgaji.albbaim.auth.__common__.AuthUser;
import localgaji.albbaim.user.userDTO.ResponseUser.GetMyInfoResponse;
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
    public ResponseEntity<ApiUtil.Response<GetMyInfoResponse>> getMyInfo(@AuthUser User user) {
        GetMyInfoResponse responseBody = new GetMyInfoResponse(user);
        return ResponseEntity.ok().body(ApiUtil.success(responseBody));
    }
}
