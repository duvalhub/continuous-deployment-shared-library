FROM node:${container_version}
WORKDIR /app
ARG build_directory
ARG build_destination
COPY --from=builder ${build_directory}/package.json ${build_directory}/package-lock.jso[n]  ./
RUN npm install --only-prod && npm cache clean --force
COPY --from=builder ${build_directory}/${build_destination}  ./
ARG port=80
EXPOSE ${port}
ENV PORT=${port}
ARG NODE_ENV=production
ENV NODE_ENV $NODE_ENV
ENTRYPOINT ["/bin/sh", "/app/start.sh"]
CMD ["npm", "start"]
