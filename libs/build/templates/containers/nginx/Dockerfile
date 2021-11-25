FROM cirrusci/wget as nginx_gen
WORKDIR /app
COPY install_nginx_gen.sh ./
RUN chmod +x install_nginx_gen.sh && ./install_nginx_gen.sh

FROM nginx:${container_version}
ARG build_directory
EXPOSE 80
RUN rm -r /usr/share/nginx/html
COPY --from=builder ${build_directory}  /usr/share/nginx/html
COPY --from=nginx_gen /app/nginx-gen /usr/bin/nginx-gen
COPY --from=nginx_gen /app/nginx.tmpl /root/nginx.tmpl
COPY start.sh /app/start.sh
COPY init_container.sh /app/init_container.sh
ENTRYPOINT ["/bin/sh", "/app/start.sh"]
CMD ["/app/init_container.sh"]
