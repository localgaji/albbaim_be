package localgaji.albbaim.workplace;

import localgaji.albbaim.__core__.ApiUtil;
import localgaji.albbaim.__core__.auth.AuthUser;
import localgaji.albbaim.auth.user.User;
import localgaji.albbaim.workplace.workplaceDTO.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequiredArgsConstructor
@Tag(name = "3. 매장 설정", description = "매장 설정 / 초대 API")
@RequestMapping("/workplace")
public class WorkplaceController {

    private final WorkplaceService workplaceService;

    @GetMapping
    @Operation(summary = "내 매장 정보 조회")
    public ResponseEntity<ApiUtil.Response<ResponseWorkplace.GetMyWorkplaceResponse>> getMemberList(
            @AuthUser User user) {

        ResponseWorkplace.GetMyWorkplaceResponse responseBody = workplaceService.findGroupInfo(user);

        return ResponseEntity.ok().body(ApiUtil.success(responseBody));
    }

    @PostMapping
    @Operation(summary = "매장 등록")
    public ResponseEntity<ApiUtil.Response<String>> postAddGroup(
            @RequestBody RequestWorkplace.PostAddGroupRequest requestBody,
            @AuthUser User user) {

        workplaceService.addNewWorkplace(user, requestBody);

        return ResponseEntity.ok().body(ApiUtil.success(null));
    }

    @PostMapping("/invitation")
    @Operation(summary = "매장 가입")
    public ResponseEntity<ApiUtil.Response<String>> postJoinGroup(
            @RequestBody RequestWorkplace.PostJoinGroupRequest requestBody,
            @AuthUser User user) {

        workplaceService.joinWorkplace(user, requestBody);

        return ResponseEntity.ok().body(ApiUtil.success(null));
    }

    @GetMapping("/invitation")
    @Operation(summary = "매장 초대키 발급")
    public ResponseEntity<ApiUtil.Response<ResponseWorkplace.GetInvitationKeyResponse>> getInvitationKey(
            @AuthUser User user) {

        ResponseWorkplace.GetInvitationKeyResponse responseBody = workplaceService.getInvitationKey(user);

        return ResponseEntity.ok().body(ApiUtil.success(responseBody));
    }

    @GetMapping("/invitation/information")
    @Operation(summary = "매장 초대 페이지 조회")
    public ResponseEntity<ApiUtil.Response<ResponseWorkplace.GetInvitationInfoResponse>> getGroupInfo(
            RequestWorkplace.GetGroupInfoRequest requestParams) {

        ResponseWorkplace.GetInvitationInfoResponse responseBody =
                workplaceService.findWorkplaceByInvitationKey(requestParams.invitationKey());

        return ResponseEntity.ok().body(ApiUtil.success(responseBody));
    }
}
