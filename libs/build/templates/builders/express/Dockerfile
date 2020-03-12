ARG builder_version=8
FROM node:${builder_version} as builder
ARG build_destination
WORKDIR ${build_destination}
ARG build_directory
COPY package.json ./
RUN npm install
COPY src ./src
RUN npm run build \
&& echo "Build destination: '${build_destination}'" \
&& mv node_modules ${build_destination}/node_modules