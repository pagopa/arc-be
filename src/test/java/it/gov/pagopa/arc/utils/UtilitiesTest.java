package it.gov.pagopa.arc.utils;

import it.gov.pagopa.arc.exception.custom.BizEventsInvalidAmountException;
import it.gov.pagopa.arc.exception.custom.BizEventsInvalidDateException;
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
        BizEventsInvalidAmountException exception = assertThrows(BizEventsInvalidAmountException.class,
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
        BizEventsInvalidDateException exception = assertThrows(BizEventsInvalidDateException.class,
                () -> Utilities.dateStringToZonedDateTime(wrongDateString));
        Assertions.assertEquals("Invalid date format",exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "someone@email.com, someone",
            "test.someone@email.it, test.someone",
            "some.one.!test#@email.com, some.one.!test#"
    })
    void givenCorrectEmailFormatWhenCallExtractNameFromEmailAssistanceTokenThenReturnName(String email, String name){
        //when
        String nameFromEmailAssistanceToken = Utilities.extractNameFromEmailAssistanceToken(email);

        //then
        assertEquals(nameFromEmailAssistanceToken, name);
    }

    @Test
    void givenWrongEmailStringWhenExtractNameFromEmailAssistanceTokenThenReturnException() {
        //given
        String wrongEmail = "email";
        //when
        //then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> Utilities.extractNameFromEmailAssistanceToken(wrongEmail));
        Assertions.assertEquals("Invalid user email",exception.getMessage());

    }

}