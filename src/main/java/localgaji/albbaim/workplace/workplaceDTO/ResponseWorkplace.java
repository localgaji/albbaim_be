package localgaji.albbaim.workplace.workplaceDTO;

import localgaji.albbaim.user.User;
import localgaji.albbaim.workplace.Workplace;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ResponseWorkplace {
    @Builder
    @Schema(description = "내 매장 조회")
    public record GetMyWorkplaceResponse(
            @Schema(description = "내 매장 이름")
            String workplaceName,
            @Schema(description = "내 매장 멤버 리스트")
            List<UserListDTO> members) {

        @Getter
        public static class UserListDTO {
            Long userId;
            String userName;

            public UserListDTO(User user) {
                this.userId = user.getUserId();
                this.userName = user.getUserName();
            }
        }
    }

    @Schema(description = "초대링크 발급")
    public record GetInvitationKeyResponse(
            @Schema(description = "초대키")
            String invitationKey
    ) {
    }

    @Schema(description = "초대장으로 그룹 정보 조회") @Getter
    public static class GetInvitationInfoResponse {
        @Schema(description = "매장 이름")
        public String workplaceName;

        public GetInvitationInfoResponse(Workplace workplace) {
            this.workplaceName = workplace.getWorkplaceName();
        }
    }

}
