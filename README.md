# 

## Model
www.msaez.io/#/storming/24effc668ce374dd71b64a9c7a4a2d2e

## Before Running Services
### Make sure there is a Kafka server running
```
cd kafka
docker-compose up
```
- Check the Kafka messages:
```
cd kafka
docker-compose exec -it kafka /bin/bash
cd /bin
./kafka-console-consumer --bootstrap-server localhost:9092 --topic
```

## Run the backend micro-services
See the README.md files inside the each microservices directory:

- reservation
- payment
- review
- dashboard
- schedule


## Run API Gateway (Spring Gateway)
```
cd gateway
mvn spring-boot:run
```

## Test by API
- reservation
```
 http :8088/reservations id="id" userId="userId" scheduleId="scheduleId" paymentId="paymentId" status="status" createDate="createDate" updateDate="updateDate" reservId="reservId" 
```
- payment
```
 http :8088/payments paymentId="paymentId" scheduleId="scheduleId" approveDate="approveDate" amount="amount" status="status" qty="qty" reservId="reservId" 
```
- review
```
 http :8088/reviews reviewId="reviewId" movieId="movieId" userId="userId" content="content" rating="rating" status="status" createDate="createDate" updateDate="updateDate" 
```
- dashboard
```
```
- schedule
```
 http :8088/schedules id="id" movieId="movieId" title="title" price="price" seatCnt="seatCnt" startDate="startDate" endDate="endDate" saleCnt="saleCnt" theather="theather" scheduleId="scheduleId" 
```


## Run the frontend
```
cd frontend
npm i
npm run serve
```

## Test by UI
Open a browser to localhost:8088

## Required Utilities

- httpie (alternative for curl / POSTMAN) and network utils
```
sudo apt-get update
sudo apt-get install net-tools
sudo apt install iputils-ping
pip install httpie
```

- kubernetes utilities (kubectl)
```
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

- aws cli (aws)
```
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

- eksctl 
```
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
```

