FROM maven as builder
WORKDIR /builder
ARG build_directory
ARG build_destination
COPY pom.xml ./
RUN mvn verify --fail-never
COPY src ./src
RUN mvn package \
&& mkdir -p ${build_directory} \
&& rm -r ${build_directory} \
&& mv ${build_destination} ${build_directory}

