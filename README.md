## Quarkus extension for running Wiremock in DEVELOPMENT mode
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-1-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

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
## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/Spanjer1"><img src="https://avatars.githubusercontent.com/u/40360503?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Spanjer1</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-wiremock/commits?author=Spanjer1" title="Code">💻</a> <a href="#maintenance-Spanjer1" title="Maintenance">🚧</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!