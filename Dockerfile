FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /tmp/build
COPY pom.xml .
COPY src src
COPY ./docker/build/check-dependencies.sh check-dependencies.sh
RUN --mount=type=cache,target=/root/.m2,id=maven-cache,sharing=locked \
  mvn -B package; \
  chmod 550 check-dependencies.sh; \
  ./check-dependencies.sh;

FROM eclipse-temurin:21-alpine AS jre-build
WORKDIR /tmp/jre-build
COPY ./docker/jre-build/create-java-runtime.sh create-java-runtime.sh
COPY --from=build /tmp/build/jre-module-dependencies.tmp jre-module-dependencies.tmp
RUN chmod 550 create-java-runtime.sh; \
  chmod 440 jre-module-dependencies.tmp; \
  ./create-java-runtime.sh

FROM alpine:3.20.2 AS project
ENV JAVA_HOME=/opt/java/openjdk
ENV JAVA_OPTS="-Xms64M -Xmx128M"
RUN mkdir -p /opt/project "$JAVA_HOME"; \
  chown -R 1001:1001 /opt/project "$JAVA_HOME"

USER 1001:1001
COPY --chown=1001:1001 --from=jre-build /tmp/java-runtime $JAVA_HOME
COPY --chown=1001:1001 --from=build /tmp/build/target/*.jar /opt/project/app.jar
COPY --chown=1001:1001 docker/project/entrypoint.sh /opt/project/entrypoint.sh
RUN chmod ug+xw /opt/project/entrypoint.sh
ENTRYPOINT ["/opt/project/entrypoint.sh"]