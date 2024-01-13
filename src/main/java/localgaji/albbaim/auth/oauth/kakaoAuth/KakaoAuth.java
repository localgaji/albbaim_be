package localgaji.albbaim.auth.oauth.kakaoAuth;

import localgaji.albbaim.auth.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Entity @Table(name = "kakao_auth")
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class KakaoAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column @NotNull
    private Long kakaoId;

}
