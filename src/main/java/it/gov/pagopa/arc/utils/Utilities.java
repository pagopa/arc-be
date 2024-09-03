package it.gov.pagopa.arc.utils;


import it.gov.pagopa.arc.exception.custom.BizEventsInvalidAmountException;
import it.gov.pagopa.arc.exception.custom.BizEventsInvalidDateException;
import it.gov.pagopa.arc.exception.custom.ZendeskAssistanceInvalidUserEmailException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Slf4j
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
            throw new BizEventsInvalidAmountException("Invalid amount format");
        }
    }

    /** To convert Date from String to ZonedDateTime*/
    public static ZonedDateTime dateStringToZonedDateTime(String dateString){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
            return  ZonedDateTime.parse(dateString, formatter);
        }catch (DateTimeParseException e){
            throw new BizEventsInvalidDateException("Invalid date format");
        }
    }

    /**
     * To log the full error stack
     *
     * @param ex exception to log
     */
    public static void logExceptionDetails(RuntimeException ex){
        log.error("Exception occurred: {} - {}", ex.getClass().getSimpleName(), ex.getMessage(), ex);
    }

    /**
     * To extract name value from email
     */
    public static String extractNameFromEmailAssistanceToken(String userMail){
        String nameExtracted;

        if(!StringUtils.isBlank(userMail) && userMail.contains("@")){
            int index = userMail.indexOf("@");
            nameExtracted = userMail.substring(0, index);
        }else {
            throw new ZendeskAssistanceInvalidUserEmailException("Invalid user email [%s]".formatted(userMail));
        }
        return  nameExtracted;
    }
}
