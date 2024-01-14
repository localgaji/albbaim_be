package localgaji.albbaim;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import localgaji.albbaim.__core__.ApiUtil;
import localgaji.albbaim.__core__.exception.CustomException;
import localgaji.albbaim.__core__.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "테스트", description = "정상 응답 / 에러 응답 테스트 API")
@RequestMapping("/test")
public class TestController {

    @GetMapping("/response")
    @Operation(summary = "테스트", description = "ok 테스트")
    public ResponseEntity<ApiUtil.Response<String>> test() {
        return ResponseEntity.ok()
                .body(ApiUtil.success(null));
    }

    @GetMapping("/error")
    @Operation(summary = "테스트", description = "에러 테스트")
    public ResponseEntity<ApiUtil.Response<String>> errorTest() {
        throw new CustomException(ErrorType.NOT_OUR_MEMBER);
    }

}
