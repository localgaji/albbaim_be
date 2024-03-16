package localgaji.albbaim.schedule.replacement;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import localgaji.albbaim.schedule.fixed.Fixed;
import localgaji.albbaim.schedule.week.Week;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity @Table(name = "replacement") @Hidden
@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class Replacement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replacement_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixed_id") @NotNull
    private Fixed fixed;

    @Column @NotNull @Builder.Default
    private Boolean hasFound = false;

    @Column @NotNull
    private LocalDateTime expirationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week_id") @NotNull
    private Week week;

    public void findReplacement() {
        this.hasFound = true;
    }
    public Boolean hasExpired() {
        return expirationTime.isBefore(LocalDateTime.now());
    }

}
