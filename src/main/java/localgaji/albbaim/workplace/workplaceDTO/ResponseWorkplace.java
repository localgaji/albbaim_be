package localgaji.albbaim.workplace.workplaceDTO;

import localgaji.albbaim.auth.user.User;
import localgaji.albbaim.workplace.Workplace;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ResponseWorkplace {
    @Builder
    @Schema(description = "내 그룹 조회")
    public record GetMyWorkplaceResponse(
            @Schema(description = "내 그룹 이름")
            String groupName,
            @Schema(description = "내 그룹 멤버 리스트")
            List<UserListDTO> members) {

        @Getter
        public static class UserListDTO {
            Long userId;
            String userName;
            Boolean isAdmin;

            public UserListDTO(User user) {
                this.userId = user.getUserId();
                this.userName = user.getUserName();
                this.isAdmin = user.getIsAdmin();
            }
        }
    }

    @Schema(description = "초대링크 발급")
    public record GetInvitationKeyResponse(
            @Schema(description = "초대키")
            String invitationKey
    ) {
    }

    @Schema(description = "초대장으로 그룹 정보 조회")
    public static class GetInvitationInfoResponse {
        @Schema(description = "매장 이름")
        public String marketName;

        public GetInvitationInfoResponse(Workplace workplace) {
            this.marketName = workplace.getMarketName();
        }
    }

}
