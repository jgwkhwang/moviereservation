package moviereservation.domain;

import moviereservation.domain.*;
import moviereservation.infra.AbstractEvent;
import java.util.*;
import lombok.*;


@Data
@ToString
public class ScheduleUpdate extends AbstractEvent {

    private Long id;
    private String movieId;
    private String title;
    private Long price;
    private Long seatCnt;
    private Date startDate;
    private Date endDate;
    private Long saleCnt;

    public ScheduleUpdate(Schedule aggregate){
        super(aggregate);
    }
    public ScheduleUpdate(){
        super();
    }
}
