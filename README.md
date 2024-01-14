Introduction
------------

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.jsmpp/jsmpp/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.jsmpp/jsmpp)

jSMPP is a java implementation (SMPP API) of the SMPP protocol (currently supports
SMPP v3.3, v3.4 and v5.0). It provides interfaces to communicate with a Message Center
or an ESME (External Short Message Entity) and is able to handle
traffic of 3000-5000 messages per second. 

jSMPP is not a high-level library. People looking for a quick way to
get started with SMPP may be better of using an abstraction layer such
as the Apache Camel SMPP component [Apache Camel SMPP component](https://camel.apache.org/smpp.html)

Travis-CI status:
-----------------

[![Build Status](https://travis-ci.com/opentelecoms-org/jsmpp.svg?branch=master)](https://travis-ci.com/opentelecoms-org/jsmpp)

History
-------

The project started on Google Code: http://code.google.com/p/jsmpp/

It was maintained by uudashr on GitHub until 2013.

It is now a community project maintained at https://jsmpp.org

Release procedure
-----------------

```
mvn deploy -DperformRelease=true -Durl=https://oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=sonatype-nexus-staging -Dgpg.passphrase=\<yourpassphrase\>
```

  * log in here: https://oss.sonatype.org
  * click the 'Staging Repositories' link
  * select the repository and click close
  * select the repository and click release

License
-------

Copyright (C) 2007-2013, Nuruddin Ashr <uudashr@gmail.com>
Copyright (C) 2012-2013, Denis Kostousov <denis.kostousov@gmail.com>
Copyright (C) 2014, Daniel Pocock http://danielpocock.com
Copyright (C) 2016-2024, Pim Moerenhout <pim.moerenhout@gmail.com>

This project is licensed under the Apache Software License 2.0.
