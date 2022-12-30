package moviereservation.domain;

import moviereservation.infra.AbstractEvent;
import lombok.Data;
import java.util.*;


@Data
public class ReservationRegistered extends AbstractEvent {

    private Long id;
    private String userId;
    private Long scheduleId;
    private Long paymentId;
    private String status;
    private String createDate;
    private String updateDate;
}