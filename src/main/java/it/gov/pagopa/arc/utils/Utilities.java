package it.gov.pagopa.arc.utils;


import io.micrometer.common.util.StringUtils;

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
        if(StringUtils.isNotBlank(euroString)){
            Double euroDouble = euroStringToLong(euroString);
            if( euroDouble != null){
                return (long) (euroDouble * 100);
            }else {
                return null;
            }
        } else{
            return null;
        }
    }

    /** To convert euro from String to double */
    public static Double euroStringToLong(String euroString){
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
            Number parse = nf.parse(euroString);
            return parse.doubleValue();
        }catch (ParseException e){
            return null;
        }
    }

    /** To convert Date from String to ZonedDateTime*/
    public static ZonedDateTime dateStringToZonedDateTime(String dateString){
        if(StringUtils.isNotBlank(dateString)){
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");
                return  ZonedDateTime.parse(dateString, formatter);
            }catch (DateTimeParseException e){
                return null;
            }
        }else {
            return null;
        }
    }
}
