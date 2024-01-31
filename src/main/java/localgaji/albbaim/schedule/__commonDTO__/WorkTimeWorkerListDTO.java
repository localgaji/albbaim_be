package localgaji.albbaim.schedule.__commonDTO__;

import java.util.List;

public class WorkTimeWorkerListDTO extends WorkTimeDTO {
    List<Worker> workerList;

    public record Worker(
            String userId,
            String userName
    ) {
    }
    public WorkTimeWorkerListDTO(String title, String startTime, String endTime, List<Worker> workerList) {
        super(title, startTime, endTime);
        this.workerList = workerList;
    }
}
