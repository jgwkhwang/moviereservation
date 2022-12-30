package moviereservation.domain;

import moviereservation.domain.ReservationRegistered;
import moviereservation.domain.ReservationConfirmed;
import moviereservation.domain.CancelConfirmed;
import moviereservation.domain.ReservationCancelled;
import moviereservation.ReservationApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;


@Entity
@Table(name="Reservation_table")
@Data

public class Reservation  {


    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    
    private Long id;
    
    
    
    
    
    private String userId;
    
    
    
    
    
    private String scheduleId;
    
    
    
    
    
    private String status;
    
    
    
    
    
    private String createDate;
    
    
    
    
    
    private String updateDate;
    
    
    
    
    
    private String reservId;

    // 5. CircuitBreaker
    @PrePersist
    public void onPrePersist(){

        // command 
        ReservationRegistered reservationRegistered = new ReservationRegistered(this);
        reservationRegistered.publishAfterCommit();

        ReservationCancelled reservationCancelled = new ReservationCancelled(this);
        reservationCancelled.publishAfterCommit();
    }

    @PostPersist
    public void onPostPersist() {
        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        moviereservation.external.Payment payment = new moviereservation.external.Payment();
        payment.setPayId("P_" + String.valueOf(getId()));
        payment.setReservId(getReservId());
        payment.setStatus("created");
        payment.setApproveDate("2022-12-29 18:00:00");
        payment.setAmount(12000);
        payment.setQty("1");
        // mappings goes here
        ReservationApplication.applicationContext
            .getBean(moviereservation.external.PaymentService.class)
            .approvePayment(payment);

        ReservationRegistered reservationRegistered = new ReservationRegistered(
            this
        );
        reservationRegistered.publishAfterCommit();

        ReservationConfirmed reservationConfirmed = new ReservationConfirmed(
            this
        );
        reservationConfirmed.publishAfterCommit();

        CancelConfirmed cancelConfirmed = new CancelConfirmed(this);
        cancelConfirmed.publishAfterCommit();
    }
    @PreRemove
    public void onPreRemove(){


        ReservationCancelled reservationCancelled = new ReservationCancelled(this);
        reservationCancelled.publishAfterCommit();

    }

    public static ReservationRepository repository(){
        ReservationRepository reservationRepository = ReservationApplication.applicationContext.getBean(ReservationRepository.class);
        return reservationRepository;
    }




    public static void confirmReservation(PaymentApproved paymentApproved){

        /** Example 1:  new item 
        Reservation reservation = new Reservation();
        repository().save(reservation);

        ReservationConfirmed reservationConfirmed = new ReservationConfirmed(reservation);
        reservationConfirmed.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(paymentApproved.get???()).ifPresent(reservation->{
            
            reservation // do something
            repository().save(reservation);

            ReservationConfirmed reservationConfirmed = new ReservationConfirmed(reservation);
            reservationConfirmed.publishAfterCommit();

         });
        */

        
    }
    public static void confirmCancel(PaymentCancelled paymentCancelled){

        /** Example 1:  new item 
        Reservation reservation = new Reservation();
        repository().save(reservation);

        CancelConfirmed cancelConfirmed = new CancelConfirmed(reservation);
        cancelConfirmed.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(paymentCancelled.get???()).ifPresent(reservation->{
            
            reservation // do something
            repository().save(reservation);

            CancelConfirmed cancelConfirmed = new CancelConfirmed(reservation);
            cancelConfirmed.publishAfterCommit();

         });
        */

        
    }


}
