FROM openjdk:11.0.6-jdk
MAINTAINER Shimun Matic <shimun.matic@gmail.com>

ARG JAR_FILE=*.jar
ADD target/${JAR_FILE} /usr/share/information-hub/app.jar

ENTRYPOINT ["java","-jar","/usr/share/information-hub/app.jar"]