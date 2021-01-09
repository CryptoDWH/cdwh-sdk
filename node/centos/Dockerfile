#Base image
FROM centos:8

#Update image
RUN yum install sudo -y
RUN yum install redhat-lsb-core -y
RUN yum install net-tools -y
RUN yum install systemd-sysv -y
RUN yum install wget -y
RUN wget https://mwfs.oss-cn-shenzhen.aliyuncs.com/cos/client/docker/hub_install_cos_centos.sh
RUN chmod 667 hub_install_cos_centos.sh
RUN wget https://mwfs.oss-cn-shenzhen.aliyuncs.com/cos/client/docker/docker_init_centos.sh
RUN chmod 667 docker_init_centos.sh
EXPOSE 7216 9001 7218 7219 4001 8088
#EXPOSE 9001
#EXPOSE 7218
#EXPOSE 7219
#EXPOSE 4001
#EXPOSE 8088

#CMD "TOP"
ENTRYPOINT [ "/bin/bash", "./docker_init_centos.sh" ]