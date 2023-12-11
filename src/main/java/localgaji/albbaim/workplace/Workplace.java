package localgaji.albbaim.workplace;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "workplace")
@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class Workplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workplaceId;

    @Column
    private String marketName;

    @Column
    private String marketNumber;

    @Column
    private String mainAddress;

    @Column
    private String detailAddress;
}
