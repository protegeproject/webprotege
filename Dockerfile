FROM tomcat:8.0-jre8

WORKDIR $CATALINA_HOME

ADD target/webprotege-2.6.0-SNAPSHOT webapps/ROOT

RUN echo -e "\nmongodb.host=mongo" >> webapps/ROOT/WEB-INF/classes/webprotege.properties
RUN mkdir -p /data/webprotege

VOLUME /data/webprotege

CMD ["catalina.sh", "run"]
