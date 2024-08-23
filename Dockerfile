FROM tomcat:latest

RUN rm -rf /usr/local/tomcat/webapps/ROOT

ADD target/bookapp-1.war /usr/local/tomcat/webapps/ROOT.war

COPY server.xml /usr/local/tomcat/conf/server.xml

EXPOSE 443
#EXPOSE 80

CMD ["catalina.sh", "run"]