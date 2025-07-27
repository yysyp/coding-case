FROM mysql:5.7
USER root
RUN groupadd -g 9000 demo && useradd -u 9000 -g demo demo
ENV SQL_FILE /docker-entrypoint-initdb.d/init.sql
COPY docker.cnf /etc/mysql/conf.d/
COPY init.sql ${SQL_FILE}
RUN chown -R demo:demo /docker-entrypoint-initdb.d /etc/mysql
USER demo
#CMD ["mysqld", "--defaults-file=/etc/mysql/conf.d/docker.cnf", "--initialize-insecure", "--max_allowed_packet=1073741824"]
CMD ["mysqld", "--defaults-file=/etc/mysql/conf.d/docker.cnf"]