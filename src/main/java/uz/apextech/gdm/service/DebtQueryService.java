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
import uz.apextech.gdm.domain.Debt;
import uz.apextech.gdm.repository.DebtRepository;
import uz.apextech.gdm.service.criteria.DebtCriteria;
import uz.apextech.gdm.service.dto.DebtDTO;
import uz.apextech.gdm.service.mapper.DebtMapper;

/**
 * Service for executing complex queries for {@link Debt} entities in the database.
 * The main input is a {@link DebtCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DebtDTO} or a {@link Page} of {@link DebtDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DebtQueryService extends QueryService<Debt> {

    private final Logger log = LoggerFactory.getLogger(DebtQueryService.class);

    private final DebtRepository debtRepository;

    private final DebtMapper debtMapper;

    public DebtQueryService(DebtRepository debtRepository, DebtMapper debtMapper) {
        this.debtRepository = debtRepository;
        this.debtMapper = debtMapper;
    }

    /**
     * Return a {@link List} of {@link DebtDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DebtDTO> findByCriteria(DebtCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Debt> specification = createSpecification(criteria);
        return debtMapper.toDto(debtRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DebtDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DebtDTO> findByCriteria(DebtCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Debt> specification = createSpecification(criteria);
        return debtRepository.findAll(specification, page).map(debtMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DebtCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Debt> specification = createSpecification(criteria);
        return debtRepository.count(specification);
    }

    /**
     * Function to convert {@link DebtCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Debt> createSpecification(DebtCriteria criteria) {
        Specification<Debt> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Debt_.id));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), Debt_.amount));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Debt_.status));
            }
            if (criteria.getDueDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDueDate(), Debt_.dueDate));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Debt_.description));
            }
            if (criteria.getDebtScore() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDebtScore(), Debt_.debtScore));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Debt_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Debt_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Debt_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Debt_.lastModifiedDate));
            }
            if (criteria.getProfileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProfileId(), root -> root.join(Debt_.profile, JoinType.LEFT).get(Profile_.id))
                    );
            }
            if (criteria.getCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCurrencyId(), root -> root.join(Debt_.currency, JoinType.LEFT).get(Currency_.id))
                    );
            }
        }
        return specification;
    }
}
