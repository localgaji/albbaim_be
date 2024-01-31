package localgaji.albbaim.__core__.exception;
import localgaji.albbaim.__core__.ApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(CustomException.class)
    public static ResponseEntity<ApiUtil.Response<String>> handleCustomException(CustomException exception) {
        ErrorType errorType = exception.getErrorType();

        log.error("에러 {}", errorType.getInternalCode());

        HttpStatus httpStatus = HttpStatus.valueOf(errorType.getStatusCode());

        log.error("에러 {}", httpStatus);

        return new ResponseEntity<>(ApiUtil.fail(errorType.getInternalCode()), httpStatus);
    }


}
