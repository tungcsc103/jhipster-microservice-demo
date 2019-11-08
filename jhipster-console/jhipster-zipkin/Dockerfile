FROM openzipkin/zipkin:2.11.5
# Embed zipkin dependencies, a spark job to compute the graph between microservices
ENV ZIPKIN_DEPENDENCIES_VERSION=2.18.3 \
    STORAGE_TYPE=elasticsearch \
    ES_INDEX=traces \
    KAFKA_BOOTSTRAP_SERVERS=http://kafka:9092

#RUN curl -SL $ZIPKIN_REPO/io/zipkin/dependencies/zipkin-dependencies/$ZIPKIN_DEPENDENCIES_VERSION/zipkin-dependencies-$ZIPKIN_DEPENDENCIES_VERSION.jar > zipkin.jar
RUN curl -fL -o 'zipkin.jar' 'https://repo1.maven.org/maven2/io/zipkin/zipkin-server/2.18.3/zipkin-server-2.18.3-exec.jar'
# Copy CRON files
#COPY periodic/ /etc/periodic/
# To run the dependency job once, run: ./run-job.sh from inside the container
# `docker-compose exec jhipster-zipkin ./run-job.sh`
#COPY run-job.sh .

#CMD crond -b && java ${JAVA_OPTS} -cp . org.springframework.boot.loader.JarLauncher
CMD java ${JAVA_OPTS} -jar zipkin.jar
