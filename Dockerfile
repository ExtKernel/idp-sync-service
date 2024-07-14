FROM amazoncorretto:17.0.11
LABEL authors="exkernel"

WORKDIR /app

COPY . /app

RUN yum install -y wget && \
    wget https://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo && \
    sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo && \
    yum install -y apache-maven && \
    mvn package

EXPOSE 8000

CMD ["mvn", "spring-boot:run"]