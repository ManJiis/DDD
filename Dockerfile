FROM 192.168.1.111:20202/ddd/alpine-oraclejdk8:latest
COPY representation-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]