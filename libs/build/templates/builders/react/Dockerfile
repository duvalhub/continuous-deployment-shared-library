FROM node:${builder_version} AS builder
WORKDIR /app
ENV CI="true"
ARG BUILD_COMMAND="npm run build"
ARG BUILD_DEST="build"
ARG build_directory
COPY package.json package-lock.jso[n] ./
RUN npm install
COPY src ./src
COPY public ./public
RUN ${BUILD_COMMAND}
RUN mkdir -p ${build_directory} \
&& rm -r ${build_directory} \
&& mv ${BUILD_DEST} ${build_directory}
