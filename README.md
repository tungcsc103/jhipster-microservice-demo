## DEPLOYMENT STEPS:

From the project root

# Start infa service: zookeeper, kafka, jhipster registry ( eureka)
    1. docker-compose -f infra/docker-compose.yml up -d
    2. Eureka url: http://localhost:8761

# Start elastic, elk
    1. docker-compose -f jhipster-console/docker-compose.yml up -d
    2. Kibana url: http://localhost:5601/app/kibana
    
# Start zipkin
    1. STORAGE_TYPE=elasticsearch ES_INDEX=traces ES_HOSTS=http://127.0.0.1:9200 KAFKA_BOOTSTRAP_SERVERS=localhost:9092 java ${JAVA_OPTS} -jar zipkin/zipkin.jar
    2. Zipkin url: http://localhost:9411/zipkin/

# Start applications:
    1. docker-compose -f ms_edge/src/main/docker/app.yml up -d
    2. docker-compose -f AccountService/src/main/docker/app.yml up -d 
    3. docker-compose -f AnalyticService/src/main/docker/app.yml up -d
    4. docker-compose -f StatsService/src/main/docker/app.yml up -d  
    5. docker-compose -f TaskService/src/main/docker/app.yml up -d
    
    API Gateway: http://localhost:8889



## Testing step:
# Preparation:
    - Login: {{host}}/api/authenticate 
    - Create a task: {{host}}/services/tasks/api/tasks
    - Create view stats: {{host}}/services/analytic/api/view-stats
# Case 1: Pubsub communication
    1. User view a task: {{host}}/services/analytic/api/view-logs
    2. View stats: {{host}}/services/stats/api/user-stats

# Case 2: Inter service communication:
    1. View tasks + statistic: {{host}}/services/account/api/tasks-stats?id=54bb37b3-c94c-4624-9393-dff039a371c9