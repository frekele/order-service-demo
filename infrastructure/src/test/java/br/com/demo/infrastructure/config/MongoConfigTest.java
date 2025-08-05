package br.com.demo.infrastructure.config;

import com.mongodb.MongoClientSettings;
import org.bson.UuidRepresentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;

import static org.junit.jupiter.api.Assertions.*;

class MongoConfigTest {

    private MongoConfig mongoConfig;

    @BeforeEach
    void setUp() {
        mongoConfig = new MongoConfig();
    }

    @Test
    @DisplayName("Should return a customizer that sets UUID representation to STANDARD")
    void shouldReturnCustomizerThatSetsUuidRepresentationToStandard() {
        MongoClientSettingsBuilderCustomizer customizer = mongoConfig.mongoClientSettingsBuilderCustomizer();
        assertNotNull(customizer, "Customizer should not be null");

        MongoClientSettings.Builder builder = MongoClientSettings.builder();
        customizer.customize(builder);
        assertEquals(UuidRepresentation.STANDARD, builder.build().getUuidRepresentation(), "UUID representation should be set to STANDARD");
    }
}