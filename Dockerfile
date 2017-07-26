FROM sciencebase/tomcat
VOLUME /config
COPY build/libs/upload-status.jar /usr/local/tomcat/webapps/upload-status.war

CMD ["catalina.sh", "run"]