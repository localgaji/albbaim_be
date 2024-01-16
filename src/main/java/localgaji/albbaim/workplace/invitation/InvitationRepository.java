package localgaji.albbaim.workplace.invitation;

import localgaji.albbaim.workplace.Workplace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Optional<Invitation> findByInvitationKey(String invitationKey);
    Optional<Invitation> findByWorkplace(Workplace workplace);
}
