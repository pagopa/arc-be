package it.gov.pagopa.arc.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class WireMockConfig {
    public static final String WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX =  "wireMock-test.prop2basePath.";
    private static final Map<String,String> propertiesMap = new HashMap<>();

    public static class WireMockInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().usingFilesUnderClasspath("src/test/resources/stub").dynamicPort());
            wireMockServer.start();

            applicationContext.getBeanFactory().registerSingleton("wireMockServer", wireMockServer);
            applicationContext.addApplicationListener(
                    applicationEvent -> {
                        if (applicationEvent instanceof ContextClosedEvent) {
                            wireMockServer.stop();
                        }
                    });

            applicationContext.getEnvironment().getPropertySources().stream()
                    .filter(propertySource -> propertySource instanceof EnumerablePropertySource)
                    .map(propertySource -> (EnumerablePropertySource<?>) propertySource)
                    .flatMap(propertySource -> Arrays.stream(propertySource.getPropertyNames()))
                    .forEach(key -> {
                        if (key.startsWith(WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX)) {
                            propertiesMap.put(key.substring(WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX.length()), applicationContext.getEnvironment().getProperty(key));
                        }
                    });

            for (Map.Entry<String, String> entry : propertiesMap.entrySet()) {
                setWireMockBaseMockedServicePath(applicationContext,wireMockServer.baseUrl(),entry);
            }

        }
    }

    private static void setWireMockBaseMockedServicePath(ConfigurableApplicationContext applicationContext, String serverWireMock,Map.Entry<String, String> entry){
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,String.format("%s=%s/%s",entry.getKey(),serverWireMock,entry.getValue()));
    }
}
