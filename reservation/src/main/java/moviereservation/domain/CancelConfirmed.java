package moviereservation.domain;

import moviereservation.domain.*;
import moviereservation.infra.AbstractEvent;
import java.util.*;
import lombok.*;


@Data
@ToString
public class CancelConfirmed extends AbstractEvent {

    private Long id;
    private Long userId;
    private Long scheduleId;
    private Long paymentId;
    private String status;
    private Date createDate;
    private Date updateDate;
    private String reservId;

    public CancelConfirmed(Reservation aggregate){
        super(aggregate);
    }
    public CancelConfirmed(){
        super();
    }
}