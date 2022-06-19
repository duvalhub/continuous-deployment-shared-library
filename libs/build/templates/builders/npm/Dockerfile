FROM node:${builder_version} AS builder
WORKDIR /app
ENV CI="true"
ARG build_command="npm run build"
ARG build_directory
ARG build_destination="build"
# Config Server
ARG APPLICATION_NAME
ARG APPLICATION_PROFILES
ARG CONFIG_LABEL
ARG CONFIG_URL
ARG CONFIG_USERNAME
ARG CONFIG_PASSWORD
RUN "env | grep -i config"
COPY package.json package-lock.jso[n] ./
RUN npm install
COPY . .
RUN ${build_command}
RUN mkdir -p ${build_directory} \
&& rm -r ${build_directory} \
&& mv ${build_destination} ${build_directory}
