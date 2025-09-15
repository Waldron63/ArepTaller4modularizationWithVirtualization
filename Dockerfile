FROM openjdk:21

WORKDIR /usrapp/bin

ENV PORT=35000
ENV DOCKER_ENV=true

COPY target/classes /usrapp/bin/classes
COPY target/dependency /usrapp/bin/dependency
COPY src/main/resources /usrapp/bin/resources



CMD ["java", "-cp", "./classes:./dependency/*", "com.edu.escuelaing.areptaller4modularizationwithvirtualization.httpserver.MainStart"]