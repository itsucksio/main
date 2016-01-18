FROM java:8-jre
EXPOSE 3000
ADD target/itsucks.jar /itsucks.jar
ENTRYPOINT ["java", "-jar", "/itsucks.jar"]
