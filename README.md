https://github.com/davide-moletta/Sport_Intelligence_Android_App/blob/master/Thesis.pdf 

# SportIntelligenceTesiMolettaDavide

#### NEO4J .CONF FILE TO ALLOW NON LOCAL CONNECTION

Edit your neo4j .conf file like this under Network connector connfigurations

```
#*****************************************************************
# Network connector configuration
#*****************************************************************

# With default configuration Neo4j only accepts local connections.
# To accept non-local connections, uncomment this line:
dbms.default_listen_address=0.0.0.0

# You can also choose a specific network interface, and configure a non-default
# port for each connector, by setting their individual listen_address.

# The address at which this server can be reached by its clients. This may be the server's IP address or DNS name, or
# it may be the address of a reverse proxy which sits in front of the server. This setting may be overridden for
# individual connectors below.
#dbms.default_advertised_address=localhost

# You can also choose a specific advertised hostname or IP address, and
# configure an advertised port for each connector, by setting their
# individual advertised_address.

# By default, encryption is turned off.
# To turn on encryption, an ssl policy for the connector needs to be configured
# Read more in SSL policy section in this file for how to define a SSL policy.

# Bolt connector
dbms.connector.bolt.enabled=true
#dbms.connector.bolt.tls_level=DISABLED
dbms.connector.bolt.listen_address=<LOCAL IP ADDRESS OF THE SERVER>:7687
dbms.connector.bolt.advertised_address=<LOCAL IP ADDRESS OF THE SERVER>:7687

# HTTP Connector. There can be zero or one HTTP connectors.
dbms.connector.http.enabled=true
#dbms.connector.http.listen_address=:7474
#dbms.connector.http.advertised_address=:7474

# HTTPS Connector. There can be zero or one HTTPS connectors.
dbms.connector.https.enabled=false
#dbms.connector.https.listen_address=:7473
#dbms.connector.https.advertised_address=:7473

# Cluster Routing Connector. Enables the opening of an additional port to allow
# for internal communication using the same security configuration as CLUSTER
#dbms.routing.enabled=false

# Customize the listen address and advertised address used for the routing connector.
#dbms.routing.listen_address=0.0.0.0:7688
#dbms.routing.advertised_address=:7688

# Number of Neo4j worker threads.
#dbms.threads.worker_count
```
