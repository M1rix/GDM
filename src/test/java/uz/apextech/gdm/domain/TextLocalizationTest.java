package uz.apextech.gdm.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.apextech.gdm.web.rest.TestUtil;

class TextLocalizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TextLocalization.class);
        TextLocalization textLocalization1 = new TextLocalization();
        textLocalization1.setId(1L);
        TextLocalization textLocalization2 = new TextLocalization();
        textLocalization2.setId(textLocalization1.getId());
        assertThat(textLocalization1).isEqualTo(textLocalization2);
        textLocalization2.setId(2L);
        assertThat(textLocalization1).isNotEqualTo(textLocalization2);
        textLocalization1.setId(null);
        assertThat(textLocalization1).isNotEqualTo(textLocalization2);
    }
}
