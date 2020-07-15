FROM node:${builder_version} as builder
ARG build_directory
WORKDIR ${build_directory}
ARG build_destination
COPY package.json package-lock.jso[n] ./
RUN npm install
COPY src ./src
RUN npm run build
