package it.gov.pagopa.arc.dto.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

class DateTimeTruncateTest {

    @ParameterizedTest
    @CsvSource(value = {
            "2024-04-11T06:56:14.845126, 2024-04-11T06:56:14",
            "2023-02-08T06:56:18.542235, 2023-02-08T06:56:18",
            "2023-02-08T06:56:18, 2023-02-08T06:56:18"
    })
    void givenDateTimeWithMillisWhenCallTruncateToSecondsThenReturnDateTimeWithoutMillis(LocalDateTime dateTime, LocalDateTime expectedDateTime) {
        //when
        LocalDateTime localDateTime = DateTimeTruncate.truncateToSeconds(dateTime);
        //then
        Assertions.assertEquals(expectedDateTime, localDateTime);
    }
}