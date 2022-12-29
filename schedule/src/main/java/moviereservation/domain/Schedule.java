package moviereservation.domain;

import moviereservation.domain.ScheduleRegisted;
import moviereservation.domain.ScheduleUpdate;
import moviereservation.domain.ScheduleDeleted;
import moviereservation.domain.UpdateSaleCnt;
import moviereservation.ScheduleApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;


@Entity
@Table(name="Schedule_table")
@Data

public class Schedule  {


    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    
    private Long id;
    
    
    
    
    
    private String movieId;
    
    
    
    
    
    private String title;
    
    
    
    
    
    private Long price;
    
    
    
    
    
    private Long seatCnt;
    
    
    
    
    
    private Date startDate;
    
    
    
    
    
    private Date endDate;
    
    
    
    
    
    private Long saleCnt;
    
    
    
    
    
    private String theather;
    
    
    
    
    
    private String scheduleId;

    @PostPersist
    public void onPostPersist(){


        ScheduleRegisted scheduleRegisted = new ScheduleRegisted(this);
        scheduleRegisted.publishAfterCommit();



        ScheduleUpdate scheduleUpdate = new ScheduleUpdate(this);
        scheduleUpdate.publishAfterCommit();



        ScheduleDeleted scheduleDeleted = new ScheduleDeleted(this);
        scheduleDeleted.publishAfterCommit();

    }
    @PostUpdate
    public void onPostUpdate(){


        UpdateSaleCnt updateSaleCnt = new UpdateSaleCnt(this);
        updateSaleCnt.publishAfterCommit();

    }

    public static ScheduleRepository repository(){
        ScheduleRepository scheduleRepository = ScheduleApplication.applicationContext.getBean(ScheduleRepository.class);
        return scheduleRepository;
    }




    public static void updateSaleCnt(PaymentApproved paymentApproved){

        /** Example 1:  new item 
        Schedule schedule = new Schedule();
        repository().save(schedule);

        */

        /** Example 2:  finding and process
        
        repository().findById(paymentApproved.get???()).ifPresent(schedule->{
            
            schedule // do something
            repository().save(schedule);


         });
        */

        
    }
    public static void updateSaleCnt(PaymentCancelled paymentCancelled){

        /** Example 1:  new item 
        Schedule schedule = new Schedule();
        repository().save(schedule);

        */

        /** Example 2:  finding and process
        
        repository().findById(paymentCancelled.get???()).ifPresent(schedule->{
            
            schedule // do something
            repository().save(schedule);


         });
        */

        
    }


}
