FROM nginx:${container_version}
ARG build_directory
EXPOSE 80
RUN rm -r /usr/share/nginx/html
COPY --from=builder ${build_directory}  /usr/share/nginx/html
COPY start.sh /app/start.sh
ENTRYPOINT ["/bin/sh", "/app/start.sh"]
CMD ["/docker-entrypoint.sh", "nginx", "-g", "daemon off;"]