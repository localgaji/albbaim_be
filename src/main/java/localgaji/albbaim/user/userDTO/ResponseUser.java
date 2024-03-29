package localgaji.albbaim.user.userDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import localgaji.albbaim.user.User;
import localgaji.albbaim.user.UserType;
import lombok.Getter;

public class ResponseUser {
    @Getter
    @Schema(description = "내 정보 조회")
    public static class GetMyInfoResponse {
        @Schema(description = "회원 이름")
        String userName;
        @Schema(description = "회원 타입")
        UserType userType;
        public GetMyInfoResponse(User user) {
            this.userName = user.getUserName();
            this.userType = user.getUserType();
        }
    }
}
