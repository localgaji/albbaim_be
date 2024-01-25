package localgaji.albbaim.schedule.workTime;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import localgaji.albbaim.__core__.auth.AuthUser;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static localgaji.albbaim.__core__.ApiUtil.*;
import static localgaji.albbaim.schedule.workTime.DTO.WorkTimeRequest.*;
import static localgaji.albbaim.schedule.workTime.DTO.WorkTimeResponse.*;

@RestController @RequiredArgsConstructor
@Tag(name = "스케줄 모집 : 근무 시간", description = "근무 시간 조회 / 수정")
@RequestMapping("/workTime")
public class WorkTimeController {

    private final WorkTimeService workTimeService;

    @PostMapping
    @Operation(summary = "매니저 스케줄 모집 시작", description = "시간대 정보 저장")
    public ResponseEntity<Response<String>> postOpen(@AuthUser User user,
                                                     PostOpenRequest requestBody) {
        workTimeService.saveWorkTimes(user, requestBody);
        return ResponseEntity.ok().body(success(null));
    }

    @GetMapping
    @Operation(summary = "매니저 스케줄 모집 시작", description = "스케줄 템플릿 조회")
    public ResponseEntity<Response<GetTemplateResponse>> getTemplate(@AuthUser User user) {

        GetTemplateResponse response = workTimeService.getLastWorkTimeTemplate(user);
        return ResponseEntity.ok().body(success(response));
    }

}
