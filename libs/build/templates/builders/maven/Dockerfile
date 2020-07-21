FROM maven as builder
WORKDIR /builder
ARG build_directory
ARG build_destination=target/*.jar
COPY src ./src
COPY pom.xml ./
RUN mvn install
RUN echo "build_directory: '$build_directory', build_destination: '$build_destination'"
RUN mkdir -p ${build_directory}
RUN rm -r ${build_directory}
RUN mv ${build_destination} ${build_directory}

