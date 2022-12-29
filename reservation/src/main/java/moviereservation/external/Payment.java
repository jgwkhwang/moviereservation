package moviereservation.external;

import lombok.Data;
import java.util.Date;
@Data
public class Payment {

    private String paymentId;
    private String movieId;
    private String approveDate;
    private Integer amount;
    private String status;
    private String qty;
    private String reservId;
}


