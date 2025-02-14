#
# Build
#
FROM amazoncorretto:21-alpine3.19@sha256:61eafdeb119539fbbeb328da83f4f196c00a9db13dc3789fffd4551caa3eb678 AS buildtime

WORKDIR /build
COPY . .

RUN chmod +x ./gradlew \
&& ./gradlew bootJar

#
# Docker RUNTIME
#
FROM amazoncorretto:21-alpine3.19@sha256:61eafdeb119539fbbeb328da83f4f196c00a9db13dc3789fffd4551caa3eb678 AS runtime

VOLUME /tmp
WORKDIR /app

COPY --from=buildtime /build/build/libs/*.jar /app/app.jar
# The agent is enabled at runtime via JAVA_TOOL_OPTIONS.
ADD https://github.com/microsoft/ApplicationInsights-Java/releases/download/3.5.4/applicationinsights-agent-3.5.4.jar /app/applicationinsights-agent.jar

RUN chown -R nobody:nobody /app

USER 65534 # user nobody

ENTRYPOINT ["java","-jar","/app/app.jar"]
