package localgaji.albbaim.schedule.fixed;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import localgaji.albbaim.__core__.ApiUtil.Response;
import localgaji.albbaim.__core__.auth.AuthUser;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static localgaji.albbaim.__core__.ApiUtil.*;
import static localgaji.albbaim.schedule.fixed.DTO.FixedRequest.*;
import static localgaji.albbaim.schedule.fixed.DTO.FixedResponse.*;

@RestController @RequiredArgsConstructor
@Tag(name = "확정 스케줄", description = "스케줄 확정 / 확정 스케줄 조회")
@RequestMapping("/fixed")
public class FixedController {

    private final FixedService fixedService;

    @GetMapping("/dailyWorkers/{selectedDate}")
    @Operation(summary = "일간 확정 근무자 조회")
    public ResponseEntity<Response<GetDailyWorkersResponse>> getDailyWorkers(@AuthUser User user,
                                                                             @PathVariable String selectedDate) {
        GetDailyWorkersResponse response = fixedService.getDailyWorkers(user, selectedDate);
        return ResponseEntity.ok().body(success(response));
    }

    @GetMapping(value = {"/monthly/{year}/{month}/{userId}", "/monthly/{year}/{month}"})
    @Operation(summary = "월간 확정 스케줄 조회")
    public ResponseEntity<Response<GetMonthlyResponse>> getMonthly(@AuthUser User user,
                                                                   @PathVariable Integer year,
                                                                   @PathVariable Integer month,
                                                                   @PathVariable(required = false) Long userId) {
        GetMonthlyResponse response = fixedService.getMonthlyFixed(user, year, month);
        return ResponseEntity.ok().body(success(response));
    }

    @PostMapping("/{startWeekDate}")
    @Operation(summary = "스케줄 확정하기", description = "모집 마감: 추천 스케줄 중 선택")
    public ResponseEntity<Response<String>> postRecommends(@AuthUser User user,
                                                           @PathVariable String startWeekDate,
                                                           @RequestBody PostRequest request) {
        fixedService.saveFixed(user, startWeekDate, request);
        return ResponseEntity.ok().body(success(null));
    }

}
