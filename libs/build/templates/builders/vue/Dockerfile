FROM node:${builder_version} AS builder
WORKDIR /app
ARG BUILD_COMMAND="npm run build"
ARG build_destination="build"
ARG build_directory
COPY package.json package-lock.jso[n] ./
RUN npm install
COPY . .
RUN ${BUILD_COMMAND}
RUN mkdir -p ${build_directory} \
&& rm -r ${build_directory} \
&& mv ${build_destination} ${build_directory}