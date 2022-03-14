package uz.apextech.gdm.service.mapper;

import org.mapstruct.*;
import uz.apextech.gdm.domain.Profile;
import uz.apextech.gdm.service.dto.ProfileDTO;

/**
 * Mapper for the entity {@link Profile} and its DTO {@link ProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProfileMapper extends EntityMapper<ProfileDTO, Profile> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProfileDTO toDtoId(Profile profile);
}
