package localgaji.albbaim.workplace;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import localgaji.albbaim.schedule.week.Week;
import localgaji.albbaim.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "workplace")
@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class Workplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workplaceId;

    @Column
    private String workplaceName;

    @Column
    private String workplaceNumber;

    @Column
    private String mainAddress;

    @Column
    private String detailAddress;

    @OneToMany(mappedBy = "workplace")
    @Builder.Default @NotNull
    private List<User> userList = new ArrayList<>();

    @OneToMany(mappedBy = "workplace")
    @Builder.Default @NotNull
    private List<Week> weekList = new ArrayList<>();
}
