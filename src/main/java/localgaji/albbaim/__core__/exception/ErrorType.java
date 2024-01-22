package localgaji.albbaim.__core__.exception;

import lombok.Getter;

@Getter
public enum ErrorType {

    ETC_ERROR(400, -10001),
    KAKAO_AUTH_FAIL(400, -10001),
    INVALID_TOKEN(400, -21000),
    EXPIRED_TOKEN(400, -21000),
    GROUP_NOT_FOUND(404, -10001),
    MEMBER_NOT_FOUND(404, -10006),
    INVALID_INVITATION(404, -20004),
    GROUP_ALREADY(400, -10001),
    DAILY_NOT_FOUND(404, -11001);


    private final int statusCode;
    private final int internalCode;

    ErrorType(int statusCode, int internalCode) {
        this.statusCode = statusCode;
        this.internalCode = internalCode;
    }
}