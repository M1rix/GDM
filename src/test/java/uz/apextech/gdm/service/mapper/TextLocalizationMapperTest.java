package uz.apextech.gdm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TextLocalizationMapperTest {

    private TextLocalizationMapper textLocalizationMapper;

    @BeforeEach
    public void setUp() {
        textLocalizationMapper = new TextLocalizationMapperImpl();
    }
}
