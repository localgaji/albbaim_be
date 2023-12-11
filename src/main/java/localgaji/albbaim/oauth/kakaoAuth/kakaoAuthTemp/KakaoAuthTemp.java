package localgaji.albbaim.oauth.kakaoAuth.kakaoAuthTemp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter @Builder @NoArgsConstructor @AllArgsConstructor
@Table(name = "kakao_temp")
public class KakaoAuthTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String code;

    @Column
    private Long kakaoId;
}
