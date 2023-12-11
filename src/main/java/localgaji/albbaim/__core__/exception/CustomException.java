package localgaji.albbaim.__core__.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class CustomException extends RuntimeException {
    public ErrorType errorType;
}