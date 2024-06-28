package it.gov.pagopa.arc.utils;

import it.gov.pagopa.arc.exception.custom.BizEventsInvocationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UtilitiesTest {

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            "1.123.456,80; 112345680",
            "2.681,52; 268152",
            "200,35; 20035",
            "54,1; 5410",
            "2; 200",
            "0,24; 24"
    })
    void givenCorrectEuroStringWhenCallEuroToCentsThenReturnAmountCents(String euroString, Long expected) {
        //given
        //when
        Long amountCents = Utilities.euroToCents(euroString);
        //then
        Assertions.assertEquals(expected, amountCents);
    }

    @Test
    void givenWrongEuroStringWhenCallEuroToCentsThenReturnException() {
        //given
        String euroString = "";
        //when
        //then
        BizEventsInvocationException exception = assertThrows(BizEventsInvocationException.class,
                () -> Utilities.euroToCents(euroString));
        Assertions.assertEquals("Invalid amount format",exception.getMessage());

    }

    @Test
    void givenCorrectDateStringWhenCallDateStringToZonedDateTimeThenReturnZonedDateTime() {
        //given
        String dateString = "2022-12-22T13:07:25Z";
        ZonedDateTime expected = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        //when
        ZonedDateTime zonedDateTime = Utilities.dateStringToZonedDateTime(dateString);
        //then
        Assertions.assertNotNull(zonedDateTime);
        assertEquals(expected,zonedDateTime);
    }

    @Test
    void givenWrongDateStringWhenCallDateStringToZonedDateTimeThenReturnException(){
        //given
        String wrongDateString = "";
        //when
        //then
        BizEventsInvocationException exception = assertThrows(BizEventsInvocationException.class,
                () -> Utilities.dateStringToZonedDateTime(wrongDateString));
        Assertions.assertEquals("Invalid date format",exception.getMessage());
    }

}