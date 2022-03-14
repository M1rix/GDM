package uz.apextech.gdm.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import uz.apextech.gdm.domain.*; // for static metamodels
import uz.apextech.gdm.domain.TextLocalization;
import uz.apextech.gdm.repository.TextLocalizationRepository;
import uz.apextech.gdm.service.criteria.TextLocalizationCriteria;
import uz.apextech.gdm.service.dto.TextLocalizationDTO;
import uz.apextech.gdm.service.mapper.TextLocalizationMapper;

/**
 * Service for executing complex queries for {@link TextLocalization} entities in the database.
 * The main input is a {@link TextLocalizationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TextLocalizationDTO} or a {@link Page} of {@link TextLocalizationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TextLocalizationQueryService extends QueryService<TextLocalization> {

    private final Logger log = LoggerFactory.getLogger(TextLocalizationQueryService.class);

    private final TextLocalizationRepository textLocalizationRepository;

    private final TextLocalizationMapper textLocalizationMapper;

    public TextLocalizationQueryService(
        TextLocalizationRepository textLocalizationRepository,
        TextLocalizationMapper textLocalizationMapper
    ) {
        this.textLocalizationRepository = textLocalizationRepository;
        this.textLocalizationMapper = textLocalizationMapper;
    }

    /**
     * Return a {@link List} of {@link TextLocalizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TextLocalizationDTO> findByCriteria(TextLocalizationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TextLocalization> specification = createSpecification(criteria);
        return textLocalizationMapper.toDto(textLocalizationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TextLocalizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TextLocalizationDTO> findByCriteria(TextLocalizationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TextLocalization> specification = createSpecification(criteria);
        return textLocalizationRepository.findAll(specification, page).map(textLocalizationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TextLocalizationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TextLocalization> specification = createSpecification(criteria);
        return textLocalizationRepository.count(specification);
    }

    /**
     * Function to convert {@link TextLocalizationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TextLocalization> createSpecification(TextLocalizationCriteria criteria) {
        Specification<TextLocalization> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TextLocalization_.id));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), TextLocalization_.code));
            }
            if (criteria.getMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessage(), TextLocalization_.message));
            }
            if (criteria.getLocale() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocale(), TextLocalization_.locale));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), TextLocalization_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), TextLocalization_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), TextLocalization_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), TextLocalization_.lastModifiedDate));
            }
        }
        return specification;
    }
}
