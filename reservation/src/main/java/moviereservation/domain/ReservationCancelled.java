package moviereservation.domain;

import moviereservation.domain.*;
import moviereservation.infra.AbstractEvent;
import java.util.*;
import lombok.*;


@Data
@ToString
public class ReservationCancelled extends AbstractEvent {

    private Long id;
    private String userId;
    private String scheduleId;
    private String paymentId;
    private String status;
    private String reservId;
    private String createDate;
    private String updateDate;

    public ReservationCancelled(Reservation aggregate){
        super(aggregate);
    }
    public ReservationCancelled(){
        super();
    }
}
