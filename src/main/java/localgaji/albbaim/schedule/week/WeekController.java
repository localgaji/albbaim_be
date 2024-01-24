package localgaji.albbaim.schedule.week;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import localgaji.albbaim.__core__.auth.AuthUser;
import localgaji.albbaim.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static localgaji.albbaim.__core__.ApiUtil.*;
import static localgaji.albbaim.schedule.__DTO__.ResponseSchedule.*;

@RestController @RequiredArgsConstructor
@Tag(name = "주차 정보", description = "주차 정보 조회")
@RequestMapping("/week")
public class WeekController {

    private final WeekService weekService;

    @GetMapping("/{startWeekDate}")
    @Operation(summary = "해당 주차의 모집 여부 조회")
    public ResponseEntity<Response<GetWeekStatusResponse>> getWeekStatus(@AuthUser User user,
                                                                         @PathVariable("startWeekDate") String startWeekDate) {
        GetWeekStatusResponse response = new GetWeekStatusResponse(
                weekService.findWeekStatus(user, startWeekDate)
        );
        return ResponseEntity.ok().body(success(response));
    }

}
