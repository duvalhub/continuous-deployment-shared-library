FROM node:${container_version}
WORKDIR /app
ARG build_directory
ARG build_destination
COPY start.sh package.json package-lock.jso[n] ./
RUN npm install --only-prod && npm cache clean --force
COPY --from=builder ${build_directory} ./${build_destination}
ARG port=80
EXPOSE ${port}
ENV PORT=${port}
ARG NODE_ENV=production
ENV NODE_ENV $NODE_ENV
ENTRYPOINT ["/bin/sh", "/app/start.sh"]
CMD ["npm", "start"]
ARG HEALTHCHECK_ENDPOINT
ENV HEALTHCHECK_ENDPOINT $HEALTHCHECK_ENDPOINT
COPY healthcheck.js ./
HEALTHCHECK CMD node healthcheck.js
