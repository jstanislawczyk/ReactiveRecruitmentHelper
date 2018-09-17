FROM openjdk:10
ADD build/libs/reactive-recruitment-helper-0.2.jar reactive-recruitment-helper-0.2.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "reactive-recruitment-helper-0.2.jar"]