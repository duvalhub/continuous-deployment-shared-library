FROM nginx:${container_version}
ARG build_directory
EXPOSE 80
RUN rm -r /usr/share/nginx/html
COPY --from=builder ${build_directory}  /usr/share/nginx/html
