package localgaji.albbaim.schedule.workTime;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import localgaji.albbaim.schedule.date.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "workTime") @Hidden
@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class WorkTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workTimeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "date_id")
    private Date date;

    @Column
    private String workTimeName;

    @Column
    private String startTime;

    @Column
    private String endTime;

    @Column
    private int headcount;

    public void addWorkTimeToDate() {
        this.date.getWorkTimeList().add(this);
    }
}
