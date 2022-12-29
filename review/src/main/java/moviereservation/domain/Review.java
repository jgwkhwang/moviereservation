package moviereservation.domain;

import moviereservation.domain.ReviewUpdated;
import moviereservation.domain.ReviewDeleted;
import moviereservation.domain.ReviewRegistered;
import moviereservation.ReviewApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;


@Entity
@Table(name="Review_table")
@Data

public class Review  {


    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    
    private Long reviewId;
    
    
    
    
    
    private Long movieId;
    
    
    
    
    
    private Long userId;
    
    
    
    
    
    private String content;
    
    
    
    
    
    private Integer rating;
    
    
    
    
    
    private String status;
    
    
    
    
    
    private Date createDate;
    
    
    
    
    
    private Date updateDate;

    @PostPersist
    public void onPostPersist(){


        ReviewUpdated reviewUpdated = new ReviewUpdated(this);
        reviewUpdated.publishAfterCommit();



        ReviewDeleted reviewDeleted = new ReviewDeleted(this);
        reviewDeleted.publishAfterCommit();



        ReviewRegistered reviewRegistered = new ReviewRegistered(this);
        reviewRegistered.publishAfterCommit();

    }

    public static ReviewRepository repository(){
        ReviewRepository reviewRepository = ReviewApplication.applicationContext.getBean(ReviewRepository.class);
        return reviewRepository;
    }






}
