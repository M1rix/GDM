package uz.apextech.gdm.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DebtMapperTest {

    private DebtMapper debtMapper;

    @BeforeEach
    public void setUp() {
        debtMapper = new DebtMapperImpl();
    }
}
