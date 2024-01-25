package localgaji.albbaim.schedule.date;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class DateService {
    private final DateRepository dateRepository;

    // date 엔티티 저장
    @Transactional
    public void createNewDate(Date date) {
        dateRepository.save(date);
        date.addDateToWeek();
    }
}
