#
# Build
#
FROM amazoncorretto:21-alpine3.21@sha256:d4eb1978220a3ba2551e407d3d279bfc77a8903932814a0b4166bbee6588f92c AS buildtime

WORKDIR /build
COPY . .

RUN chmod +x ./gradlew \
&& ./gradlew bootJar

#
# Docker RUNTIME
#
FROM amazoncorretto:21-alpine3.21@sha256:d4eb1978220a3ba2551e407d3d279bfc77a8903932814a0b4166bbee6588f92c AS runtime

VOLUME /tmp
WORKDIR /app

COPY --from=buildtime /build/build/libs/*.jar /app/app.jar
# The agent is enabled at runtime via JAVA_TOOL_OPTIONS.
ADD https://github.com/microsoft/ApplicationInsights-Java/releases/download/3.5.4/applicationinsights-agent-3.5.4.jar /app/applicationinsights-agent.jar

RUN chown -R nobody:nobody /app

USER 65534 # user nobody

ENTRYPOINT ["java","-jar","/app/app.jar"]
