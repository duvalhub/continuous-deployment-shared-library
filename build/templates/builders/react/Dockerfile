FROM node:lts AS builder
WORKDIR /app
ARG BUILD_COMMAND="npm run build"
ARG BUILD_DEST="build"
COPY ./package.json ./
RUN npm install
COPY src ./src
COPY public ./public
RUN ${BUILD_COMMAND}
RUN echo ${BUILD_DIRECTORY}
RUN echo ${BUILD_DEST}
ARG build_directory
RUN mkdir -p ${build_directory} \
&& rm -r ${build_directory} \
&& mv ${BUILD_DEST} ${build_directory}
