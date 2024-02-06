package localgaji.albbaim.schedule.fixed;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "fixed") @Hidden
@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class Fixed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fixedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workTime_id")
    private WorkTime workTime;

    public void addNewFixed() {
        workTime.getFixedList().add(this);
        user.getFixedList().add(this);
    }
}
