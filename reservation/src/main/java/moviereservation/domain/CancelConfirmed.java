package moviereservation.domain;

import moviereservation.domain.*;
import moviereservation.infra.AbstractEvent;
import java.util.*;
import lombok.*;


@Data
@ToString
public class CancelConfirmed extends AbstractEvent {

    private Long id;

    public CancelConfirmed(Reservation aggregate){
        super(aggregate);
    }
    public CancelConfirmed(){
        super();
    }
}
