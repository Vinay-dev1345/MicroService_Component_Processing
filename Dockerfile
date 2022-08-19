FROM public.ecr.aws/amazoncorretto/amazoncorretto:17
EXPOSE 9002
ADD target/componentProcessor-0.0.1-SNAPSHOT.jar componentProcessor-0.0.1-SNAPSHOT.jar 
ENTRYPOINT ["java","-jar","/componentProcessor-0.0.1-SNAPSHOT.jar"]