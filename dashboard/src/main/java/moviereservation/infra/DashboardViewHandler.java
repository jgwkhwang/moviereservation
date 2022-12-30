package moviereservation.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import moviereservation.config.kafka.KafkaProcessor;
import moviereservation.domain.Dashboard;
import moviereservation.domain.ReservationRegistered;
import moviereservation.domain.ScheduleRegisted;

@Service
public class DashboardViewHandler {

    @Autowired
    private DashboardRepository dashboardRepository;

    // 2. CQRS
    @StreamListener(KafkaProcessor.INPUT)
    public void whenReservationRegistered_then_CREATE_1 (@Payload ReservationRegistered reservationRegistered) {
        // CQRS-1. ReservationRegistered 이벤트 발생 시
        try {
            System.out.println("dashboard view handler try block");
            if (!reservationRegistered.validate()) return;
            // CQRS-1. ReservationRegistered의 id, status를 dashboard에 저장
            Dashboard dashboard = new Dashboard();
            dashboard.setReservId(Long.valueOf(reservationRegistered.getId()));
            dashboard.setReservStatus(reservationRegistered.getStatus());
            dashboardRepository.save(dashboard);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /* @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentApproved_then_CREATE_1 (@Payload PaymentApproved paymentApproved) {
        try {

            if (!reservationRegistered.validate()) return;

            Dashboard dashboard = new Dashboard();
            dashboard.setPaymentId(Long.valueOf(paymentApproved.getPayId()));
            dashboard.setPaymentStatus(paymentApproved.getStatus());
            dashboardRepository.save(dashboard);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenPaymentApproved_then_UPDATE_1 (@Payload PaymentApproved paymentApproved) {
        try {

            if (!paymentApproved.validate()) return;

            Dashboard dashboard = new Dashboard();
            dashboard.setPaymentStatus(paymentApproved.getStatus());
            dashboardRepository.save(dashboard);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenScheduleRegisted_then_CREATE_1 (@Payload ScheduleRegisted scheduleRegisted){
        try {

            if (!scheduleRegisted.validate()) return;

            // view 객체 생성
            Dashboard dashboard = new Dashboard();
            // view 객체에 이벤트의 Value 를 set 함
            dashboard.setMovieId(Long.valueOf(scheduleRegisted.getMovieId()));
            dashboard.setTitle(scheduleRegisted.getTitle());
            dashboard.setSeatCnt(scheduleRegisted.getSeatCnt());
            // view 레파지 토리에 save
            dashboardRepository.save(dashboard);

        }catch (Exception e){
            e.printStackTrace();
        }
    } */
    


}

