FROM openjdk:17-alpine AS builder

# set arg
ARG BUILD_TARGET=build/libs

# copy code & build
COPY . .
RUN ./gradlew clean bootJar

# unpack jar
WORKDIR ${BUILD_TARGET}
RUN jar -xf *.jar

### create image stage ###
FROM openjdk:17-alpine

ARG BUILD_TARGET=build/libs
ARG DEPLOY_PATH=deploy

# copy from build stage
COPY --from=builder ${BUILD_TARGET}/org ${DEPLOY_PATH}/org
COPY --from=builder ${BUILD_TARGET}/BOOT-INF/lib ${DEPLOY_PATH}/BOOT-INF/lib
COPY --from=builder ${BUILD_TARGET}/META-INF ${DEPLOY_PATH}/META-INF
COPY --from=builder ${BUILD_TARGET}/BOOT-INF/classes ${DEPLOY_PATH}/BOOT-INF/classes

WORKDIR ${DEPLOY_PATH}

ENV JAVA_OPTS = "-Dencrypt_component_role={SEC_KEY}mid@swebsolution"

ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]