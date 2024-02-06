package localgaji.albbaim.schedule.workTime;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import localgaji.albbaim.schedule.application.Application;
import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.schedule.fixed.Fixed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    private LocalTime startTime;

    @Column
    private LocalTime endTime;

    @OneToMany(mappedBy = "workTime")
    @Builder.Default @NotNull
    private List<Application> applicationList = new ArrayList<>();

    @OneToMany(mappedBy = "workTime")
    @Builder.Default @NotNull
    private List<Fixed> fixedList = new ArrayList<>();

    @Column
    private int headcount;

    public void addWorkTimeToDate() {
        this.date.getWorkTimeList().add(this);
    }
}
