FROM anapsix/alpine-java:8
VOLUME /tmp
VOLUME /config
ADD build/libs/upload-status.jar app.jar

#For running locally, see Development Docker Standalone Notes in https://github.com/digitalcrust/upload-status/blob/standalone-docker/README.adoc
#ADD certs/DOIRootCA.pem DOIRootCA.pem
#RUN keytool -import -noprompt -trustcacerts -alias localhost -file DOIRootCA.pem -keystore $JAVA_HOME/jre/lib/security/cacerts -storepass changeme

RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
