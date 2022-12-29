package moviereservation.domain;

import moviereservation.domain.*;
import moviereservation.infra.AbstractEvent;
import java.util.*;
import lombok.*;


@Data
@ToString
public class ReservationConfirmed extends AbstractEvent {

    private Long id;
    private Long userId;
    private Long scheduleId;
    private Long paymentId;
    private String status;
    private Date createDate;
    private Date updateDate;
    private String reservId;

    public ReservationConfirmed(Reservation aggregate){
        super(aggregate);
    }
    public ReservationConfirmed(){
        super();
    }
}
