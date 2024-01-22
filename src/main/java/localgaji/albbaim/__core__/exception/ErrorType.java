package localgaji.albbaim.__core__.exception;

import lombok.Getter;

@Getter
public enum ErrorType {

    ETC_ERROR(400, -10001, "기타 에러"),
    KAKAO_AUTH_FAIL(400, -10001, "카카오 인증 실패"),
    INVALID_TOKEN(400, -21000, "유효하지 않은 토큰"),
    EXPIRED_TOKEN(400, -21000, "만료된 토큰"),
    GROUP_NOT_FOUND(404, -10001, "매장 정보 없음"),
    MEMBER_NOT_FOUND(404, -10006, "유저 정보 없음"),
    INVALID_INVITATION(404, -20004, "유효하지 않은 초대장"),
    GROUP_ALREADY(400, -10001, "매장 이미 있음"),
    DAILY_NOT_FOUND(404, -11001, "");


    private final int statusCode;
    private final int internalCode;
    private final String errorMessage;

    ErrorType(int statusCode, int internalCode, String errorMessage) {
        this.statusCode = statusCode;
        this.internalCode = internalCode;
        this.errorMessage = errorMessage;
    }
}