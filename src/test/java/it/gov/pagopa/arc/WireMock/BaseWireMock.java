package it.gov.pagopa.arc.WireMock;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import it.gov.pagopa.arc.utils.TestUtils;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(
        initializers = BaseWireMock.WireMockInitializer.class)
public class BaseWireMock {
    public static final String WIREMOCK_TEST_PROP2BASEPATH_MAP_PREFIX =  "wireMock-test.prop2basePath.";
    private static final Map<String,String> propertiesMap = new HashMap<>();

    @RegisterExtension
    public static com.github.tomakehurst.wiremock.junit5.WireMockExtension serverWireMockExtension = initServerWiremock();

    public static com.github.tomakehurst.wiremock.junit5.WireMockExtension initServerWiremock() {
        String stubsFolder = "src/test/resources/stub";
        int httpPort=0;

        // re-using shutdown server port in order to let Spring loaded configuration still valid
        if (serverWireMockExtension != null ) {
            try {
                httpPort = serverWireMockExtension.getRuntimeInfo().getHttpPort();

                serverWireMockExtension.shutdownServer();
                // waiting server stop, releasing ports
                TestUtils.wait(200, TimeUnit.MILLISECONDS);
            } catch (IllegalStateException e){
                // Do Nothing: the wiremock server was not started
            }
        }

        com.github.tomakehurst.wiremock.junit5.WireMockExtension newWireMockConfig =
                WireMockExtension
                .newInstance()
                .options(
                        WireMockConfiguration
                                .wireMockConfig()
                                .port(httpPort)
                                .usingFilesUnderClasspath(stubsFolder))
                .build();

        return serverWireMockExtension = newWireMockConfig;
    }

    public static class WireMockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {

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
                setWireMockBaseMockedServicePath(applicationContext,serverWireMockExtension.getRuntimeInfo().getHttpBaseUrl(),entry);
            }

            System.out.printf("""
                            ************************
                            Server wiremock:
                            http base url: %s
                            ************************
                            """,
                    serverWireMockExtension.getRuntimeInfo().getHttpBaseUrl());
        }

        private static void setWireMockBaseMockedServicePath(ConfigurableApplicationContext applicationContext, String serverWireMock,Map.Entry<String, String> entry){
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,String.format("%s=%s/%s",entry.getKey(),serverWireMock,entry.getValue()));
        }
    }
}
