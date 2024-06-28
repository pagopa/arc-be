package it.gov.pagopa.arc.utils;


import it.gov.pagopa.arc.exception.custom.BizEventsInvocationException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class Utilities {
    private Utilities(){}

    /** To convert euro into cents */
    public static Long euroToCents(String euroString){
        Double euroDouble = euroStringToDouble(euroString);
        return Math.round(euroDouble * 100);
    }

    /** To convert euro from String to double */
    private static Double euroStringToDouble(String euroString){
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
            Number parse = nf.parse(euroString);
            return parse.doubleValue();
        }catch (ParseException e){
            throw new BizEventsInvocationException("Invalid amount format");
        }
    }

    /** To convert Date from String to ZonedDateTime*/
    public static ZonedDateTime dateStringToZonedDateTime(String dateString){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
            return  ZonedDateTime.parse(dateString, formatter);
        }catch (DateTimeParseException e){
            throw new BizEventsInvocationException("Invalid date format");
        }
    }
}
