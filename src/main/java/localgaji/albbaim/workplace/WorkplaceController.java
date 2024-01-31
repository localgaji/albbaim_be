package localgaji.albbaim.workplace;

import localgaji.albbaim.__core__.auth.AuthUser;
import localgaji.albbaim.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static localgaji.albbaim.__core__.ApiUtil.*;
import static localgaji.albbaim.workplace.workplaceDTO.RequestWorkplace.*;
import static localgaji.albbaim.workplace.workplaceDTO.ResponseWorkplace.*;

@RestController @RequiredArgsConstructor
@Tag(name = "3. 매장 설정", description = "매장 설정 / 초대 API")
@RequestMapping("/workplace")
public class WorkplaceController {

    private final WorkplaceService workplaceService;

    @GetMapping
    @Operation(summary = "내 매장 정보 조회")
    public ResponseEntity<Response<GetMyWorkplaceResponse>> getMemberList(@AuthUser User user) {

        GetMyWorkplaceResponse responseBody = workplaceService.findGroupInfo(user);

        return ResponseEntity.ok().body(success(responseBody));
    }

    @PostMapping
    @Operation(summary = "매장 등록")
    public ResponseEntity<Response<String>> postAddGroup(@AuthUser User user,
                                                         @RequestBody PostAddGroupRequest requestBody) {
        workplaceService.addNewWorkplace(user, requestBody);

        return ResponseEntity.ok().body(success(null));
    }

    @GetMapping("/invitation")
    @Operation(summary = "매장 초대키 발급")
    public ResponseEntity<Response<GetInvitationKeyResponse>> getInvitationKey(@AuthUser User user) {

        GetInvitationKeyResponse responseBody = workplaceService.getInvitationKey(user);

        return ResponseEntity.ok().body(success(responseBody));
    }

    @PostMapping("/invitation/{invitationKey}")
    @Operation(summary = "매장 가입")
    public ResponseEntity<Response<String>> postJoinGroup(@AuthUser User user,
                                                          @PathVariable String invitationKey) {

        workplaceService.joinWorkplace(user, invitationKey);

        return ResponseEntity.ok().body(success(null));
    }

    @GetMapping("/invitation/information/{invitationKey}")
    @Operation(summary = "매장 초대 페이지 조회")
    public ResponseEntity<Response<GetInvitationInfoResponse>> getGroupInfo(@PathVariable String invitationKey) {

        GetInvitationInfoResponse responseBody =
                workplaceService.findWorkplaceByInvitationKey(invitationKey);

        return ResponseEntity.ok().body(success(responseBody));
    }
}
