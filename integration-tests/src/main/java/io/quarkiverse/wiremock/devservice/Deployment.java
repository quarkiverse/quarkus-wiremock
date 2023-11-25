package io.quarkiverse.wiremock.devservice;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.runtime.Startup;

@Startup
@ApplicationScoped
class Deployment {
    // required to get an application under test, especially when executing integration tests.
}
