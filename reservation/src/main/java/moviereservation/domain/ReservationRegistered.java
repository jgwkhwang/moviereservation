package moviereservation.domain;

import moviereservation.domain.*;
import moviereservation.infra.AbstractEvent;
import java.util.*;
import lombok.*;


@Data
@ToString
public class ReservationRegistered extends AbstractEvent {

    private Long id;
    private Long userId;
    private Long scheduleId;
    private Long paymentId;
    private String status;
    private Date createDate;
    private Date updateDate;

    public ReservationRegistered(Reservation aggregate){
        super(aggregate);
    }
    public ReservationRegistered(){
        super();
    }
}
