FROM maven as builder
WORKDIR /builder
ARG build_directory
ARG build_destination
COPY pom.xml ./
RUN mvn verify --fail-never
COPY src ./src
RUN mvn package
RUN echo "build_directory: '$build_directory', build_destination: '$build_destination'"
RUN mkdir -p ${build_directory}
RUN rm -r ${build_directory}
RUN mv ${build_destination} ${build_directory}

