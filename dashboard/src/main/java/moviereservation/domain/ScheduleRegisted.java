package moviereservation.domain;

import moviereservation.infra.AbstractEvent;
import lombok.Data;
import java.util.*;


@Data
public class ScheduleRegisted extends AbstractEvent {

    private Long id;
    private String movieId;
    private String title;
    private Long price;
    private Long seatCnt;
    private Date startDate;
    private Date endDate;
    private Long saleCnt;
    private String theather;
    private String scheduleId;
}
