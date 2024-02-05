package localgaji.albbaim.schedule.application;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import localgaji.albbaim.__core__.BaseTime;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "application") @Hidden
@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class Application extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workTime_id")
    private WorkTime workTime;

    public void applyToWorkTime() {
        workTime.getApplicationList().add(this);
    }

    public void deleteInWorkTime() {
        workTime.getApplicationList().remove(this);
    }
}
