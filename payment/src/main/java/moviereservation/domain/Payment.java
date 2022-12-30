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
    
    
    
    
    
    private Long id;
    
    
    
    
    
    private String approveDate;
    
    
    
    
    
    private Integer amount;
    
    
    
    
    
    private String status;
    
    
    
    
    
    private String qty;
    
    
    
    
    
    private String reservId;
    
    
    
    
    
    private String payId;

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

        /** Example 1:  new item 
        Payment payment = new Payment();
        repository().save(payment);

        PaymentCancelled paymentCancelled = new PaymentCancelled(payment);
        paymentCancelled.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(reservationCancelled.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);

            PaymentCancelled paymentCancelled = new PaymentCancelled(payment);
            paymentCancelled.publishAfterCommit();

         });
        */

        
    }


}
