package uz.apextech.gdm.service.mapper;

import org.mapstruct.*;
import uz.apextech.gdm.domain.Currency;
import uz.apextech.gdm.service.dto.CurrencyDTO;

/**
 * Mapper for the entity {@link Currency} and its DTO {@link CurrencyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CurrencyMapper extends EntityMapper<CurrencyDTO, Currency> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CurrencyDTO toDtoId(Currency currency);
}
