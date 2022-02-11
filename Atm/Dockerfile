# To build and run:
#docker build -t collector:openj9 .
#docker run --rm -p 8080:8080 -p 8081:8081 --name collector-openj9 -d collector:openj9

ARG APP_NAME="Atm-0.0.1-SNAPSHOT"
################ STAGE: BUILD ##################
FROM maven:3.8.1-adoptopenjdk-11-openj9 AS builder
ARG APP_NAME
ARG USERNAME=testUsers
ARG USER_UID=1000
ARG USER_GID=$USER_UID
RUN mkdir -p /build/$APP_NAME

# Create the user
RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME
RUN chown $USERNAME /build/$APP_NAME
USER $USERNAME

WORKDIR /build/$APP_NAME
COPY pom.xml ./
COPY src src/
# COPY config config/

RUN mvn dependency:go-offline dependency:resolve-plugins
# '-Dbuild.name' defines the name of the jar file to be generated, as well as,
# the name of log file saved under /var/log/spring-boot
RUN mvn package "-Dbuild.name=$APP_NAME"
RUN mkdir layers
WORKDIR /build/$APP_NAME/layers
RUN java -Djarmode=layertools -jar ../target/${APP_NAME}.jar extract

################ STAGE: DEPLOY ##################
FROM adoptopenjdk:11-jre-openj9
ARG APP_NAME
ARG USERNAME=testUsers
ARG USER_UID=1000
ARG USER_GID=$USER_UID

RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME

WORKDIR /opt/app/$APP_NAME
RUN mkdir -p /var/log/spring-boot

EXPOSE 8080/tcp
EXPOSE 8081/tcp

COPY --from=builder build/$APP_NAME/layers/dependencies/ ./
COPY --from=builder build/$APP_NAME/layers/spring-boot-loader/ ./
COPY --from=builder build/$APP_NAME/layers/snapshot-dependencies/ ./
COPY --from=builder build/$APP_NAME/layers/application/ ./
USER $USERNAME
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
