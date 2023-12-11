package localgaji.albbaim.user;

import localgaji.albbaim.__core__.BaseTime;
import localgaji.albbaim.workplace.Workplace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity @Table(name = "userTable")
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
    @JoinColumn(name = "workplace_id", nullable = true) @Nullable
    private Workplace workplace;

    private String profileImg;

    public void updateGroup(Workplace workplace) {
        this.workplace = workplace;
    }

    public void updateProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}