package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.common.config.JsonConfig;

public class TestUtils {
    /**
     * application's objectMapper
     */
    public static ObjectMapper objectMapper = new JsonConfig().objectMapper();

}
