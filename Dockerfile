FROM tomcat:10-jdk21

RUN mkdir -p /home/yummy/storage
COPY target/yummy.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]
