package moviereservation.domain;

import moviereservation.domain.*;
import moviereservation.infra.AbstractEvent;
import lombok.*;
import java.util.*;
@Data
@ToString
public class ReservationCancelled extends AbstractEvent {

    private Long id;
    private Long userId;
    private Long scheduleId;
    private Long paymentId;
    private String status;
    private Date updateDate;
    private Date createDate;
    private String reservId;
}


