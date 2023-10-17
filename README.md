# Quarkus - WireMock
[![GitHub Actions Status](https://img.shields.io/github/actions/workflow/status/quarkiverse/quarkus-wiremock/build.yml?branch=main&logo=GitHub&style=for-the-badge)](https://github.com/quarkiverse/quarkus-wiremock/actions/workflows/build.yml)
[![Version](https://img.shields.io/maven-central/v/io.quarkiverse.wiremock/quarkus-wiremock-parent?logo=apache-maven&style=for-the-badge)](https://search.maven.org/search?q=g:io.quarkiverse.wiremock%20AND%20a:quarkus-wiremock-parent)
[![License](https://img.shields.io/github/license/quarkusio/quarkus?style=for-the-badge&logo=apache)](https://www.apache.org/licenses/LICENSE-2.0)
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-3-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

## Description

Quarkus [WireMock](https://wiremock.org) extension for tests and local development.

## Compatibility

This Quarkus ``WireMock`` extension provides different version streams.

| WireMock extension  | Quarkus Platform | WireMock | Documentation                                                                                                       |
|---------------------|------------------|----------|---------------------------------------------------------------------------------------------------------------------|
| 0.x.x               | 2.16.x.Final     | 2.35.x   | [Quarkiverse Docs - Wiremock (0.x)](https://quarkiverse.github.io/quarkiverse-docs/quarkus-wiremock/0.x/index.html) |
| 1.x.x (coming soon) | 3.2.x (LTS)      | 3.x      | [Quarkiverse Docs - Wiremock (dev)](https://quarkiverse.github.io/quarkiverse-docs/quarkus-wiremock/dev/index.html) |

## Contribution

### Git repo config
> **⚠️** Required for Windows user only
```shell
git config core.eol lf
git config core.autocrlf input
```

### Module description

| Module           | Description                                                                                                                                                                                                                                                                                        |
|------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| deployment       | The deployment module is used during the augmentation phase of the build and contains the major logic to configure and ramp-up ``WireMock`` as a ``DevService``. In addition, this module also contains test classes to test basic ``DevService`` functionality.                                   |
| runtime          | This is the module the application's developer will add as application dependency to enable ``WireMock`` as a ``DevService``. For this extension the runtime module is almost empty since everything will happen at build time (please refer to the deployment module).                            |
| test             | This module can be used during test execution to retrieve a reference of the running ``WireMock`` instance managed by the ``DevService``. This is quite helpful when a developer wants to describe the ``WireMock`` behavior programmatically. For more details please refer to the documentation. |
| integration-test | Contains Quarkus tests to verify the proper functionality of the extension. This module uses the ``WireMock`` extension in a way how a business application would use it.                                                                                                                          |
| docs             | Project documentation happens via [Asciidoctor](https://asciidoctor.org/) for content and [Antora](https://antora.org/) for navigation. The resulting documentation will be published to [Quarkiverse Docs](https://docs.quarkiverse.io/).                                                         |

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/Spanjer1"><img src="https://avatars.githubusercontent.com/u/40360503?v=4?s=100" width="100px;" alt="Spanjer1"/><br /><sub><b>Spanjer1</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-wiremock/commits?author=Spanjer1" title="Code">💻</a> <a href="#maintenance-Spanjer1" title="Maintenance">🚧</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://techspace.de"><img src="https://avatars.githubusercontent.com/u/3606282?v=4?s=100" width="100px;" alt="Christian Berger"/><br /><sub><b>Christian Berger</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-wiremock/commits?author=chberger" title="Code">💻</a> <a href="#maintenance-chberger" title="Maintenance">🚧</a> <a href="https://github.com/quarkiverse/quarkus-wiremock/commits?author=chberger" title="Documentation">📖</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://wjglerum.nl"><img src="https://avatars.githubusercontent.com/u/7404187?v=4?s=100" width="100px;" alt="Willem Jan Glerum"/><br /><sub><b>Willem Jan Glerum</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-wiremock/commits?author=wjglerum" title="Code">💻</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification.
Contributions of any kind welcome!
