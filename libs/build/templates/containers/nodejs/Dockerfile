ARG builder_version=8-alpine
FROM node:${builder_version}
WORKDIR /app
ARG build_directory
COPY --from=builder ${build_directory}  ./
ARG port=80
EXPOSE ${port}
ENV PORT=${port}
ENTRYPOINT ["node", "server.js"]