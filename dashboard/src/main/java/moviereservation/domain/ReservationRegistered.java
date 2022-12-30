package moviereservation.domain;

import moviereservation.infra.AbstractEvent;
import lombok.Data;
import java.util.*;


@Data
public class ReservationRegistered extends AbstractEvent {

    private Long id;
    private String userId;
    private String scheduleId;
    private String status;
    private String createDate;
    private String updateDate;
    private String reservId;
}
