package localgaji.albbaim.user;

import localgaji.albbaim.workplace.Workplace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

@Entity @Table(name = "member")
@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    private Boolean isAdmin;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private String userName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = true) @Nullable
    private Workplace workplace;

    private String profileImg;

    public void updateGroup(Workplace workplace) {
        this.workplace = workplace;
    }

    public void updateProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}