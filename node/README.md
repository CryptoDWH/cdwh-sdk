# CDWH Node Guide

## Guide
### docker service install
#### auto install with the official install script
- aliyun mirror
```shell script
    curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun
```
- dao mirror
```shell script
    curl -sSL https://get.daocloud.io/docker | sh
```
### steps to start program 
- select the corresponding dockerfile according to system and copy it to your server machine.
- enter the folder of dockerfile and create the docker image with the following command.
```shell script
    #imageName and tag name by yourself. DEMO---image1:0.1
    docker build -t imageName:tag .
```
- after image created, you can check the images with the following command.
```shell script
    docker images
```
- create container by image.
```shell script
    #containerName name by yourself. DEMO---docker1
    #imageName:tag use the image create by you. DEMO---image1:0.1
    #miningMachineNum provide by the tech team of mw.
    docker run -itd --name containerName --privileged -p 7216:7216 -p 9001:9001 -p 7218:7218 -p 7219:7219 -p 4001:4001 -p 8088:8088 imageName:tag miningMachineNum
```
- check the log of docker container.
```shell script
    docker ps -a
    #get containerName/containerId according to the command above
    docker logs -f containerName/containerId
```
- you can get the container name or the container id with the following command of ps.
- if the log of docker container is normal, the docker container will quit. you can restart it with the following command.
```shell script
    docker ps -a
    #get containerName/containerId according to the command above
    docker start containerName/containerId
```
- enter the docker container
```shell script
    docker exec -it containerName/containerId /bin/bash
```
- enter the folder of the java program, check if the java program is started, and check the log of java program.
```shell script
    cd ~/mwfs
    ps aux|grep java
    cd ./logs
    tail -200f log.log
```

## Docker

---
## 帮助手册
### docker服务安装
#### 使用官方安装脚本自动安装
- aliyun镜像
```shell script
    curl -fsSL https://get.docker.com | bash -s docker --mirror Aliyun
```
- 国内dao镜像
```shell script
    curl -sSL https://get.daocloud.io/docker | sh
```
### 程序启动步骤
- 根据系统选择对应的dockerfile拷贝到服务器中
- 进入到dockerfile所在目录，创建doceker镜像
```shell script
    #’镜像名‘为自定义名称，’tag‘为标签也为用户自定义
    docker build -t 镜像名:tag .
```
- 镜像构建成功后可通过命令查看镜像是否创建成功
```shell script
    docker images
```
- 通过镜像构建docker容器
```shell script
    #’容器名‘为自定义名称，‘镜像名：tag’为通过dockerfile创建的镜像的自定义名称和标签，’矿机编号‘由公链技术人员提供，启动时需要作为参数输入
    docker run -itd --name 容器名 --privileged -p 7216:7216 -p 9001:9001 -p 7218:7218 -p 7219:7219 -p 4001:4001 -p 8088:8088 镜像名:tag 矿机编号
```
- 查看启动日志
```shell script
    docker ps -a
    #容器名、容器id可以通过ps命令查看，任选其一即可
    docker logs -f 容器名/容器id
```
- 日志正常输出，容器会自动退出，重新启动容器即可
```shell script
    docker ps -a
    #容器名、容器id可以通过ps命令查看，任选其一即可
    docker start 容器名/容器id
```
- 进入容器
```shell script
    docker exec -it 容器名/容器id /bin/bash
```
- 进入Java程序目录，先查看Java程序是否启动，再查看Java程序日志是否正常
```shell script
    cd ~/mwfs
    ps aux|grep java
    cd ./logs
    tail -200f log.log
```

## Docker镜像
- 镜像只需构建一次，可以重复使用