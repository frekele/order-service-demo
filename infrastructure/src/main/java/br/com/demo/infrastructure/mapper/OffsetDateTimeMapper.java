package br.com.demo.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public class OffsetDateTimeMapper {

    public static final OffsetDateTimeMapper INSTANCE = Mappers.getMapper(OffsetDateTimeMapper.class);

    public OffsetDateTime toOffsetDateTime(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.atOffset(OffsetDateTime.now().getOffset()) : null;
    }

}
