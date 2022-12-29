package moviereservation.infra;
import moviereservation.domain.*;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;
import org.springframework.hateoas.EntityModel;

@Component
public class ReservationHateoasProcessor implements RepresentationModelProcessor<EntityModel<Reservation>>  {

    @Override
    public EntityModel<Reservation> process(EntityModel<Reservation> model) {

        
        return model;
    }
    
}
