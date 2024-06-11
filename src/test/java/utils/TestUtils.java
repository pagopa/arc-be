package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.gov.pagopa.arc.config.JsonConfig;

public class TestUtils {
    private TestUtils(){}
    /**
     * application's objectMapper
     */
    public static final ObjectMapper objectMapper = new JsonConfig().objectMapper();

}
