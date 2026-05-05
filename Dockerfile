FROM maven:3.8.6-eclipse-temurin-11 AS build

# Removed MongoDB, cause it has own container.

COPY . /webprotege

WORKDIR /webprotege

# Docker build only needs to compile and package. Database-backed checks belong 
# in CI or local dev (Testcontainers, compose stack, etc.), not necessarily 
# inside every docker build.
# @todo Use Testcontainers for that.

RUN mvn -B -DskipTests clean package

FROM tomcat:8-jre11-slim

RUN rm -rf /usr/local/tomcat/webapps/* \
    && mkdir -p /srv/webprotege \
    && mkdir -p /usr/local/tomcat/webapps/ROOT

WORKDIR /usr/local/tomcat/webapps/ROOT

# WEBPROTEGE_VERSION must match the project's version in the parent pom (currently 5.0.0-SNAPSHOT).
# Set default value, so we can override it in the docker-compose.yml file.
ARG WEBPROTEGE_VERSION=5.0.0-SNAPSHOT

ENV WEBPROTEGE_VERSION=$WEBPROTEGE_VERSION
COPY --from=build /webprotege/webprotege-cli/target/webprotege-cli-${WEBPROTEGE_VERSION}.jar /webprotege-cli.jar
COPY --from=build /webprotege/webprotege-server/target/webprotege-server-${WEBPROTEGE_VERSION}.war ./webprotege.war
RUN unzip webprotege.war \
    && rm webprotege.war
