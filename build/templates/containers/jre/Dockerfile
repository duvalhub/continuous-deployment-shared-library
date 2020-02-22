FROM openjdk:8-jdk-alpine as ping
COPY Healthcheck.java ./
RUN javac Healthcheck.java

FROM openjdk:8-jre-alpine
WORKDIR /app
EXPOSE 8080
ARG build_directory
ARG JAR_FILE=target/*.jar
COPY --from=builder ${build_directory} app.jar
COPY --from=ping Healthcheck.class healthcheck/Healthcheck.class
COPY init_container.sh ./
RUN chmod +x init_container.sh
#HEALTHCHECK --interval=10s --timeout=3s CMD java healthcheck/Healthcheck localhost 8080 || exit 1
ENTRYPOINT ["/bin/sh","init_container.sh"]