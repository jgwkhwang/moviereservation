package moviereservation.domain;

import moviereservation.infra.AbstractEvent;
import lombok.Data;
import java.util.*;


@Data
public class PaymentApproved extends AbstractEvent {

    private Long id;
    private String approveDate;
    private Integer amount;
    private String status;
    private String qty;
    private String reservId;
    private String payId;
}
