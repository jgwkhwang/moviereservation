package moviereservation.infra;
import moviereservation.domain.*;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;

@Component
public class ReviewHateoasProcessor implements RepresentationModelProcessor<EntityModel<Review>>  {

    @Override
    public EntityModel<Review> process(EntityModel<Review> model) {

        
        return model;
    }
    
}
