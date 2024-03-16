package localgaji.albbaim.auth.oauth.kakaoAuth;

import jakarta.validation.constraints.NotNull;
import localgaji.albbaim.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "kakao_auth")
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class KakaoAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") @NotNull
    private User user;

    @Column @NotNull
    private Long kakaoId;

}
