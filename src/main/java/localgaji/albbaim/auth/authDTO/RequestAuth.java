package localgaji.albbaim.auth.authDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import localgaji.albbaim.auth.user.User;

public class RequestAuth {
    @Schema(description = "카카오 로그인")
    public record LoginRequest(
        @Schema(description = "카카오 인가코드")
        String code
    ) {
    }

    @Schema(description = "카카오 회원가입")
    public record SignUpRequest(
        @Schema(description = "회원 이름")
        String userName,
        @Schema(description = "회원 타입")
        Boolean isAdmin,
        @Schema(description = "카카오 인가코드")
        String code
    ) {
        public User toEntity() {
            return User.builder()
                    .userName(userName)
                    .isAdmin(isAdmin)
                    .build();
        }
    }

}
