package localgaji.albbaim.__core__.exception;

import lombok.Getter;

@Getter
public enum ErrorType {

    ETC_ERROR(400, -10001, "기타 에러"),

    OAUTH_FAIL(401, -10007, "oauth 인증 실패"),
    INVALID_TOKEN(401, -21000, "유효하지 않은 토큰"),
    FORBIDDEN(403, -21001, "권한 없음"),

    INVALID_INVITATION(404, -20004, "유효하지 않은 초대장"),
    MEMBER_NOT_FOUND(404, -10006, "유저 정보 없음"),
    NOT_FOUND(404, -11001, "없음"),

    ALREADY_HAVE(400, -20001, "이미 있음"),
    ALREADY_CLOSED(400, -20003, "이미 마감");

    private final int statusCode;
    private final int internalCode;
    private final String errorMessage;

    ErrorType(int statusCode, int internalCode, String errorMessage) {
        this.statusCode = statusCode;
        this.internalCode = internalCode;
        this.errorMessage = errorMessage;
    }
}