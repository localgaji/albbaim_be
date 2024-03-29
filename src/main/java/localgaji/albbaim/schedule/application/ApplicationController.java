package localgaji.albbaim.schedule.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import localgaji.albbaim.auth.__common__.AuthUser;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static localgaji.albbaim.__core__.ApiUtil.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationRequest.*;
import static localgaji.albbaim.schedule.application.DTO.ApplicationResponse.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "스케줄 신청", description = "신청 정보 조회 / 수정")
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final RecommendService recommendService;

    @GetMapping("/{startWeekDate}")
    @Operation(summary = "매니저 : 스케줄 신청자 명단 조회")
    public ResponseEntity<Response<GetApplyStatusResponse>> getApplyStatus(@AuthUser User user,
                                                                           @PathVariable String startWeekDate) {
        GetApplyStatusResponse response = applicationService.getApplicationList(user, startWeekDate);
        return ResponseEntity.ok().body(success(response));
    }
    @GetMapping("/checklist/{startWeekDate}")
    @Operation(summary = "알바 스케줄 신청", description = "체크 리스트 조회")
    public ResponseEntity<Response<GetApplyFormResponse>> getApplyForm(@AuthUser User user,
                                                                       @PathVariable String startWeekDate) {
        GetApplyFormResponse response = applicationService.getApplicationCheckForm(user, startWeekDate);
        return ResponseEntity.ok().body(success(response));
    }
    @PostMapping
    @Operation(summary = "알바 스케줄 신청", description = "신청 정보 저장")
    public ResponseEntity<Response<String>> postApply(@AuthUser User user,
                                                      @RequestBody PostApplyRequest requestBody) {
        applicationService.saveApplication(user, requestBody);
        return ResponseEntity.ok().body(success(null));
    }
    @GetMapping("/recommend/{startWeekDate}")
    @Operation(summary = "매니저 스케줄 모집 마감", description = "추천 후보 조회")
    public ResponseEntity<Response<GetRecommendResponse>> getRecommend(@AuthUser User user,
                                                                        @PathVariable String startWeekDate) {
        GetRecommendResponse response = recommendService.getRecommend(user, startWeekDate);
        return ResponseEntity.ok().body(success(response));
    }
}
