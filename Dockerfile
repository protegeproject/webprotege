FROM quay.io/babylonhealth/java:11
ARG JAR_FILE="webprotege-server/target/webprotege-standalone.jar"
EXPOSE 8080

USER root
COPY webprotege-cli/target/webprotege-cli.jar /webprotege-cli.jar
RUN mkdir -p /srv/webprotege && chmod -R a+w /srv/webprotege

USER app
ADD "$JAR_FILE" "$EXECUTABLE_JAR"
