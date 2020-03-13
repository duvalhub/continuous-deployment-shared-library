FROM node:8-alpine
WORKDIR /app
ARG build_directory
ARG build_destination
COPY --from=builder ${build_directory}/${build_destination}  ./
COPY --from=builder ${build_directory}/node_modules  ./node_modules
ARG port=80
EXPOSE ${port}
ENV PORT=${port}
ENTRYPOINT ["node", "server.js"]
