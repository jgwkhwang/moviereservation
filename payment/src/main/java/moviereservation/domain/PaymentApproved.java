package moviereservation.domain;

import moviereservation.domain.*;
import moviereservation.infra.AbstractEvent;
import java.util.*;
import lombok.*;


@Data
@ToString
public class PaymentApproved extends AbstractEvent {

    private Long id;
    private String approveDate;
    private Integer amount;
    private String status;
    private String qty;
    private String reservId;
    private String paymentId;

    public PaymentApproved(Payment aggregate){
        super(aggregate);
    }
    public PaymentApproved(){
        super();
    }
}
