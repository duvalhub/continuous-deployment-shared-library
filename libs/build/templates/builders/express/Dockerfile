ARG builder_version=8
FROM node:${builder_version} as builder
ARG build_directory
WORKDIR ${build_directory}
ARG build_destination
COPY package.json ./
RUN npm install
COPY src ./src
RUN npm run build