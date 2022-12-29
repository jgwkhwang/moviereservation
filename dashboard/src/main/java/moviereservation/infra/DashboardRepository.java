package moviereservation.infra;

import moviereservation.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource(collectionResourceRel="dashboards", path="dashboards")
public interface DashboardRepository extends PagingAndSortingRepository<Dashboard, Long> {

    

    
}
