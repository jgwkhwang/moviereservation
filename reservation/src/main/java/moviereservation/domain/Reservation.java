package moviereservation.domain;

import moviereservation.domain.ReservationRegistered;
import moviereservation.domain.ReservationCancelled;
import moviereservation.domain.ReservationConfirmed;
import moviereservation.domain.CancelConfirmed;
import moviereservation.ReservationApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;


@Entity
@Table(name="Reservation_table")
@Data

public class Reservation  {


    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    
    private Long id;
    
    
    
    
    
    private Long userId;
    
    
    
    
    
    private Long scheduleId;
    
    
    
    
    
    private Long paymentId;
    
    
    
    
    
    private String status;
    
    
    
    
    
    private Date createDate;
    
    
    
    
    
    private Date updateDate;
    
    
    
    
    
    private String reservId;

    @PostPersist
    public void onPostPersist(){

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.


        moviereservation.external.Payment payment = new moviereservation.external.Payment();
        // mappings goes here
        ReservationApplication.applicationContext.getBean(moviereservation.external.PaymentService.class)
            .approvePayment(payment);


        ReservationRegistered reservationRegistered = new ReservationRegistered(this);
        reservationRegistered.publishAfterCommit();



        ReservationCancelled reservationCancelled = new ReservationCancelled(this);
        reservationCancelled.publishAfterCommit();



        ReservationConfirmed reservationConfirmed = new ReservationConfirmed(this);
        reservationConfirmed.publishAfterCommit();



        CancelConfirmed cancelConfirmed = new CancelConfirmed(this);
        cancelConfirmed.publishAfterCommit();

    }

    public static ReservationRepository repository(){
        ReservationRepository reservationRepository = ReservationApplication.applicationContext.getBean(ReservationRepository.class);
        return reservationRepository;
    }




    public static void confirmReservation(PaymentApproved paymentApproved){

        /** Example 1:  new item 
        Reservation reservation = new Reservation();
        repository().save(reservation);

        */

        /** Example 2:  finding and process
        
        repository().findById(paymentApproved.get???()).ifPresent(reservation->{
            
            reservation // do something
            repository().save(reservation);


         });
        */

        
    }
    public static void confirmCancel(PaymentCancelled paymentCancelled){

        /** Example 1:  new item 
        Reservation reservation = new Reservation();
        repository().save(reservation);

        */

        /** Example 2:  finding and process
        
        repository().findById(paymentCancelled.get???()).ifPresent(reservation->{
            
            reservation // do something
            repository().save(reservation);


         });
        */

        
    }


}
