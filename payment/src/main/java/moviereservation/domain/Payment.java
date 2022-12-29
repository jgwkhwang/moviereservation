package moviereservation.domain;

import moviereservation.domain.PaymentApproved;
import moviereservation.domain.PaymentCancelled;
import moviereservation.PaymentApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;


@Entity
@Table(name="Payment_table")
@Data

public class Payment  {


    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    
    private Long paymentId;
    
    
    
    
    
<<<<<<< HEAD
    private Long scheduleId;
=======
    private String scheduleId;
>>>>>>> origin/template
    
    
    
    
    
    private String approveDate;
    
    
    
    
    
    private Integer amount;
    
    
    
    
    
    private String status;
    
    
    
    
    
    private String qty;
    
    
    
    
    
    private String reservId;

    @PostPersist
    public void onPostPersist(){


        PaymentApproved paymentApproved = new PaymentApproved(this);
        paymentApproved.publishAfterCommit();



        PaymentCancelled paymentCancelled = new PaymentCancelled(this);
        paymentCancelled.publishAfterCommit();

    }

    public static PaymentRepository repository(){
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(PaymentRepository.class);
        return paymentRepository;
    }

    public static void cancelPayment(ReservationCancelled reservationCancelled){

        //Saga-2. ReservationCanclled event에서 넘어온 paymentId(Payment의 PK)로 Payment 레코드 검색
        repository().findById(reservationCancelled.getPaymentId()).ifPresent(payment->{
            //Saga-3. Saga-3에서 검색된 Payment 레코드의 상태를 Cancelled로 변경
            payment.setStatus("Cancelled");
            repository().save(payment);
         });
        
    }


}
