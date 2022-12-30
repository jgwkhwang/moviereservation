# 예제 - 영화예매

본 예제는 MSA/DDD/Event Storming/EDA 를 포괄하는 분석/설계/구현/운영 전단계를 커버하도록 구성한 예제입니다.
이는 클라우드 네이티브 애플리케이션의 개발에 요구되는 체크포인트들을 통과하기 위한 예시 답안을 포함합니다.
- 체크포인트 : https://workflowy.com/s/assessment-check-po/T5YrzcMewfo4J6LW


# Table of contents

- [예제 - 영화예약](#---)
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
    - [Gateway](#Gateway)
    - [Deploy](#Deploy)
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

![image](https://user-images.githubusercontent.com/117132766/210037385-316b7b68-17f1-4205-a00c-926dd8c63406.png)

![image](https://user-images.githubusercontent.com/117132766/210037914-3d864687-9f9b-4ec4-aa90-f00ef6c2c4c3.png)


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

- 위 이벤트 발생에 따라 dashboard 레코드 생성

![image](https://user-images.githubusercontent.com/117132766/210033018-4c51f155-5582-4f66-9c72-1a3c49bc6f46.png)


## Request Response
- 분석 단계에서의 조건 중 하나로 예약 시 예약 가능 상태 확인 호출은 동기식 일관성을 유지하는 트랜잭션으로 처리하기로 하였다. 호출 프로토콜은 이미 앞서 Rest Repository 에 의해 노출되어있는 REST 서비스를 FeignClient 를 이용하여 호출하도록 한다. 또한 예약(reservation) -> 결제(payment) 서비스도 동기식으로 처리하기로 하였다.

[동기식 모델 디자인]
![image](https://user-images.githubusercontent.com/31139303/210038073-3a995ddd-2315-4d8b-a787-81ee1e8dddec.png)

- 예약 요청을 받은 직후(@PostPersist) 가능상태 확인 및 결제를 동기(Sync)로 요청하도록 처리

```
# Reservation.java
...
   @PostPersist
    public void onPostPersist() {
        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        moviereservation.external.Payment payment = new moviereservation.external.Payment();
        payment.setPayId("P_" + String.valueOf(getId()));
        payment.setReservId(getReservId());
        payment.setStatus("created");
        payment.setApproveDate("2022-12-29 18:00:00");
        payment.setAmount(12000);
        payment.setQty("1");
        // mappings goes here
        ReservationApplication.applicationContext
            .getBean(moviereservation.external.PaymentService.class)
            .approvePayment(payment);
...
```

## Compensation Correlation
- 본 프로젝트에서는 PolicyHandler에서 처리 시 어떤 건에 대한 처리인지를 구별하기 위한 Correlation-key 구현을 이벤트 클래스 안의 변수로 전달받아 서비스간 연관된 처리를 정확하게 구현하고 있습니다.

아래의 구현 예제를 보면

예약(Reservation)을 하면 동시에 연관된 결제(Payment) 등의 서비스의 상태가 적당하게 변경이 되고, 예약건의 취소를 수행하면 다시 연관된 결제(Payment) 등의 서비스의 상태값 등의 데이터가 적당한 상태로 변경되는 것을 확인할 수 있습니다.

[예약등록 및 예약취소 프로세스]
- 노란색 : 예약등록 프로세스
- 빨간색 : 예약취소 프로세스

![image](https://user-images.githubusercontent.com/31139303/210036983-d922a0b4-e91d-4d94-a1a1-25999628d6b6.png)

1.예약등록

1-1. 예약등록시 예약상태 (register Reservation) : Created

![image](https://user-images.githubusercontent.com/31139303/210038483-4634f9ca-b0ef-4690-b3ec-66d6e4fda7bc.png)

1-2. 결재상태 (register payment): Created

![image](https://user-images.githubusercontent.com/31139303/210038917-9bc54f68-5ce1-4331-ae22-56b0db26eafb.png)


2. 예약취소

2-1. 취소시 예약정보는 삭제됨.

![image](https://user-images.githubusercontent.com/117132766/210037385-316b7b68-17f1-4205-a00c-926dd8c63406.png)

2-2. 결재상태 : Cancelled

![image](https://user-images.githubusercontent.com/117132766/210037914-3d864687-9f9b-4ec4-aa90-f00ef6c2c4c3.png)



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

## Gateway
1. application.yml 파일 내에 profiles 별 routes를 추가.
   gateway 서버의 포트는 8080.

- application.yml
```
spring:
  profiles: docker
  cloud:
    gateway:
      routes:
        - id: reservation
          uri: http://reservation:8080
          predicates:
            - Path=/reservations/**, 
        - id: payment
          uri: http://payment:8080
          predicates:
            - Path=/payments/**, 
        - id: review
          uri: http://review:8080
          predicates:
            - Path=/reviews/**, 
        - id: dashboard
          uri: http://dashboard:8080
          predicates:
            - Path=, /dashboards/**
        - id: schedule
          uri: http://schedule:8080
          predicates:
            - Path=/schedules/**, 
        - id: frontend
          uri: http://frontend:8080
          predicates:
            - Path=/**
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins:
              - "*"
            allowedMethods:
              - "*"
            allowedHeaders:
              - "*"
            allowCredentials: true
server:
  port: 8080
```   

2. Service 
  Kubernestes용 service.yaml 작성한 후 gateway 엔드포인트 확인.
- service.yaml 
```
apiVersion: v1
kind: Service
metadata:
  name: gateway
  labels:
    app: gateway
spec:
  ports:
    - port: 8080
      targetPort: 8080
  selector:
    app: gateway
  type: LoadBalancer
```
 ![image](https://user-images.githubusercontent.com/117131347/209915789-8005b700-cb18-45a2-afc5-0765afb42052.png) 

## Deploy

- Kubernetes에 Deploy 생성.

- deployment.yml
``` 
apiVersion: apps/v1
kind: Deployment
metadata:
  name: gateway
  labels:
    app: gateway
spec:
  replicas: 1
  selector:
    matchLabels:
      app: gateway
  template:
    metadata:
      labels:
        app: gateway
    spec:
      containers:
        - name: gateway
          image: username/gateway:latest
          ports:
            - containerPort: 8080
```             

- Kubernetes에 생성된 Deploy 확인

![image](https://user-images.githubusercontent.com/117131347/210037419-5687b526-28e6-4029-a5cb-e2919e6d188b.png)


## Autoscale
앞서 CB 는 시스템을 안정되게 운영할 수 있게 해줬지만 사용자의 요청을 100% 받아들여주지 못했기 때문에 이에 대한 보완책으로 자동화된 확장 기능을 적용하고자 한다.

- Reservation deployment.yml 파일에 resources 설정을 추가한다.

![image](https://user-images.githubusercontent.com/98464146/210037454-6ba83845-3b51-48a0-8d1a-e40d1c23662b.png)

- 결제서비스에 대한 replica 를 동적으로 늘려주도록 HPA 를 설정한다. 설정은 CPU 사용량이 15프로를 넘어서면 replica 를 10개까지 늘려준다:

```
kubectl autoscale deploy reservation --min=1 --max=10 --cpu-percent=15
```

- CB 에서 했던 방식대로 워크로드를 2분 동안 걸어준다.
  동시 사용자 100명, 2분 동안 실시

```
siege -c100 -t120S --content-type "application/json" 'http://reservation:8080 POST {"status": "created"}'
```

 ![image](https://user-images.githubusercontent.com/117131347/209915789-8005b700-cb18-45a2-afc5-0765afb42052.png) 


- 오토스케일이 어떻게 되고 있는지 모니터링을 걸어둔다


```
kubectl get deploy reservation -w
```

- 어느정도 시간이 흐른 후 (약 30초) 스케일 아웃이 벌어지는 것을 확인할 수 있다.

![image](https://user-images.githubusercontent.com/98464146/210038195-63191b70-fb23-4c6f-b00d-c6047d4f7ce9.png)

- siege 의 로그를 보아도 전체적인 성공률이 높아진 것을 확인 할 수 있다.

![image](https://user-images.githubusercontent.com/98464146/210038303-cc363234-0e48-4354-ab61-e8733fe5f248.png)


## Readiness Probe 

- 먼저 무정지 재배포가 100% 되는 것인지 확인하기 위해서 Autoscaler 이나 CB 설정을 제거함!

```
kubectl delete destinationrules dr-payment
kubectl delete hpa reservation
```

- seige 로 배포 작업 직전에 워크로드를 모니터링 함.
  동시 사용자 100명, 2분 동안 실시

```
siege -c100 -t120S --content-type "application/json" 'http://reservation:8080 POST {"status": "created"}'
```

![image](https://user-images.githubusercontent.com/98464146/210039560-d353af64-6352-4ffd-a539-5a53a75fac9f.png)

- 새버전으로의 배포 시작
  Reservation deployment.yml 파일에 resources 설정을 추가 후 배포 한다.
  
```
kubectl apply -f deployment.yml
```

- seige 의 화면으로 넘어가서 Availability 가 100% 미만으로 떨어졌는지 확인

![image](https://user-images.githubusercontent.com/98464146/210039743-7931e579-09f7-425c-bef6-d5372b7e62a6.png)

배포기간중 Availability 가 평소 100%에서 70% 대로 떨어지는 것을 확인. 
원인은 쿠버네티스가 성급하게 새로 올려진 서비스를 READY 상태로 인식하여 서비스 유입을 진행한 것이기 때문. 이를 막기위해 Readiness Probe 를 설정함

- reservation deployment.yml 파일 수정

![image](https://user-images.githubusercontent.com/98464146/210039990-5574aa64-d77a-4edd-a9f3-5f126c42fd15.png)

```
kubectl apply -f deployment.yml
```

- 동일한 시나리오로 재배포 한 후 Availability 확인

![image](https://user-images.githubusercontent.com/98464146/210040090-66648ae5-681c-44b2-9ee2-8c47faf0b807.png)

배포기간 동안 Availability 가 변화없기 때문에 무정지 재배포가 성공한 것으로 확인됨


## PV
1. EFS 생성 및 security group 생성

![image](https://user-images.githubusercontent.com/118946107/210042040-15f53591-621a-4b78-9900-0136b794aa3d.png)

![image](https://user-images.githubusercontent.com/118946107/210042076-d07df316-aedb-4483-85e1-90ace1345a84.png)


2. EFS 계정 생성 및 ROLE 바인딩

![image](https://user-images.githubusercontent.com/118946107/210042344-69757b14-22ed-4903-b169-a4bdff245b7c.png)

3. EFS Provisioner 배포 / 설치한 Provisioner storageclass에 등록

![image](https://user-images.githubusercontent.com/118946107/210042391-3ac902cf-ea46-4fa1-9b8f-4c292ba73950.png)

![image](https://user-images.githubusercontent.com/118946107/210042427-98837103-3032-475a-8753-f60a765dded2.png)

4. PVC(PersistentVolumeClaim) 생성

![image](https://user-images.githubusercontent.com/118946107/210042466-9f5aa82e-f2b8-496d-bd4e-7dc79d0f9aaf.png)


5. 볼륨을 가지는 마이크로서비스 배포

![image](https://user-images.githubusercontent.com/118946107/210042511-9d2d338d-36ef-4628-b48e-2008c7f3b01d.png)

6. pod에서 마운트된 경로 파일 생성 및 확인

![image](https://user-images.githubusercontent.com/118946107/210042639-df3de277-1013-49ea-9303-7c1ffd6615f1.png)

## Liveness Probe

## Loggregation

1. ElasticSearch 설치

- helm repo add elastic https://helm.elastic.co

- helm repo update

- helm show values elastic/elasticsearch > es-value.yml

- vi es-value.yml

![image](https://user-images.githubusercontent.com/118946107/210042809-d2cbba20-d342-4e6d-b918-91e29dd038a7.png)

- kubectl create namespace logging

- helm install elastic elastic/elasticsearch -f es-value.yml -n logging

- kubectl get all -n logging

![image](https://user-images.githubusercontent.com/118946107/210042863-2a127089-2143-40ac-a27f-aba5e78de2bb.png)

2. FluentBit 설치
- git clone https://github.com/fluent/fluent-bit-kubernetes-logging.git

- cd fluent-bit-kubernetes-logging/

- kubectl create -f fluent-bit-service-account.yaml -n logging

- kubectl create -f fluent-bit-role-1.22.yaml -n logging

- kubectl create -f fluent-bit-role-binding-1.22.yaml -n logging

- vi fluent-bit-ds.yaml

![image](https://user-images.githubusercontent.com/118946107/210042963-f03048ee-28a1-446d-92e6-79a829264c61.png)

- vi fluent-bit-configmap.yaml

![image](https://user-images.githubusercontent.com/118946107/210043022-7a9cdf42-cbb1-45ae-b6e6-95edf7d53391.png)

- kubectl apply -f fluent-bit-configmap.yaml -n logging
- kubectl apply -f fluent-bit-ds.yaml -n logging

3. Kibana 설치

- helm show values elastic/kibana > kibana-value.yml

- vi kibana-value.yml

![image](https://user-images.githubusercontent.com/118946107/210043126-1b8e4bf4-ccb5-4e5c-8a21-9ccdacbb15eb.png)

![image](https://user-images.githubusercontent.com/118946107/210043097-48b5056b-d2be-4389-8789-bb2f8946412b.png)

- helm install kibana elastic/kibana -f kibana-value.yml -n logging

4. Kibana를 통한 로깅 확인

![image](https://user-images.githubusercontent.com/118946107/210043164-994692ee-0f48-4518-ae19-b28c5ea37a4b.png)

