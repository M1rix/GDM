package uz.apextech.gdm.service.mapper;

import org.mapstruct.*;
import uz.apextech.gdm.domain.Debt;
import uz.apextech.gdm.service.dto.DebtDTO;

/**
 * Mapper for the entity {@link Debt} and its DTO {@link DebtDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CurrencyMapper.class })
public interface DebtMapper extends EntityMapper<DebtDTO, Debt> {
    @Mapping(target = "profile", source = "profile", qualifiedByName = "id")
    @Mapping(target = "currency", source = "currency", qualifiedByName = "id")
    DebtDTO toDto(Debt s);
}
