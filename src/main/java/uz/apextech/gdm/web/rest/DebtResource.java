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
import uz.apextech.gdm.repository.DebtRepository;
import uz.apextech.gdm.service.DebtQueryService;
import uz.apextech.gdm.service.DebtService;
import uz.apextech.gdm.service.criteria.DebtCriteria;
import uz.apextech.gdm.service.dto.DebtDTO;
import uz.apextech.gdm.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.apextech.gdm.domain.Debt}.
 */
@RestController
@RequestMapping("/api")
public class DebtResource {

    private final Logger log = LoggerFactory.getLogger(DebtResource.class);

    private static final String ENTITY_NAME = "debt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DebtService debtService;

    private final DebtRepository debtRepository;

    private final DebtQueryService debtQueryService;

    public DebtResource(DebtService debtService, DebtRepository debtRepository, DebtQueryService debtQueryService) {
        this.debtService = debtService;
        this.debtRepository = debtRepository;
        this.debtQueryService = debtQueryService;
    }

    /**
     * {@code POST  /debts} : Create a new debt.
     *
     * @param debtDTO the debtDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new debtDTO, or with status {@code 400 (Bad Request)} if the debt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/debts")
    public ResponseEntity<DebtDTO> createDebt(@Valid @RequestBody DebtDTO debtDTO) throws URISyntaxException {
        log.debug("REST request to save Debt : {}", debtDTO);
        if (debtDTO.getId() != null) {
            throw new BadRequestAlertException("A new debt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DebtDTO result = debtService.save(debtDTO);
        return ResponseEntity
            .created(new URI("/api/debts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /debts/:id} : Updates an existing debt.
     *
     * @param id the id of the debtDTO to save.
     * @param debtDTO the debtDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated debtDTO,
     * or with status {@code 400 (Bad Request)} if the debtDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the debtDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/debts/{id}")
    public ResponseEntity<DebtDTO> updateDebt(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DebtDTO debtDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Debt : {}, {}", id, debtDTO);
        if (debtDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, debtDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!debtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DebtDTO result = debtService.save(debtDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, debtDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /debts/:id} : Partial updates given fields of an existing debt, field will ignore if it is null
     *
     * @param id the id of the debtDTO to save.
     * @param debtDTO the debtDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated debtDTO,
     * or with status {@code 400 (Bad Request)} if the debtDTO is not valid,
     * or with status {@code 404 (Not Found)} if the debtDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the debtDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/debts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DebtDTO> partialUpdateDebt(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DebtDTO debtDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Debt partially : {}, {}", id, debtDTO);
        if (debtDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, debtDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!debtRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DebtDTO> result = debtService.partialUpdate(debtDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, debtDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /debts} : get all the debts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of debts in body.
     */
    @GetMapping("/debts")
    public ResponseEntity<List<DebtDTO>> getAllDebts(
        DebtCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Debts by criteria: {}", criteria);
        Page<DebtDTO> page = debtQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /debts/count} : count all the debts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/debts/count")
    public ResponseEntity<Long> countDebts(DebtCriteria criteria) {
        log.debug("REST request to count Debts by criteria: {}", criteria);
        return ResponseEntity.ok().body(debtQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /debts/:id} : get the "id" debt.
     *
     * @param id the id of the debtDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the debtDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/debts/{id}")
    public ResponseEntity<DebtDTO> getDebt(@PathVariable Long id) {
        log.debug("REST request to get Debt : {}", id);
        Optional<DebtDTO> debtDTO = debtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(debtDTO);
    }

    /**
     * {@code DELETE  /debts/:id} : delete the "id" debt.
     *
     * @param id the id of the debtDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/debts/{id}")
    public ResponseEntity<Void> deleteDebt(@PathVariable Long id) {
        log.debug("REST request to delete Debt : {}", id);
        debtService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
