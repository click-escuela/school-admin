FROM openjdk:8

EXPOSE 8090

ADD target/school-admin-0.0.1-SNAPSHOT.jar school-admin-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar --illegal-access=permit","/school-admin-0.0.1-SNAPSHOT.jar"]