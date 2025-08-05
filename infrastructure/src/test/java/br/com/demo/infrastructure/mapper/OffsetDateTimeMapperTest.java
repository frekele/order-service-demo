package br.com.demo.infrastructure.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.*;

class OffsetDateTimeMapperTest {

    private OffsetDateTimeMapper offsetDateTimeMapper;

    @BeforeEach
    void setUp() {
        offsetDateTimeMapper = new OffsetDateTimeMapper();
    }

    @Test
    @DisplayName("Should convert LocalDateTime to OffsetDateTime when given a valid LocalDateTime")
    void toOffsetDateTime_whenLocalDateTimeIsNotNull_shouldReturnOffsetDateTime() {
        LocalDateTime localDateTime = LocalDateTime.of(2023, 10, 27, 10, 0, 0);
        ZoneOffset expectedOffset = OffsetDateTime.now().getOffset();

        OffsetDateTime result = offsetDateTimeMapper.toOffsetDateTime(localDateTime);

        assertThat(result).isNotNull();
        assertThat(result.toLocalDateTime()).isEqualTo(localDateTime);
        assertThat(result.getOffset()).isEqualTo(expectedOffset);
    }

    @Test
    @DisplayName("Should return null when given a null LocalDateTime")
    void toOffsetDateTime_whenLocalDateTimeIsNull_shouldReturnNull() {
        OffsetDateTime result = offsetDateTimeMapper.toOffsetDateTime(null);

        assertThat(result).isNull();
    }
}