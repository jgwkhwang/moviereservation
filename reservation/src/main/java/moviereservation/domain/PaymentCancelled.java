package moviereservation.domain;

import moviereservation.domain.*;
import moviereservation.infra.AbstractEvent;
import lombok.*;
import java.util.*;
@Data
@ToString
public class PaymentCancelled extends AbstractEvent {

    private Long id;
    private String approveDate;
    private Integer amount;
    private String status;
    private String qty;
    private String reservId;
    private String paymentId;
}


