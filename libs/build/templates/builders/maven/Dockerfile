FROM maven as builder
WORKDIR /builder
ARG build_directory
ARG build_destination=target/*.jar
COPY src ./src
COPY pom.xml ./
RUN mvn install \
&& mkdir -p ${build_directory} \
&& rm -r ${build_directory} \
&& mv ${build_destination} ${build_directory}
