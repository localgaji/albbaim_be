package localgaji.albbaim.auth.user;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import localgaji.albbaim.__core__.BaseTime;
import localgaji.albbaim.workplace.Workplace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "userTable") @Hidden
@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    private Boolean isAdmin;

    @NotNull
    private String userName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workplace_id") @Nullable
    private Workplace workplace;

    @Nullable
    private String profileImg;

    public void updateGroup(Workplace workplace) {
        this.workplace = workplace;
    }

    public void updateProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public UserType getUserType() {
        if (isAdmin) {
            if (workplace == null) {
                return UserType.ADMIN_NO_GROUP;
            } else {
                return UserType.ADMIN;
            }
        } else {
            if (workplace == null) {
                return UserType.ALBA_NO_GROUP;
            } else {
                return UserType.ALBA;
            }
        }
    }
}