FROM nginx:${container_version}
ARG build_directory
EXPOSE 80
RUN rm -r /usr/share/nginx/html
COPY --from=builder ${build_directory}  /usr/share/nginx/html
COPY start.sh /app/start.sh
COPY todelete.sh /app/todelete.sh
RUN chmod +x /app/todelete.sh
ENTRYPOINT ["/bin/sh", "/app/start.sh", "/app/todelete.sh"]
# ENTRYPOINT ["/bin/sh", "/app/start.sh", "/docker-entrypoint.sh"]
# CMD ["nginx", "-g", "daemon off;"]