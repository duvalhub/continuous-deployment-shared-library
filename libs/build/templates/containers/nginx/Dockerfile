FROM ubuntu as nginx_gen
COPY install_nginx_gen.sh ./
RUN chmod +x install_nginx_gen.sh && ./install_nginx_gen.sh

FROM nginx:${container_version}
ARG build_directory
EXPOSE 80
RUN rm -r /usr/share/nginx/html
COPY --from=builder ${build_directory}  /usr/share/nginx/html
COPY --from=nginx_gen nginx-gen /usr/bin/nginx-gen
COPY start.sh /app/start.sh
ENTRYPOINT ["/bin/sh", "/app/start.sh"]
CMD ["nginx-gen", "sleep", "infinity"]
#CMD ["nginx-gen", "sleep", "infinity", "/docker-entrypoint.sh", "nginx", "-g", "daemon off;"]
