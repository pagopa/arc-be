package it.gov.pagopa.arc.dto.mapper;

import it.gov.pagopa.arc.exception.custom.BizEventsInvalidAmountException;
import it.gov.pagopa.arc.exception.custom.BizEventsInvalidDateException;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Mapper
public interface MapperUtilities {

    @Named("truncateToSeconds")
    static LocalDateTime truncateToSeconds(LocalDateTime dateTime){
        return dateTime.truncatedTo(ChronoUnit.SECONDS);
    }

    /** To convert euro into cents */
    @Named("euroToCents")
    static Long euroToCents(String euroString){
        Double euroDouble = euroStringToDouble(euroString);
        return Math.round(euroDouble * 100);
    }

    /** To convert euro from String to double */
    private static Double euroStringToDouble(String euroString){
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.US);
            Number parse = nf.parse(euroString);
            return parse.doubleValue();
        }catch (ParseException e){
            throw new BizEventsInvalidAmountException("Invalid amount format");
        }
    }

    /** To convert Date from String to ZonedDateTime*/
    @Named("dateStringToZonedDateTime")
    static ZonedDateTime dateStringToZonedDateTime(String dateString){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
            return  ZonedDateTime.parse(dateString, formatter);
        }catch (DateTimeParseException e){
            throw new BizEventsInvalidDateException("Invalid date format");
        }
    }
}
