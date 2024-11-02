package localgaji.albbaim.schedule.replacement;

import jakarta.persistence.LockModeType;
import localgaji.albbaim.schedule.week.Week;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReplacementRepository extends JpaRepository<Replacement, Long> {
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Replacement r where r.replacement_id = :replacement_id")
    Optional<Replacement> findWithPessimisticLock(Long replacement_id);

    Slice<Replacement> findByWeekAndExpirationTimeAfterAndHasFoundFalse(
            Week week,
            LocalDateTime now,
            Pageable pageable
    );

    @Query(value = "SELECT r FROM Replacement r"
            + " JOIN FETCH r.fixed f"
            + " JOIN FETCH f.workTime w"
            + " JOIN FETCH w.date"
            + " WHERE r.week = :week"
            + " AND r.expirationTime > :now"
            + " AND NOT r.hasFound"
    )
    Slice<Replacement> findByWeekFetchJoin(
            Week week,
            LocalDateTime now,
            Pageable pageable
    );
}
