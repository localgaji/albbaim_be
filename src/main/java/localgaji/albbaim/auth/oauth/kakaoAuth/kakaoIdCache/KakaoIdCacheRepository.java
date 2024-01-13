package localgaji.albbaim.auth.oauth.kakaoAuth.kakaoIdCache;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KakaoIdCacheRepository extends JpaRepository<KakaoIdCache, Long> {
    Optional<KakaoIdCache> findByCode(String code);
}
