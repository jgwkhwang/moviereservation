package moviereservation.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Date;
import lombok.Data;


@Entity
@Table(name="Dashboard_table")
@Data
public class Dashboard {

        @Id
        //@GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private String title;
        private Long reviewCnt;
        private Long seatCnt;
        private Long reservationCnt;
        private Long reservId;
        private String reservStatus;
        private String paymentStatus;
        private String payId;


}
