package localgaji.albbaim.auth.user.userDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import localgaji.albbaim.auth.user.User;
import localgaji.albbaim.auth.user.UserType;
import lombok.Getter;

public class ResponseUser {
    @Getter
    @Schema(description = "내 정보 조회")
    public static class GetMyInfoResponse {
        @Schema(description = "회원 이름")
        String username;
        @Schema(description = "회원 타입")
        UserType userType;
        public GetMyInfoResponse(User user) {
            this.username = user.getUserName();
            this.userType = user.getUserType();
        }
    }
}
