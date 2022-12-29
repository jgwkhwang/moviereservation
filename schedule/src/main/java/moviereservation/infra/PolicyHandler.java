package moviereservation.infra;

import javax.naming.NameParser;

import javax.naming.NameParser;
import javax.transaction.Transactional;

import moviereservation.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import moviereservation.domain.*;

@Service
@Transactional
public class PolicyHandler{
    @Autowired ScheduleRepository scheduleRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}

    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='PaymentApproved'")
    public void wheneverPaymentApproved_UpdateSaleCnt(@Payload PaymentApproved paymentApproved){

        PaymentApproved event = paymentApproved;
        System.out.println("\n\n##### listener UpdateSaleCnt : " + paymentApproved + "\n\n");


        

        // Sample Logic //
        Schedule.updateSaleCnt(event);
        

        

    }
    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='PaymentCancelled'")
    public void wheneverPaymentCancelled_UpdateSaleCnt(@Payload PaymentCancelled paymentCancelled){

        PaymentCancelled event = paymentCancelled;
        System.out.println("\n\n##### listener UpdateSaleCnt : " + paymentCancelled + "\n\n");


        

        // Sample Logic //
        Schedule.updateSaleCnt(event);
        

        

    }

}


