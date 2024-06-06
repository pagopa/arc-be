package it.gov.pagopa.arc.utils;

import it.gov.pagopa.arc.exception.custom.BizEventsInvocationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UtilitiesTest {

    @Test
    void givenCorrectEuroStringWhenCallEuroToCentsThenReturnAmountCents() {
        //given
        String euroString = "2.681,52";
        //when
        Long amountCents = Utilities.euroToCents(euroString);
        //then
        Assertions.assertEquals(268152L, amountCents);
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