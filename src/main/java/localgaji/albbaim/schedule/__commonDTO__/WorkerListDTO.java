package localgaji.albbaim.schedule.__commonDTO__;

import localgaji.albbaim.schedule.workTime.DTO.WorkTimeHeadCountDTO;
import localgaji.albbaim.schedule.workTime.WorkTime;
import localgaji.albbaim.user.User;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder @Getter
public class WorkerListDTO extends WorkTimeHeadCountDTO {
    List<Worker> workerList;

    public record Worker(
            Long userId,
            String userName
    ) {
        public Worker(User user) {
            this(user.getUserId(), user.getUserName());
        }
    }
    public WorkerListDTO(WorkTime workTime, List<Worker> workerList) {
        super(workTime.getWorkTimeName(),
                workTime.getStartTime().toString(),
                workTime.getEndTime().toString(),
                workTime.getHeadcount()
        );
        this.workerList = workerList;
    }
}
