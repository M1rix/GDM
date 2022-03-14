package uz.apextech.gdm.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import uz.apextech.gdm.web.rest.TestUtil;

class DebtDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DebtDTO.class);
        DebtDTO debtDTO1 = new DebtDTO();
        debtDTO1.setId(1L);
        DebtDTO debtDTO2 = new DebtDTO();
        assertThat(debtDTO1).isNotEqualTo(debtDTO2);
        debtDTO2.setId(debtDTO1.getId());
        assertThat(debtDTO1).isEqualTo(debtDTO2);
        debtDTO2.setId(2L);
        assertThat(debtDTO1).isNotEqualTo(debtDTO2);
        debtDTO1.setId(null);
        assertThat(debtDTO1).isNotEqualTo(debtDTO2);
    }
}
