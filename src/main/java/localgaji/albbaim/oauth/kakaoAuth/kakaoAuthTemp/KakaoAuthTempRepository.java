package localgaji.albbaim.oauth.kakaoAuth.kakaoAuthTemp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoAuthTempRepository extends JpaRepository<KakaoAuthTemp, Long> {
    Optional<KakaoAuthTemp> findByCode(String code);
}
