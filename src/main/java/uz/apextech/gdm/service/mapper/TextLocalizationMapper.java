package uz.apextech.gdm.service.mapper;

import org.mapstruct.*;
import uz.apextech.gdm.domain.TextLocalization;
import uz.apextech.gdm.service.dto.TextLocalizationDTO;

/**
 * Mapper for the entity {@link TextLocalization} and its DTO {@link TextLocalizationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TextLocalizationMapper extends EntityMapper<TextLocalizationDTO, TextLocalization> {}
