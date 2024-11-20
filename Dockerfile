#
# Build
#
FROM amazoncorretto:17-alpine3.19@sha256:ac45489ce3ac30e9758d3354a60c63a2e962d102e62dd1a1ef816ad6aad58fdf AS buildtime

WORKDIR /build
COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew bootJar

#
# Docker RUNTIME
#
FROM amazoncorretto:17-alpine3.19@sha256:ac45489ce3ac30e9758d3354a60c63a2e962d102e62dd1a1ef816ad6aad58fdf AS runtime

VOLUME /tmp
WORKDIR /app

COPY --from=buildtime /build/build/libs/*.jar /app/app.jar
# The agent is enabled at runtime via JAVA_TOOL_OPTIONS.
ADD https://github.com/microsoft/ApplicationInsights-Java/releases/download/3.5.4/applicationinsights-agent-3.5.4.jar /app/applicationinsights-agent.jar

RUN chown -R nobody:nobody /app

USER 65534 # user nobody

ENTRYPOINT ["java","-jar","/app/app.jar"]
