package localgaji.albbaim.auth.oauth.kakaoAuth.kakaoIdCache;

import jakarta.persistence.*;
import localgaji.albbaim.__core__.BaseTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
@Table(name = "kakao_cache")
public class KakaoIdCache extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String code;

    @Column
    private Long kakaoId;
}
