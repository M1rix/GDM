package uz.apextech.gdm.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.apextech.gdm.repository.TextLocalizationRepository;
import uz.apextech.gdm.service.TextLocalizationQueryService;
import uz.apextech.gdm.service.TextLocalizationService;
import uz.apextech.gdm.service.criteria.TextLocalizationCriteria;
import uz.apextech.gdm.service.dto.TextLocalizationDTO;
import uz.apextech.gdm.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.apextech.gdm.domain.TextLocalization}.
 */
@RestController
@RequestMapping("/api")
public class TextLocalizationResource {

    private final Logger log = LoggerFactory.getLogger(TextLocalizationResource.class);

    private static final String ENTITY_NAME = "textLocalization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TextLocalizationService textLocalizationService;

    private final TextLocalizationRepository textLocalizationRepository;

    private final TextLocalizationQueryService textLocalizationQueryService;

    public TextLocalizationResource(
        TextLocalizationService textLocalizationService,
        TextLocalizationRepository textLocalizationRepository,
        TextLocalizationQueryService textLocalizationQueryService
    ) {
        this.textLocalizationService = textLocalizationService;
        this.textLocalizationRepository = textLocalizationRepository;
        this.textLocalizationQueryService = textLocalizationQueryService;
    }

    /**
     * {@code POST  /text-localizations} : Create a new textLocalization.
     *
     * @param textLocalizationDTO the textLocalizationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new textLocalizationDTO, or with status {@code 400 (Bad Request)} if the textLocalization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/text-localizations")
    public ResponseEntity<TextLocalizationDTO> createTextLocalization(@Valid @RequestBody TextLocalizationDTO textLocalizationDTO)
        throws URISyntaxException {
        log.debug("REST request to save TextLocalization : {}", textLocalizationDTO);
        if (textLocalizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new textLocalization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TextLocalizationDTO result = textLocalizationService.save(textLocalizationDTO);
        return ResponseEntity
            .created(new URI("/api/text-localizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /text-localizations/:id} : Updates an existing textLocalization.
     *
     * @param id the id of the textLocalizationDTO to save.
     * @param textLocalizationDTO the textLocalizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated textLocalizationDTO,
     * or with status {@code 400 (Bad Request)} if the textLocalizationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the textLocalizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/text-localizations/{id}")
    public ResponseEntity<TextLocalizationDTO> updateTextLocalization(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TextLocalizationDTO textLocalizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TextLocalization : {}, {}", id, textLocalizationDTO);
        if (textLocalizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, textLocalizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!textLocalizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TextLocalizationDTO result = textLocalizationService.save(textLocalizationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, textLocalizationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /text-localizations/:id} : Partial updates given fields of an existing textLocalization, field will ignore if it is null
     *
     * @param id the id of the textLocalizationDTO to save.
     * @param textLocalizationDTO the textLocalizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated textLocalizationDTO,
     * or with status {@code 400 (Bad Request)} if the textLocalizationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the textLocalizationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the textLocalizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/text-localizations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TextLocalizationDTO> partialUpdateTextLocalization(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TextLocalizationDTO textLocalizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TextLocalization partially : {}, {}", id, textLocalizationDTO);
        if (textLocalizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, textLocalizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!textLocalizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TextLocalizationDTO> result = textLocalizationService.partialUpdate(textLocalizationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, textLocalizationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /text-localizations} : get all the textLocalizations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of textLocalizations in body.
     */
    @GetMapping("/text-localizations")
    public ResponseEntity<List<TextLocalizationDTO>> getAllTextLocalizations(
        TextLocalizationCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get TextLocalizations by criteria: {}", criteria);
        Page<TextLocalizationDTO> page = textLocalizationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /text-localizations/count} : count all the textLocalizations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/text-localizations/count")
    public ResponseEntity<Long> countTextLocalizations(TextLocalizationCriteria criteria) {
        log.debug("REST request to count TextLocalizations by criteria: {}", criteria);
        return ResponseEntity.ok().body(textLocalizationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /text-localizations/:id} : get the "id" textLocalization.
     *
     * @param id the id of the textLocalizationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the textLocalizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/text-localizations/{id}")
    public ResponseEntity<TextLocalizationDTO> getTextLocalization(@PathVariable Long id) {
        log.debug("REST request to get TextLocalization : {}", id);
        Optional<TextLocalizationDTO> textLocalizationDTO = textLocalizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(textLocalizationDTO);
    }

    /**
     * {@code DELETE  /text-localizations/:id} : delete the "id" textLocalization.
     *
     * @param id the id of the textLocalizationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/text-localizations/{id}")
    public ResponseEntity<Void> deleteTextLocalization(@PathVariable Long id) {
        log.debug("REST request to delete TextLocalization : {}", id);
        textLocalizationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
