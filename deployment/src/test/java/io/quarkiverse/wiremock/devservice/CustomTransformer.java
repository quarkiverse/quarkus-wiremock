package io.quarkiverse.wiremock.devservice;

import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

public class CustomTransformer implements ResponseDefinitionTransformerV2 {
    @Override
    public ResponseDefinition transform(ServeEvent serveEvent) {
        ResponseDefinition responseDefinition = serveEvent.getResponseDefinition();
        responseDefinition.getTransformerParameters().put("custom", "good");
        return responseDefinition;
    }

    @Override
    public String getName() {
        return "custom-transformer";
    }
}
