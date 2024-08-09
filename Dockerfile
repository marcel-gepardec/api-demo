FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn package

FROM quay.io/wildfly/wildfly:33.0.0.Final-jdk21 AS wildfly

ARG DB_USER_NAME
ARG DB_USER_PASSWORD

USER root

# Add Oracle driver module
COPY src/main/resources/wildfly/modules $JBOSS_HOME/modules
# Copy the CLI script into the container
COPY src/main/resources/wildfly/config/wildfly-config.cli $JBOSS_HOME/configuration/wildfly-config.cli
RUN sed -i 's/DB_USER_NAME/'${DB_USER_NAME}'/g' $JBOSS_HOME/configuration/wildfly-config.cli
RUN sed -i 's/DB_USER_PASSWORD/'${DB_USER_PASSWORD}'/g' $JBOSS_HOME/configuration/wildfly-config.cli

# Run the CLI script
RUN $JBOSS_HOME/bin/jboss-cli.sh --file=$JBOSS_HOME/configuration/wildfly-config.cli


RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

WORKDIR /opt/jboss/wildfly/standalone/deployments/
COPY --from=build /app/target/*.war $JBOSS_HOME/standalone/deployments/

RUN chmod -R 777 $JBOSS_HOME/standalone/configuration/
RUN rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/current
RUN chown -R jboss:jboss $JBOSS_HOME/

EXPOSE 8080 9990 9999 8009 45700 7600 57600

EXPOSE 23364/udp 55200/udp 54200/udp 45688/udp

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]


