## Quarkus extension for running Wiremock in DEVELOPMENT mode

This extension is currently not published in any maven repository.
If you want to test this extension you would need to check out locally and run ``mvn install``. This will install the extension to your local maven repository.

Afterwards add the following dependency to your pom.xml.  
```xml
<dependency>
  <groupId>io.quarkiverse</groupId>
  <artifactId>wiremock-dev</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

The current properties are
```properties
quarkus.wiremock.dev.enabled=true
quarkus.wiremock.dev.path=<path to wiremock files>
quarkus.wiremock.dev.port=8089
quarkus.wiremock.dev.reload=true
```

This is a very basic way of running Wiremock together with Quarkus. 
Further development needed.