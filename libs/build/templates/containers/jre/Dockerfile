#FROM openjdk:${container_version}-jdk-alpine as ping
#COPY Healthcheck.java ./
#RUN javac Healthcheck.java

FROM openjdk:${container_version}
WORKDIR /app
EXPOSE 8080
ARG build_directory
ENV JAR_FILE=app.jar
COPY --from=builder ${build_directory} ${JAR_FILE}
#COPY --from=ping Healthcheck.class healthcheck/Healthcheck.class
COPY init_container.sh ./
RUN chmod +x init_container.sh
#HEALTHCHECK --interval=10s --timeout=3s CMD java healthcheck/Healthcheck localhost 8080 || exit 1
ENTRYPOINT ["/bin/sh","init_container.sh"]