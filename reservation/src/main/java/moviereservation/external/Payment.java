package moviereservation.external;

import lombok.Data;
import java.util.Date;
@Data
public class Payment {

    private Long id;
    private String approveDate;
    private Integer amount;
    private String status;
    private String qty;
    private String reservId;
    private String paymentId;
}


