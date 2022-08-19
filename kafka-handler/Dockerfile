FROM nexus-registry-nexus.apps.ocpdev.anb.net/maven:3.6.3-openjdk-11-slim AS builder
COPY pom.xml /tmp/
COPY settings.xml /tmp/
RUN mvn -B dependency:resolve -f /tmp/pom.xml -s /tmp/settings.xml -DskipTests
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn -B -s /tmp/settings.xml package -DskipTests
FROM nexus-registry-nexus.apps.ocpdev.anb.net/nexus-iq-cli:latest as nexus-iq-cli
ARG IQUrl=
ARG nexus_user=
ARG nexus_pass=
ARG WORKSPACE=
ARG scm_project=
COPY pom.xml /tmp/
RUN --mount=type=secret,id=nexus_user --mount=type=secret,id=nexus_pass /sonatype/evaluate -s $IQUrl -a $(cat /run/secrets/nexus_user):$(cat /run/secrets/nexus_pass) -r /sonatype/reports/evaluate.json  -t 'build' -i $scm_project /tmp/pom.xml
FROM scratch as iqresults
COPY --from=nexus-iq-cli /sonatype/reports/ .
FROM nexus-registry-nexus.apps.ocpdev.anb.net/sonar-scanner-cli:latest as sonar-scanner-cli
ARG sonarUrl=
ARG sonarscrt=
ARG scm_project=
ARG BUILD_NUMBER=
COPY --from=builder /tmp/ /tmp/app
WORKDIR /tmp/app
RUN sonar-scanner -Dsonar.host.url=$sonarUrl -Dsonar.login=$sonarscrt -Dsonar.projectName=$scm_project -Dsonar.projectVersion=$BUILD_NUMBER -Dsonar.projectKey=$scm_project -Dsonar.projectBaseDir=/tmp/app/ -Dsonar.sources=src/main/java/ -Dsonar.java.binaries=target/classes
FROM nexus-registry-nexus.apps.ocpdev.anb.net/openjdk:11-jre as run
EXPOSE 8080
RUN mkdir /app
COPY --from=builder /tmp/target/*.jar /app/spring-boot-application.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/spring-boot-application.jar"]