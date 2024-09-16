package it.gov.pagopa.arc.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Mapper
public interface DateTimeTruncate {
    @Named("truncateToSeconds")
    static LocalDateTime truncateToSeconds(LocalDateTime dateTime){
        return dateTime.truncatedTo(ChronoUnit.SECONDS);
    }
}
