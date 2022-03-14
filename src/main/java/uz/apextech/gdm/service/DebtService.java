package uz.apextech.gdm.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.apextech.gdm.domain.Debt;
import uz.apextech.gdm.repository.DebtRepository;
import uz.apextech.gdm.service.dto.DebtDTO;
import uz.apextech.gdm.service.mapper.DebtMapper;

/**
 * Service Implementation for managing {@link Debt}.
 */
@Service
@Transactional
public class DebtService {

    private final Logger log = LoggerFactory.getLogger(DebtService.class);

    private final DebtRepository debtRepository;

    private final DebtMapper debtMapper;

    public DebtService(DebtRepository debtRepository, DebtMapper debtMapper) {
        this.debtRepository = debtRepository;
        this.debtMapper = debtMapper;
    }

    /**
     * Save a debt.
     *
     * @param debtDTO the entity to save.
     * @return the persisted entity.
     */
    public DebtDTO save(DebtDTO debtDTO) {
        log.debug("Request to save Debt : {}", debtDTO);
        Debt debt = debtMapper.toEntity(debtDTO);
        debt = debtRepository.save(debt);
        return debtMapper.toDto(debt);
    }

    /**
     * Partially update a debt.
     *
     * @param debtDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DebtDTO> partialUpdate(DebtDTO debtDTO) {
        log.debug("Request to partially update Debt : {}", debtDTO);

        return debtRepository
            .findById(debtDTO.getId())
            .map(existingDebt -> {
                debtMapper.partialUpdate(existingDebt, debtDTO);

                return existingDebt;
            })
            .map(debtRepository::save)
            .map(debtMapper::toDto);
    }

    /**
     * Get all the debts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DebtDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Debts");
        return debtRepository.findAll(pageable).map(debtMapper::toDto);
    }

    /**
     * Get one debt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DebtDTO> findOne(Long id) {
        log.debug("Request to get Debt : {}", id);
        return debtRepository.findById(id).map(debtMapper::toDto);
    }

    /**
     * Delete the debt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Debt : {}", id);
        debtRepository.deleteById(id);
    }
}
