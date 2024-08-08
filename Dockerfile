# Stage 1: Build the WAR file using Maven
FROM maven:3.8.8-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml file and download dependencies
COPY pom.xml .

# Download the dependencies
RUN mvn dependency:go-offline

# Copy the rest of the application source code
COPY src ./src

# Package the application to produce a WAR file
RUN mvn package

FROM quay.io/wildfly/wildfly:33.0.0.Final-jdk21

# Add Oracle driver module
COPY src/main/resources/wildfly/modules $JBOSS_HOME/modules

ARG DB_USER_NAME
ARG DB_USER_PASSWORD

ENV CONFIG_DIR=/tmp/config

ENV JBOSS_HOME=/opt/jboss/wildfly
ENV CONFIG_DIR=/tmp/config
ENV DEPLOYMENT_DIR=$JBOSS_HOME/standalone/deployments

# Create a temporary directory for configuration files
RUN mkdir -p $CONFIG_DIR

# Copy your standalone.xml to the temporary directory
COPY src/main/resources/wildfly/config/standalone.xml $CONFIG_DIR/

# Replace placeholder text in standalone.xml using sed
RUN sed -i 's/DB_USER_NAME/'${DB_USER_NAME}'/g' $CONFIG_DIR/standalone.xml
RUN sed -i 's/DB_USER_PASSWORD/'${DB_USER_PASSWORD}'/g' $CONFIG_DIR/standalone.xml

# Copy the modified standalone.xml to the WildFly configuration directory
RUN cp $CONFIG_DIR/standalone.xml $JBOSS_HOME/standalone/configuration/standalone.xml

# User root to modify war owners
USER root

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent
WORKDIR /opt/jboss/wildfly/standalone/deployments/
COPY --from=build /app/target/*.war $JBOSS_HOME/standalone/deployments/
## Attempt fix permissions error ##
# Attepmt to fix for Error: Could not rename /opt/jboss/wildfly/standalone/configuration/standalone_xml_history/current
# See https://stackoverflow.com/questions/20965737/docker-jboss7-war-commit-server-boot-failed-in-an-unrecoverable-manner
RUN chmod -R 777 $JBOSS_HOME/standalone/configuration/
RUN rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/current
RUN chown -R jboss:jboss $JBOSS_HOME/

# Uncomment next line if war file is in the same dir as the Dockerfile and want to autodeploy
# ADD your-war-file-name.war /opt/jboss/wildfly/standalone/deployments/

EXPOSE 8080 9990 9999 8009 45700 7600 57600

EXPOSE 23364/udp 55200/udp 54200/udp 45688/udp

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]


