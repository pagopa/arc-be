package it.gov.pagopa.arc.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

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
    void givenWrongEuroStringWhenCallEuroToCentsThenReturnNull() {
        //given
        String euroString = "";
        //when
        Long amountCents = Utilities.euroToCents(euroString);
        //then
        Assertions.assertNull( amountCents);
    }

    @Test
    void givenNullWhenCallEuroToCentsThenReturnNull() {
        //when
        Long amountCents = Utilities.euroToCents(null);
        //then
        Assertions.assertNull( amountCents);
    }
    @Test
    void givenValidEuroStringWhenCallEuroStringToLongThenReturnDouble() {
        //given
        String validEuroString = "2.681,52";
        // when
        Double result = Utilities.euroStringToLong(validEuroString);
        // then
        assertNotNull(result);
        assertEquals(2681.52, result);
    }

    @Test
    void givenEmptyEuroStringWhenCallEuroStringToLongThenReturnNull() {
        //given
        String validEuroString = "";
        // when
        Double result = Utilities.euroStringToLong(validEuroString);
        // then
        assertNull(result);
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
    void givenWrongDateStringWhenCallDateStringToZonedDateTimeThenReturnNull(){
        //given
        String wrongDateString = "";
        //when
        ZonedDateTime zonedDateTime = Utilities.dateStringToZonedDateTime(wrongDateString);
        //then
        assertNull(zonedDateTime);
    }
    @Test
    void givenNullDateStringWhenCallDateStringToZonedDateTimeThenReturnNull() {
        // when
        ZonedDateTime zonedDateTime = Utilities.dateStringToZonedDateTime(null);
        // then
        assertNull(zonedDateTime);
    }
}