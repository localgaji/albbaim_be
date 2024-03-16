package localgaji.albbaim.schedule.replacement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import localgaji.albbaim.__core__.ApiUtil;
import localgaji.albbaim.auth.__common__.AuthUser;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static localgaji.albbaim.__core__.ApiUtil.success;
import static localgaji.albbaim.schedule.replacement.DTO.ReplacementResponse.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "스케줄 대타", description = "대타 요청 / 신청 / 조회")
@RequestMapping("/replacement")
public class ReplacementController {

    private final ReplacementService replacementService;

    @PostMapping("/{fixedId}")
    @Operation(summary = "대타 구인 시작", description = "대타 모집 중 상태로 변경")
    public ResponseEntity<ApiUtil.Response<String>> postFixed(@AuthUser User user,
                                                              @PathVariable String fixedId) {
        replacementService.findReplacementWorker(user, fixedId);
        return ResponseEntity.ok().body(success(null));
    }

    @PatchMapping("/{replacementId}")
    @Operation(summary = "대타 확정", description = "해당 스케줄의 직원을 변경")
    public ResponseEntity<ApiUtil.Response<String>> patchFixedSave(@AuthUser User user,
                                                                  @PathVariable String replacementId) {
        replacementService.changeWorker(user, replacementId);
        return ResponseEntity.ok().body(success(null));
    }

    @GetMapping("/{startWeekDate}")
    @Operation(summary = "모집중인 대타 리스트를 조회")
    public ResponseEntity<ApiUtil.Response<GetReplacementList>> getReplacementList(@AuthUser User user,
                                                                                   @PathVariable String startWeekDate) {
        GetReplacementList replacementList = replacementService.getReplacementList(user, startWeekDate);
        return ResponseEntity.ok().body(success(replacementList));
    }


}
