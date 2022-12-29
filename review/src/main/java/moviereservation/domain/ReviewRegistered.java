package moviereservation.domain;

import moviereservation.domain.*;
import moviereservation.infra.AbstractEvent;
import java.util.*;
import lombok.*;


@Data
@ToString
public class ReviewRegistered extends AbstractEvent {

    private Long id;
    private Long reviewId;
    private Long movieId;
    private Long userId;
    private String content;
    private Integer rating;
    private String status;
    private Date createDate;
    private Date updateDate;

    public ReviewRegistered(Review aggregate){
        super(aggregate);
    }
    public ReviewRegistered(){
        super();
    }
}
