FROM quay.io/wildfly/wildfly:latest-jdk21

# Add Oracle driver module
COPY wildfly/modules $JBOSS_HOME/modules

ARG DB_USER_NAME
ENV DB_USER_NAME=${DB_USER_NAME}
ARG DB_USER_PASSWORD
ENV DB_USER_PASSWORD=${DB_USER_PASSWORD}

RUN /bin/sh -c '$JBOSS_HOME/bin/standalone.sh -c=standalone.xml &' && \
      sleep 100 && \
      cd /tmp && \
      $JBOSS_HOME/bin/jboss-cli.sh --connect --command="/subsystem=datasources/jdbc-driver=oracle:add(driver-name=oracle,driver-module-name=com.oracle,driver-xa-datasource-class-name=oracle.jdbc.xa.client.OracleXADataSource)" && \
      $JBOSS_HOME/bin/jboss-cli.sh --connect --command="data-source add --name=ApiDemoPersistenceDS --connection-url=jdbc:oracle:thin:@host.docker.internal:1521/XEPDB1?oracle.net.disableOob=true --jndi-name=java:jboss/datasources/ApiDemoPersistenceDS --driver-name=oracle --user-name=${DB_USER_NAME} --password=${DB_USER_PASSWORD} --transaction-isolation=TRANSACTION_READ_COMMITTED --min-pool-size=10 --max-pool-size=50 --pool-prefill=true --allocation-retry=3 --allocation-retry-wait-millis=100 --valid-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.oracle.OracleValidConnectionChecker --validate-on-match=false --background-validation=true --background-validation-millis=30000 --stale-connection-checker-class-name=org.jboss.jca.adapters.jdbc.extensions.oracle.OracleStaleConnectionChecker --exception-sorter-class-name=org.jboss.jca.adapters.jdbc.extensions.oracle.OracleExceptionSorter --enabled=true" && \
      $JBOSS_HOME/bin/jboss-cli.sh --connect --command="/extension=org.wildfly.extension.microprofile.openapi-smallrye:add()" && \
      $JBOSS_HOME/bin/jboss-cli.sh --connect --command="/subsystem=microprofile-openapi-smallrye:add()" && \
      $JBOSS_HOME/bin/jboss-cli.sh --connect --command=:shutdown

    # User root to modify war owners
USER root

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent

COPY ./target/*.war $JBOSS_HOME/standalone/deployments/
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


