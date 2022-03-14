package uz.apextech.gdm.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import uz.apextech.gdm.domain.TextLocalization;

/**
 * Spring Data SQL repository for the TextLocalization entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TextLocalizationRepository extends JpaRepository<TextLocalization, Long>, JpaSpecificationExecutor<TextLocalization> {}
