package localgaji.albbaim.user.userDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class ResponseAuth {
    @Schema(description = "카카오 로그인")
    public record PostLoginResponse(
        @Schema(description = "회원 타입")
        Boolean isAdmin
    ) {
    }
}
