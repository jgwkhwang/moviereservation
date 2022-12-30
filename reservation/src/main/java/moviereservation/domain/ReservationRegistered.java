package moviereservation.domain;

import moviereservation.domain.*;
import moviereservation.infra.AbstractEvent;
import java.util.*;
import lombok.*;


@Data
@ToString
public class ReservationRegistered extends AbstractEvent {

    private Long id;
    private String userId;
    private String scheduleId;
    private String status;
    private String createDate;
    private String updateDate;
    private String reservId;

    public ReservationRegistered(Reservation aggregate){
        super(aggregate);
    }
    public ReservationRegistered(){
        super();
    }
}
