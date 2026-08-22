package io.quarkiverse.wiremock.devservice;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.QuarkusTestResourceRepeatable;

@QuarkusTestResource(value = WireMockServerConnector.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(ConnectWireMock.List.class)
public @interface ConnectWireMock {

    /**
     * Name of a configured REST client (as used in the {@code quarkus.rest-client.<name>.url} property) whose base
     * URL should automatically be redirected to the WireMock {@code Dev Service}.
     * <p>
     * Left empty by default, since an application can have multiple REST clients and only some of them may need to
     * be redirected to WireMock for a given test. Repeat {@code @ConnectWireMock} to redirect more than one REST
     * client.
     * <p>
     * This is a single {@code String} rather than a {@code String[]} on purpose: Quarkus decides whether a test
     * resource's owning application context can be reused across test classes by comparing the reflected values of
     * this annotation's attributes, and array-typed attribute values never compare equal in that check (arrays
     * don't implement {@code equals()}), which would force an application restart between every test class carrying
     * this annotation.
     */
    String restClient() default "";

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @QuarkusTestResourceRepeatable(ConnectWireMock.class)
    @interface List {
        ConnectWireMock[] value();
    }
}
