package localgaji.albbaim.workplace.invitation;

import localgaji.albbaim.__core__.BaseTime;
import localgaji.albbaim.workplace.Workplace;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Getter @Builder @AllArgsConstructor @NoArgsConstructor
@Table(name = "invitation") @DynamicInsert
public class Invitation extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invitationId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Workplace workplace;

    @Column(unique = true)
    private String invitationKey;

    @Column
    private LocalDateTime keyUpdatedDate;

    @Column @Builder.Default
    private int durationHours = 720;

    public void updateInvitation(String invitationKey, LocalDateTime keyUpdatedDate) {
        this.invitationKey = invitationKey;
        this.keyUpdatedDate = keyUpdatedDate;
    }

    public void updateDurationHours(int durationHours) {
        this.durationHours = durationHours;
    }

    public boolean isExpired() {
        return keyUpdatedDate.plusHours(durationHours).isBefore(LocalDateTime.now());
    }
}
