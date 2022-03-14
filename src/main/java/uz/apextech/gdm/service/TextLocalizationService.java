package uz.apextech.gdm.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.apextech.gdm.domain.TextLocalization;
import uz.apextech.gdm.repository.TextLocalizationRepository;
import uz.apextech.gdm.service.dto.TextLocalizationDTO;
import uz.apextech.gdm.service.mapper.TextLocalizationMapper;

/**
 * Service Implementation for managing {@link TextLocalization}.
 */
@Service
@Transactional
public class TextLocalizationService {

    private final Logger log = LoggerFactory.getLogger(TextLocalizationService.class);

    private final TextLocalizationRepository textLocalizationRepository;

    private final TextLocalizationMapper textLocalizationMapper;

    public TextLocalizationService(TextLocalizationRepository textLocalizationRepository, TextLocalizationMapper textLocalizationMapper) {
        this.textLocalizationRepository = textLocalizationRepository;
        this.textLocalizationMapper = textLocalizationMapper;
    }

    /**
     * Save a textLocalization.
     *
     * @param textLocalizationDTO the entity to save.
     * @return the persisted entity.
     */
    public TextLocalizationDTO save(TextLocalizationDTO textLocalizationDTO) {
        log.debug("Request to save TextLocalization : {}", textLocalizationDTO);
        TextLocalization textLocalization = textLocalizationMapper.toEntity(textLocalizationDTO);
        textLocalization = textLocalizationRepository.save(textLocalization);
        return textLocalizationMapper.toDto(textLocalization);
    }

    /**
     * Partially update a textLocalization.
     *
     * @param textLocalizationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TextLocalizationDTO> partialUpdate(TextLocalizationDTO textLocalizationDTO) {
        log.debug("Request to partially update TextLocalization : {}", textLocalizationDTO);

        return textLocalizationRepository
            .findById(textLocalizationDTO.getId())
            .map(existingTextLocalization -> {
                textLocalizationMapper.partialUpdate(existingTextLocalization, textLocalizationDTO);

                return existingTextLocalization;
            })
            .map(textLocalizationRepository::save)
            .map(textLocalizationMapper::toDto);
    }

    /**
     * Get all the textLocalizations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TextLocalizationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TextLocalizations");
        return textLocalizationRepository.findAll(pageable).map(textLocalizationMapper::toDto);
    }

    /**
     * Get one textLocalization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TextLocalizationDTO> findOne(Long id) {
        log.debug("Request to get TextLocalization : {}", id);
        return textLocalizationRepository.findById(id).map(textLocalizationMapper::toDto);
    }

    /**
     * Delete the textLocalization by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TextLocalization : {}", id);
        textLocalizationRepository.deleteById(id);
    }
}
