package uz.apextech.gdm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.apextech.gdm.web.rest.TestUtil;

class TextLocalizationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TextLocalizationDTO.class);
        TextLocalizationDTO textLocalizationDTO1 = new TextLocalizationDTO();
        textLocalizationDTO1.setId(1L);
        TextLocalizationDTO textLocalizationDTO2 = new TextLocalizationDTO();
        assertThat(textLocalizationDTO1).isNotEqualTo(textLocalizationDTO2);
        textLocalizationDTO2.setId(textLocalizationDTO1.getId());
        assertThat(textLocalizationDTO1).isEqualTo(textLocalizationDTO2);
        textLocalizationDTO2.setId(2L);
        assertThat(textLocalizationDTO1).isNotEqualTo(textLocalizationDTO2);
        textLocalizationDTO1.setId(null);
        assertThat(textLocalizationDTO1).isNotEqualTo(textLocalizationDTO2);
    }
}
