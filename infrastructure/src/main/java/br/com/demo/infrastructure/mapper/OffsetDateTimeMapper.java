package br.com.demo.infrastructure.mapper;

import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public class OffsetDateTimeMapper {

    public OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atOffset(OffsetDateTime.now().getOffset()) : null;
    }

}
