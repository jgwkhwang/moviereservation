package moviereservation.domain;

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
    }
    @PreRemove
    public void onPreRemove(){
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
