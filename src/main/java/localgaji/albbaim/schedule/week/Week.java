package localgaji.albbaim.schedule.week;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import localgaji.albbaim.schedule.date.Date;
import localgaji.albbaim.workplace.Workplace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "week") @Hidden
@Getter @Builder @AllArgsConstructor @NoArgsConstructor  @DynamicInsert
public class Week {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weekId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workplace_id")
    private Workplace workplace;

    @Column
    private LocalDate startWeekDate;

    @OneToMany(mappedBy = "week") @Builder.Default @NotNull
    private List<Date> dateList = new ArrayList<>();

    @Column @Builder.Default
    private Boolean hasFixed = false;

    public void fixWeekly() {
        this.hasFixed = true;
    }

    public void addWeekToWorkplace() {
        this.workplace.getWeekList().add(this);
    }

}
