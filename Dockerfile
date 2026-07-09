FROM maven:3.6.0-jdk-11-slim AS build

RUN sed -i 's|deb.debian.org/debian|archive.debian.org/debian|g; s|security.debian.org/debian-security|archive.debian.org/debian-security|g' /etc/apt/sources.list \
    && sed -i '/stretch-updates/d; /stretch-backports/d' /etc/apt/sources.list \
    && rm -f /etc/apt/sources.list.d/stretch-backports.list \
    && printf 'Acquire::Check-Valid-Until "false";\nAcquire::AllowInsecureRepositories "true";\nAcquire::AllowDowngradeToInsecureRepositories "true";\nAPT::Get::AllowUnauthenticated "true";\n' > /etc/apt/apt.conf.d/99archive \
    && apt-get update \
    && apt-get install -y --allow-unauthenticated --no-install-recommends git mongodb \
    && rm -rf /var/lib/apt/lists/*

COPY . /webprotege

WORKDIR /webprotege

RUN mkdir -p /data/db /artifacts \
    && mongod --fork --syslog \
    && mvn --batch-mode --no-transfer-progress -Dmaven.wagon.http.retryHandler.count=5 -Dmaven.wagon.rto=600000 clean package \
    && version="$(mvn --batch-mode --no-transfer-progress -Dmaven.wagon.http.retryHandler.count=5 -Dmaven.wagon.rto=600000 help:evaluate -Dexpression=project.version -q -DforceStdout)" \
    && cp "/webprotege/webprotege-cli/target/webprotege-cli-${version}.jar" /artifacts/webprotege-cli.jar \
    && cp "/webprotege/webprotege-server/target/webprotege-server-${version}.war" /artifacts/webprotege.war
FROM tomcat:8-jre11-slim

RUN rm -rf /usr/local/tomcat/webapps/* \
    && mkdir -p /srv/webprotege \
    && mkdir -p /usr/local/tomcat/webapps/ROOT

WORKDIR /usr/local/tomcat/webapps/ROOT

COPY --from=build /artifacts/webprotege-cli.jar /webprotege-cli.jar
COPY --from=build /artifacts/webprotege.war ./webprotege.war
RUN unzip webprotege.war \
    && rm webprotege.war
