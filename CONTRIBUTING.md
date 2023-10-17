# Contributing at quarkus-wiremock

We are beyond excited to see that you want to contribute! We would love to accept your contributions. Navigate through
the following to understand more about contributing.

## Prerequisites

- Java 11
- Git client

## Module overview

| Module           | Description                                                                                                                                                                                                                                                                                        |
|------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| deployment       | The deployment module is used during the augmentation phase of the build and contains the major logic to configure and ramp-up ``WireMock`` as a ``DevService``. In addition, this module also contains test classes to test basic ``DevService`` functionality.                                   |
| runtime          | This is the module the application's developer will add as application dependency to enable ``WireMock`` as a ``DevService``. For this extension the runtime module is almost empty since everything will happen at build time (please refer to the deployment module).                            |
| test             | This module can be used during test execution to retrieve a reference of the running ``WireMock`` instance managed by the ``DevService``. This is quite helpful when a developer wants to describe the ``WireMock`` behavior programmatically. For more details please refer to the documentation. |
| integration-test | Contains Quarkus tests to verify the proper functionality of the extension. This module uses the ``WireMock`` extension in a way how a business application would use it.                                                                                                                          |
| docs             | Project documentation happens via [Asciidoctor](https://asciidoctor.org/) for content and [Antora](https://antora.org/) for navigation. The resulting documentation will be published to [Quarkiverse Docs](https://docs.quarkiverse.io/).                                                         |

## Repository configuration

> **⚠️** Required for Windows user only

```shell
git config core.eol lf
git config core.autocrlf input
```

## Follow these Steps

- [Fork the repository](https://github.com/quarkiverse/quarkus-wiremock/fork)

- Clone the project locally

``` 
git clone https://github.com/<github-username>/quarkus-wiremock.git
``` 

- Create a new branch

```
git checkout -b <your branch_name>
```

After creating a new branch start making your changes and push them once they are done. After that you
can create a ` pull_request`.

- Push your changes

```
git push origin <your branch_name>
```

Now you have to wait for the review. The project maintainer will review your PR and merge it after the approval. If you
want to support, please give a ⭐

### Things to remember before making changes

#### Build the project before pushing any changes

```
./mvnw clean verify
```

Please make sure to build your code before pushing it. By doing so it's guaranteed that your contribution is aligned
with our formatting rules.

#### Stay in sync with upstream

Before making any contribution make sure your local `main` keep up-to-date with upstream `main`. To do that type the
following commands.

- First add upstream

```
git remote add upstream https://github.com/quarkiverse/quarkus-wiremock.git
```

- Pull all changes from upstream

```
 git pull upstream main
```

- Keep your fork up-to-date

```
  git push origin main
```