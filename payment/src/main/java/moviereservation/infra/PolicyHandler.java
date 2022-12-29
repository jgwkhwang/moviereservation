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
    @Autowired PaymentRepository paymentRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}

    // 1. Saga
    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='ReservationCancelled'")
    public void wheneverReservationCancelled_CancelPayment(@Payload ReservationCancelled reservationCancelled){

        System.out.println("\n\n##### listener CancelPayment : " + reservationCancelled + "\n\n");
        try {
            if(!reservationCancelled.validate()) return;
            // Saga-1. cancelPayment Policy 호출
            Payment.cancelPayment(reservationCancelled);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}


