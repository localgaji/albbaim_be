package localgaji.albbaim.workplace.workplaceDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import localgaji.albbaim.workplace.Workplace;
import lombok.Builder;

public class RequestWorkplace {
    @Schema(description = "그룹 생성") @Builder
    public record PostAddGroupRequest (
        @Schema(description = "매장 이름")
        String workplaceName,

        @Schema(description = "사업자 번호")
        String workplaceNumber,

        @Schema(description = "주소1")
        String mainAddress,

        @Schema(description = "주소2")
        String detailAddress
    ) {
        public Workplace toEntity() {
            return Workplace.builder()
                    .workplaceName(this.workplaceName)
                    .workplaceNumber(this.workplaceNumber)
                    .mainAddress(this.mainAddress)
                    .detailAddress(this.detailAddress)
                    .build();
        }
    }
}
