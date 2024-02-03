package localgaji.albbaim.schedule.__commonDTO__;

import localgaji.albbaim.user.User;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class WorkTimeWorkerListDTO extends WorkTimeDTO {
    List<Worker> workerList;

    public record Worker(
            Long userId,
            String userName
    ) {
        public Worker(User user) {
            this(user.getUserId(), user.getUserName());
        }
    }
    public WorkTimeWorkerListDTO(String title, String startTime, String endTime, List<Worker> workerList) {
        super(title, startTime, endTime);
        this.workerList = workerList;
    }
}
