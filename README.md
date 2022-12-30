# 예제 - 영화예매

본 예제는 MSA/DDD/Event Storming/EDA 를 포괄하는 분석/설계/구현/운영 전단계를 커버하도록 구성한 예제입니다.
이는 클라우드 네이티브 애플리케이션의 개발에 요구되는 체크포인트들을 통과하기 위한 예시 답안을 포함합니다.
- 체크포인트 : https://workflowy.com/s/assessment-check-po/T5YrzcMewfo4J6LW


# Table of contents

- [예제 - 음식배달](#---)
  - [서비스 시나리오](#서비스-시나리오)
  - [체크포인트](#체크포인트)
  - [분석/설계](#분석설계)
  - [구현:](#구현-)
    - [Saga](#Saga)
    - [CQRS](#CQRS)
    - [Compensation Correlation](#Compensation-Correlation)
    - [Request Response](#Request-Response)
    - [Circuit Breaker](#Circuit-Breaker)
  - [운영](#운영)
    - [Gateway Ingress](#Gateway-Ingress)
    - [Deploy Pipeline](#Deploy-Pipeline)
    - [Autoscale](#Autoscale)
    - [Readiness Probe ](#Readiness-Probe )
    - [PV ConfigMap Secret ](#PV-ConfigMap-Secret)
    - [Liveness Probe](#Liveness-Probe  )
    - [Loggregation Monitoring](#Loggregation-Monitoring)
  

# 바운디드 컨텍스트
- 영화 예약(reservation)
- 결재(payment)
- 영화 일정(schedule)
- 영화 후기(review)
- 현황판(Dashboard)


# 서비스 시나리오

기능적 요구사항
1. 매니저가 상영할 영화를 등록/수정/삭제한다.
2. 고객이 예약 가능한 좌석을 선택하여 예약한다.
3. 예약과 동시에 결제(payment) 진행된다.
4. 결제(payment) 완료 되면 티켓좌석이 감소된다.
5. 결제확인(confirm)가 되면 예약 승인내역(Kakao Message)이 전달된다.
6. 고객은 예약을 취소할 수 있다.
7. 결제취소 되면 티켓좌석이 증가된다.
8. 결제취소(cancel)가 되면 예약 취소내역(Kakao Message)이 전달된다.
9. 영화관람 후 후기(review)를 남길 수 있다.
10. 전체적인 영화 정보 및 예약 상태 등을 한 화면에서 확인 할 수 있다.(dashbord)

비기능적 요구사항
1. 트랜잭션
   1. 결제가 되지 않은 예약 건은 성립되지 않아야 한다. (Sync 호출)
2. 장애격리
   1. 영화예약 등록 및 메시지 전송 기능이 수행되지 않더라도 예약은 할수 있어야 한다 Async (event-driven), Eventual Consistency
   2. 예약 시스템이 과중되면 사용자를 잠시동안 받지 않고 잠시 후에 하도록 유도한다 Circuit breaker, fallback
3. 성능
   1.모든 영화 예약정보 및 예약 상태 등을 한번에 확인할 수 있어야 한다 (CQRS)


# 체크포인트

- 분석 설계


  - 이벤트스토밍: 
    - 스티커 색상별 객체의 의미를 제대로 이해하여 헥사고날 아키텍처와의 연계 설계에 적절히 반영하고 있는가?
    - 각 도메인 이벤트가 의미있는 수준으로 정의되었는가?
    - 어그리게잇: Command와 Event 들을 ACID 트랜잭션 단위의 Aggregate 로 제대로 묶었는가?
    - 기능적 요구사항과 비기능적 요구사항을 누락 없이 반영하였는가?    

  - 서브 도메인, 바운디드 컨텍스트 분리
    - 팀별 KPI 와 관심사, 상이한 배포주기 등에 따른  Sub-domain 이나 Bounded Context 를 적절히 분리하였고 그 분리 기준의 합리성이 충분히 설명되는가?
      - 적어도 3개 이상 서비스 분리
    - 폴리글랏 설계: 각 마이크로 서비스들의 구현 목표와 기능 특성에 따른 각자의 기술 Stack 과 저장소 구조를 다양하게 채택하여 설계하였는가?
    - 서비스 시나리오 중 ACID 트랜잭션이 크리티컬한 Use 케이스에 대하여 무리하게 서비스가 과다하게 조밀히 분리되지 않았는가?
  - 컨텍스트 매핑 / 이벤트 드리븐 아키텍처 
    - 업무 중요성과  도메인간 서열을 구분할 수 있는가? (Core, Supporting, General Domain)
    - Request-Response 방식과 이벤트 드리븐 방식을 구분하여 설계할 수 있는가?
    - 장애격리: 서포팅 서비스를 제거 하여도 기존 서비스에 영향이 없도록 설계하였는가?
    - 신규 서비스를 추가 하였을때 기존 서비스의 데이터베이스에 영향이 없도록 설계(열려있는 아키택처)할 수 있는가?
    - 이벤트와 폴리시를 연결하기 위한 Correlation-key 연결을 제대로 설계하였는가?

  - 헥사고날 아키텍처
    - 설계 결과에 따른 헥사고날 아키텍처 다이어그램을 제대로 그렸는가?
    
- 구현
  - [DDD] 분석단계에서의 스티커별 색상과 헥사고날 아키텍처에 따라 구현체가 매핑되게 개발되었는가?
    - Entity Pattern 과 Repository Pattern 을 적용하여 JPA 를 통하여 데이터 접근 어댑터를 개발하였는가
    - [헥사고날 아키텍처] REST Inbound adaptor 이외에 gRPC 등의 Inbound Adaptor 를 추가함에 있어서 도메인 모델의 손상을 주지 않고 새로운 프로토콜에 기존 구현체를 적응시킬 수 있는가?
    - 분석단계에서의 유비쿼터스 랭귀지 (업무현장에서 쓰는 용어) 를 사용하여 소스코드가 서술되었는가?
  - Request-Response 방식의 서비스 중심 아키텍처 구현
    - 마이크로 서비스간 Request-Response 호출에 있어 대상 서비스를 어떠한 방식으로 찾아서 호출 하였는가? (Service Discovery, REST, FeignClient)
    - 서킷브레이커를 통하여  장애를 격리시킬 수 있는가?
  - 이벤트 드리븐 아키텍처의 구현
    - 카프카를 이용하여 PubSub 으로 하나 이상의 서비스가 연동되었는가?
    - Correlation-key:  각 이벤트 건 (메시지)가 어떠한 폴리시를 처리할때 어떤 건에 연결된 처리건인지를 구별하기 위한 Correlation-key 연결을 제대로 구현 하였는가?
    - Message Consumer 마이크로서비스가 장애상황에서 수신받지 못했던 기존 이벤트들을 다시 수신받아 처리하는가?
    - Scaling-out: Message Consumer 마이크로서비스의 Replica 를 추가했을때 중복없이 이벤트를 수신할 수 있는가
    - CQRS: Materialized View 를 구현하여, 타 마이크로서비스의 데이터 원본에 접근없이(Composite 서비스나 조인SQL 등 없이) 도 내 서비스의 화면 구성과 잦은 조회가 가능한가?

  - 폴리글랏 플로그래밍
    - 각 마이크로 서비스들이 하나이상의 각자의 기술 Stack 으로 구성되었는가?
    - 각 마이크로 서비스들이 각자의 저장소 구조를 자율적으로 채택하고 각자의 저장소 유형 (RDB, NoSQL, File System 등)을 선택하여 구현하였는가?
  - API 게이트웨이
    - API GW를 통하여 마이크로 서비스들의 집입점을 통일할 수 있는가?
    - 게이트웨이와 인증서버(OAuth), JWT 토큰 인증을 통하여 마이크로서비스들을 보호할 수 있는가?
- 운영
  - SLA 준수
    - 셀프힐링: Liveness Probe 를 통하여 어떠한 서비스의 health 상태가 지속적으로 저하됨에 따라 어떠한 임계치에서 pod 가 재생되는 것을 증명할 수 있는가?
    - 서킷브레이커, 레이트리밋 등을 통한 장애격리와 성능효율을 높힐 수 있는가?
    - 오토스케일러 (HPA) 를 설정하여 확장적 운영이 가능한가?
    - 모니터링, 앨럿팅: 
  - 무정지 운영 CI/CD (10)
    - Readiness Probe 의 설정과 Rolling update을 통하여 신규 버전이 완전히 서비스를 받을 수 있는 상태일때 신규버전의 서비스로 전환됨을 siege 등으로 증명 
    - Contract Test :  자동화된 경계 테스트를 통하여 구현 오류나 API 계약위반를 미리 차단 가능한가?


# 분석/설계


## AS-IS 조직 (Horizontally-Aligned)
  ![image](https://user-images.githubusercontent.com/487999/79684144-2a893200-826a-11ea-9a01-79927d3a0107.png)

## TO-BE 조직 (Vertically-Aligned)
  ![image](https://user-images.githubusercontent.com/487999/79684159-3543c700-826a-11ea-8d5f-a3fc0c4cad87.png)


## Event Storming 결과
* MSAEz 로 모델링한 이벤트스토밍 결과: https://labs.msaez.io/#/storming/24effc668ce374dd71b64a9c7a4a2d2e
### 완성된 1차 모형
![image](https://user-images.githubusercontent.com/31139303/210032959-fe7d46f6-6d87-4145-9e28-2018f5d88240.png)

## 헥사고날 아키텍처 다이어그램 도출
    
![image](https://user-images.githubusercontent.com/487999/79684772-eba9ab00-826e-11ea-9405-17e2bf39ec76.png)


    - Chris Richardson, MSA Patterns 참고하여 Inbound adaptor와 Outbound adaptor를 구분함
    - 호출관계에서 PubSub 과 Req/Resp 를 구분함
    - 서브 도메인과 바운디드 컨텍스트의 분리:  각 팀의 KPI 별로 아래와 같이 관심 구현 스토리를 나눠가짐


# 구현:

분석/설계 단계에서 도출된 헥사고날 아키텍처에 따라, 각 BC별로 대변되는 마이크로 서비스들을 스프링부트와 파이선으로 구현하였다. 구현한 각 서비스를 로컬에서 실행하는 방법은 아래와 같다 (각자의 포트넘버는 8081 ~ 808n 이다)

```
cd reservation
mvn spring-boot:run

cd payment
mvn spring-boot:run 

cd schedule
mvn spring-boot:run  

cd review
mvn spring-boot:run 

cd dashboard
mvn spring-boot:run 
```

## Saga
- 고객(customer)에 의해 reservation에서 예약이 취소되어 ReservationCancelled 이벤트가 발생할 경우, Kafka MQ를 통해 pub-sub 방식으로 payment에서 cancelPayment가 작동된다.

```
...
# payment/src/main/java/moviereservation/infra/PolicyHandler.java

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
...    
    
```

```
...
    # payment/src/main/java/moviereservation/domain/Payment.java
    public static void cancelPayment(ReservationCancelled reservationCancelled){

        //Saga-2. ReservationCanclled event에서 넘어온 paymentId(Payment의 PK)로 Payment 레코드 검색
        repository().findById(reservationCancelled.getPaymentId()).ifPresent(payment->{
            //Saga-3. Saga-3에서 검색된 Payment 레코드의 상태를 Cancelled로 변경
            payment.setStatus("Cancelled");
            repository().save(payment);
         });        
    }
...
```

## CQRS
- dashboard ReadModel의 데이터 입력/수정/삭제와 조회를 분리한다.
- 예시로 reservation의 ReservationRegistered 이벤트 발생 시 reservation.id, status를 dashboard.reservId, reservStatus에 반영한다.

```
...
  # dashboard/src/main/java/moviereservation/infra/DashboardViewHandler.java

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

...
```

- ReservationRegistered 이벤트 발생

![image](https://user-images.githubusercontent.com/117132766/210033007-6a787fd9-0f42-4e6f-bca6-825f11e5860a.png)

- 위 이벤트 발생에 따라 approvePayment 커맨드를 거쳐 Payment 레코드 생성

![image](https://user-images.githubusercontent.com/117132766/210033018-4c51f155-5582-4f66-9c72-1a3c49bc6f46.png)


## Circuit Breaker
- Payment에 delay 발생 코드를 작성하여 부하 발생에 따른 요청 실패를 구현한다.

```
...
  # reservation/src/main/resources/application.yml
  
  feign:
    hystrix:
      enabled: true

  hystrix:
    command:
      default:
        execution.isolation.thread.timeoutInMilliseconds: 600
...
```

```
...
    # payment/src/main/java/moviereservation/domain/Payment.java
    
    @PostLoad
    public void makeDelay(){
        try {
            Thread.currentThread().sleep((long) (400 + Math.random() * 210));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
     }

...
```


```
...
    # reservation/src/main/moviereservation/external/PaymentService.java

    @FeignClient(name = "payment", url = "${api.url.payment}")
    public interface PaymentService {
        @RequestMapping(method= RequestMethod.POST, path="/payments")
        public void approvePayment(@RequestBody Payment payment);
    }

...
```

# 운영

## Gateway Ingress

## Deploy Pipeline

## Autoscale

## Readiness Probe 

## PV ConfigMap Secre

## Liveness Probe

## Loggregation Monitoring
