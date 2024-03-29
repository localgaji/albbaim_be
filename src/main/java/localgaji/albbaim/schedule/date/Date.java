package localgaji.albbaim.schedule.date;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.schedule.workTime.WorkTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "date") @Hidden
@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class Date {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week_id") @NotNull
    private Week week;

    @Column @NotNull
    private LocalDate localDate;

    @OneToMany(mappedBy = "date") @Builder.Default @NotNull
    private List<WorkTime> workTimeList = new ArrayList<>();

    public void addDateToWeek() {
        this.week.getDateList().add(this);
    }
}
