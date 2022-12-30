package moviereservation.domain;

import moviereservation.domain.*;
import moviereservation.infra.AbstractEvent;
import lombok.*;
import java.util.*;
@Data
@ToString
public class ReservationCancelled extends AbstractEvent {

    private Long id;
    private String userId;
    private String scheduleId;
    private String status;
    private String createDate;
    private String updateDate;
    private String reservId;
}


