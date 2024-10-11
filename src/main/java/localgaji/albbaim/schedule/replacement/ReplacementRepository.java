package localgaji.albbaim.schedule.replacement;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReplacementRepository extends JpaRepository<Replacement, Long> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Replacement r where r.replacement_id = :replacement_id")
    Optional<Replacement> findWithPessimisticLock(Long replacement_id);

}
