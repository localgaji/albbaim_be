package localgaji.albbaim.workplace.invitation;

import jakarta.validation.constraints.NotNull;
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
    @JoinColumn(name = "workplace_id") @NotNull
    private Workplace workplace;

    @Column(unique = true) @NotNull
    private String invitationKey;

    @Column @NotNull
    private LocalDateTime keyUpdatedDate;

    @Column @Builder.Default @NotNull
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
