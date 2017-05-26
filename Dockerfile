FROM anapsix/alpine-java:8
VOLUME /tmp
VOLUME /config
COPY build/libs/upload-status.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]