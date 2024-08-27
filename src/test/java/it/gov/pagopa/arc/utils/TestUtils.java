package it.gov.pagopa.arc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.config.JsonConfig;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TestUtils {
    private TestUtils(){}
    /**
     * application's objectMapper
     */
    public static final ObjectMapper objectMapper = new JsonConfig().objectMapper();

    public static void wait(long timeout, TimeUnit timeoutUnit) {
        try{
            Awaitility.await()
                    .pollDelay(Duration.ZERO)
                    .timeout(timeout, timeoutUnit)
                    .pollInterval(timeout, timeoutUnit)
                    .until(()->false);
        } catch (ConditionTimeoutException ex){
            // Do Nothing
        }
    }

}
