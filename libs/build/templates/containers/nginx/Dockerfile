FROM ubuntu as nginx_gen
COPY install_nginx_gen.sh ./
RUN ./install_nginx_gen.sh

FROM nginx:${container_version}
ARG build_directory
EXPOSE 80
RUN rm -r /usr/share/nginx/html
COPY --from=builder ${build_directory}  /usr/share/nginx/html
COPY --from=nginx_gen nginx_gen /usr/bin/nginx_gen
COPY start.sh /app/start.sh
ENTRYPOINT ["/bin/sh", "/app/start.sh"]
CMD ["nginx_gen", "sleep", "infinity"]
#CMD ["nginx_gen", "sleep", "infinity", "/docker-entrypoint.sh", "nginx", "-g", "daemon off;"]
