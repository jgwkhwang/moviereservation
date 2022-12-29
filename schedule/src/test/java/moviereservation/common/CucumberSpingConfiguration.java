package moviereservation.common;


import moviereservation.ScheduleApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = { ScheduleApplication.class })
public class CucumberSpingConfiguration {
    
}
