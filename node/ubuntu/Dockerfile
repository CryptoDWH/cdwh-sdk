#Base image
FROM ubuntu:18.04

#Update image
RUN apt-get update && apt-get install -y wget
RUN apt-get update && apt-get install -y sudo
RUN apt-get update && apt-get install -y lsb-core
RUN apt-get update && apt-get install -y net-tools
RUN apt-get update && apt-get install -y systemd-sysv
RUN wget https://mwfs.oss-cn-shenzhen.aliyuncs.com/cos/client/docker/hub_install_cos_ubuntu.sh
RUN chmod 667 hub_install_cos_ubuntu.sh
RUN wget https://mwfs.oss-cn-shenzhen.aliyuncs.com/cos/client/docker/docker_init_ubuntu.sh
RUN chmod 667 docker_init_ubuntu.sh
EXPOSE 7216 9001 7218 7219 4001 8088
#EXPOSE 9001
#EXPOSE 7218
#EXPOSE 7219
#EXPOSE 4001
#EXPOSE 8088

ENTRYPOINT [ "/bin/bash", "./docker_init_ubuntu.sh" ]