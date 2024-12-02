package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.exception.custom.BizEventsInvalidAmountException;
import it.gov.pagopa.arc.exception.custom.BizEventsInvalidDateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MapperUtilitiesTest {

    @ParameterizedTest
    @CsvSource(value = {
            "2024-04-11T06:56:14.845126, 2024-04-11T06:56:14Z",
            "2023-02-08T06:56:18.542235, 2023-02-08T06:56:18Z",
            "2023-02-08T06:56:18, 2023-02-08T06:56:18Z"
    })
    void givenDateTimeWithMillisWhenCallTruncateToSecondsThenReturnDateTimeWithoutMillis(LocalDateTime dateTime, ZonedDateTime expectedDateTime) {
        //when
        ZonedDateTime zonedDateTime = MapperUtilities.convertToZonedDateTimeAndTruncateSeconds(dateTime);
        //then
        Assertions.assertEquals(expectedDateTime, zonedDateTime);
        System.out.println(zonedDateTime);
    }

    @ParameterizedTest
    @CsvSource(delimiter = ';', value = {
            "1,123,456.80; 112345680",
            "2,681.52; 268152",
            "200.35; 20035",
            "54.1; 5410",
            "2; 200",
            "0.24; 24"
    })
    void givenCorrectEuroStringWhenCallEuroToCentsThenReturnAmountCents(String euroString, Long expected) {
        //given
        //when
        Long amountCents = MapperUtilities.euroToCents(euroString);
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
                () -> MapperUtilities.euroToCents(euroString));
        Assertions.assertEquals("Invalid amount format",exception.getMessage());

    }

    @Test
    void givenCorrectDateStringWhenCallDateStringToZonedDateTimeThenReturnZonedDateTime() {
        //given
        String dateString = "2022-12-22T13:07:25Z";
        ZonedDateTime expected = ZonedDateTime.parse(dateString, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        //when
        ZonedDateTime zonedDateTime = MapperUtilities.dateStringToZonedDateTime(dateString);
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
                () -> MapperUtilities.dateStringToZonedDateTime(wrongDateString));
        Assertions.assertEquals("Invalid date format",exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("valueSource")
    void givenCalculateTotalAmountWhenThen(Long amount, Long fee, Long totalAmount) {
        //when
        Long totalAmountResult = MapperUtilities.calculateTotalAmount(amount, fee);
        //then
        assertEquals(totalAmount, totalAmountResult);
    }

    static Stream<Arguments> valueSource() {
        return Stream.of(
                Arguments.of(500L, 20L, 520L),
                Arguments.of(21L, 11L, 32L),
                Arguments.of(320L, null, 320L),
                Arguments.of(null, 19L, null),
                Arguments.of(null, null, null)
        );
    }
}