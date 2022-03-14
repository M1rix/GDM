package uz.apextech.gdm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uz.apextech.gdm.web.rest.TestUtil.sameNumber;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.apextech.gdm.IntegrationTest;
import uz.apextech.gdm.domain.Currency;
import uz.apextech.gdm.domain.Debt;
import uz.apextech.gdm.domain.Profile;
import uz.apextech.gdm.domain.enumeration.DebtStatus;
import uz.apextech.gdm.repository.DebtRepository;
import uz.apextech.gdm.service.criteria.DebtCriteria;
import uz.apextech.gdm.service.dto.DebtDTO;
import uz.apextech.gdm.service.mapper.DebtMapper;

/**
 * Integration tests for the {@link DebtResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DebtResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final DebtStatus DEFAULT_STATUS = DebtStatus.ONGOING;
    private static final DebtStatus UPDATED_STATUS = DebtStatus.PAID_OFF;

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_DEBT_SCORE = 0;
    private static final Integer UPDATED_DEBT_SCORE = 1;
    private static final Integer SMALLER_DEBT_SCORE = 0 - 1;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/debts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private DebtMapper debtMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDebtMockMvc;

    private Debt debt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Debt createEntity(EntityManager em) {
        Debt debt = new Debt()
            .amount(DEFAULT_AMOUNT)
            .status(DEFAULT_STATUS)
            .dueDate(DEFAULT_DUE_DATE)
            .description(DEFAULT_DESCRIPTION)
            .debtScore(DEFAULT_DEBT_SCORE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return debt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Debt createUpdatedEntity(EntityManager em) {
        Debt debt = new Debt()
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .dueDate(UPDATED_DUE_DATE)
            .description(UPDATED_DESCRIPTION)
            .debtScore(UPDATED_DEBT_SCORE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return debt;
    }

    @BeforeEach
    public void initTest() {
        debt = createEntity(em);
    }

    @Test
    @Transactional
    void createDebt() throws Exception {
        int databaseSizeBeforeCreate = debtRepository.findAll().size();
        // Create the Debt
        DebtDTO debtDTO = debtMapper.toDto(debt);
        restDebtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(debtDTO)))
            .andExpect(status().isCreated());

        // Validate the Debt in the database
        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeCreate + 1);
        Debt testDebt = debtList.get(debtList.size() - 1);
        assertThat(testDebt.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testDebt.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDebt.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testDebt.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDebt.getDebtScore()).isEqualTo(DEFAULT_DEBT_SCORE);
        assertThat(testDebt.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDebt.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testDebt.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testDebt.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createDebtWithExistingId() throws Exception {
        // Create the Debt with an existing ID
        debt.setId(1L);
        DebtDTO debtDTO = debtMapper.toDto(debt);

        int databaseSizeBeforeCreate = debtRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDebtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(debtDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Debt in the database
        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = debtRepository.findAll().size();
        // set the field null
        debt.setAmount(null);

        // Create the Debt, which fails.
        DebtDTO debtDTO = debtMapper.toDto(debt);

        restDebtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(debtDTO)))
            .andExpect(status().isBadRequest());

        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = debtRepository.findAll().size();
        // set the field null
        debt.setStatus(null);

        // Create the Debt, which fails.
        DebtDTO debtDTO = debtMapper.toDto(debt);

        restDebtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(debtDTO)))
            .andExpect(status().isBadRequest());

        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDueDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = debtRepository.findAll().size();
        // set the field null
        debt.setDueDate(null);

        // Create the Debt, which fails.
        DebtDTO debtDTO = debtMapper.toDto(debt);

        restDebtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(debtDTO)))
            .andExpect(status().isBadRequest());

        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = debtRepository.findAll().size();
        // set the field null
        debt.setCreatedBy(null);

        // Create the Debt, which fails.
        DebtDTO debtDTO = debtMapper.toDto(debt);

        restDebtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(debtDTO)))
            .andExpect(status().isBadRequest());

        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = debtRepository.findAll().size();
        // set the field null
        debt.setCreatedDate(null);

        // Create the Debt, which fails.
        DebtDTO debtDTO = debtMapper.toDto(debt);

        restDebtMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(debtDTO)))
            .andExpect(status().isBadRequest());

        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDebts() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList
        restDebtMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(debt.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].debtScore").value(hasItem(DEFAULT_DEBT_SCORE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDebt() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get the debt
        restDebtMockMvc
            .perform(get(ENTITY_API_URL_ID, debt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(debt.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.debtScore").value(DEFAULT_DEBT_SCORE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getDebtsByIdFiltering() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        Long id = debt.getId();

        defaultDebtShouldBeFound("id.equals=" + id);
        defaultDebtShouldNotBeFound("id.notEquals=" + id);

        defaultDebtShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDebtShouldNotBeFound("id.greaterThan=" + id);

        defaultDebtShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDebtShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDebtsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where amount equals to DEFAULT_AMOUNT
        defaultDebtShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the debtList where amount equals to UPDATED_AMOUNT
        defaultDebtShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDebtsByAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where amount not equals to DEFAULT_AMOUNT
        defaultDebtShouldNotBeFound("amount.notEquals=" + DEFAULT_AMOUNT);

        // Get all the debtList where amount not equals to UPDATED_AMOUNT
        defaultDebtShouldBeFound("amount.notEquals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDebtsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultDebtShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the debtList where amount equals to UPDATED_AMOUNT
        defaultDebtShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDebtsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where amount is not null
        defaultDebtShouldBeFound("amount.specified=true");

        // Get all the debtList where amount is null
        defaultDebtShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllDebtsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultDebtShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the debtList where amount is greater than or equal to UPDATED_AMOUNT
        defaultDebtShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDebtsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where amount is less than or equal to DEFAULT_AMOUNT
        defaultDebtShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the debtList where amount is less than or equal to SMALLER_AMOUNT
        defaultDebtShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDebtsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where amount is less than DEFAULT_AMOUNT
        defaultDebtShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the debtList where amount is less than UPDATED_AMOUNT
        defaultDebtShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDebtsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where amount is greater than DEFAULT_AMOUNT
        defaultDebtShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the debtList where amount is greater than SMALLER_AMOUNT
        defaultDebtShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDebtsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where status equals to DEFAULT_STATUS
        defaultDebtShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the debtList where status equals to UPDATED_STATUS
        defaultDebtShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDebtsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where status not equals to DEFAULT_STATUS
        defaultDebtShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the debtList where status not equals to UPDATED_STATUS
        defaultDebtShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDebtsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultDebtShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the debtList where status equals to UPDATED_STATUS
        defaultDebtShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDebtsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where status is not null
        defaultDebtShouldBeFound("status.specified=true");

        // Get all the debtList where status is null
        defaultDebtShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllDebtsByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where dueDate equals to DEFAULT_DUE_DATE
        defaultDebtShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

        // Get all the debtList where dueDate equals to UPDATED_DUE_DATE
        defaultDebtShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllDebtsByDueDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where dueDate not equals to DEFAULT_DUE_DATE
        defaultDebtShouldNotBeFound("dueDate.notEquals=" + DEFAULT_DUE_DATE);

        // Get all the debtList where dueDate not equals to UPDATED_DUE_DATE
        defaultDebtShouldBeFound("dueDate.notEquals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllDebtsByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where dueDate in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultDebtShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the debtList where dueDate equals to UPDATED_DUE_DATE
        defaultDebtShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllDebtsByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where dueDate is not null
        defaultDebtShouldBeFound("dueDate.specified=true");

        // Get all the debtList where dueDate is null
        defaultDebtShouldNotBeFound("dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDebtsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where description equals to DEFAULT_DESCRIPTION
        defaultDebtShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the debtList where description equals to UPDATED_DESCRIPTION
        defaultDebtShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDebtsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where description not equals to DEFAULT_DESCRIPTION
        defaultDebtShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the debtList where description not equals to UPDATED_DESCRIPTION
        defaultDebtShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDebtsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDebtShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the debtList where description equals to UPDATED_DESCRIPTION
        defaultDebtShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDebtsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where description is not null
        defaultDebtShouldBeFound("description.specified=true");

        // Get all the debtList where description is null
        defaultDebtShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDebtsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where description contains DEFAULT_DESCRIPTION
        defaultDebtShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the debtList where description contains UPDATED_DESCRIPTION
        defaultDebtShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDebtsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where description does not contain DEFAULT_DESCRIPTION
        defaultDebtShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the debtList where description does not contain UPDATED_DESCRIPTION
        defaultDebtShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDebtsByDebtScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where debtScore equals to DEFAULT_DEBT_SCORE
        defaultDebtShouldBeFound("debtScore.equals=" + DEFAULT_DEBT_SCORE);

        // Get all the debtList where debtScore equals to UPDATED_DEBT_SCORE
        defaultDebtShouldNotBeFound("debtScore.equals=" + UPDATED_DEBT_SCORE);
    }

    @Test
    @Transactional
    void getAllDebtsByDebtScoreIsNotEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where debtScore not equals to DEFAULT_DEBT_SCORE
        defaultDebtShouldNotBeFound("debtScore.notEquals=" + DEFAULT_DEBT_SCORE);

        // Get all the debtList where debtScore not equals to UPDATED_DEBT_SCORE
        defaultDebtShouldBeFound("debtScore.notEquals=" + UPDATED_DEBT_SCORE);
    }

    @Test
    @Transactional
    void getAllDebtsByDebtScoreIsInShouldWork() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where debtScore in DEFAULT_DEBT_SCORE or UPDATED_DEBT_SCORE
        defaultDebtShouldBeFound("debtScore.in=" + DEFAULT_DEBT_SCORE + "," + UPDATED_DEBT_SCORE);

        // Get all the debtList where debtScore equals to UPDATED_DEBT_SCORE
        defaultDebtShouldNotBeFound("debtScore.in=" + UPDATED_DEBT_SCORE);
    }

    @Test
    @Transactional
    void getAllDebtsByDebtScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where debtScore is not null
        defaultDebtShouldBeFound("debtScore.specified=true");

        // Get all the debtList where debtScore is null
        defaultDebtShouldNotBeFound("debtScore.specified=false");
    }

    @Test
    @Transactional
    void getAllDebtsByDebtScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where debtScore is greater than or equal to DEFAULT_DEBT_SCORE
        defaultDebtShouldBeFound("debtScore.greaterThanOrEqual=" + DEFAULT_DEBT_SCORE);

        // Get all the debtList where debtScore is greater than or equal to (DEFAULT_DEBT_SCORE + 1)
        defaultDebtShouldNotBeFound("debtScore.greaterThanOrEqual=" + (DEFAULT_DEBT_SCORE + 1));
    }

    @Test
    @Transactional
    void getAllDebtsByDebtScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where debtScore is less than or equal to DEFAULT_DEBT_SCORE
        defaultDebtShouldBeFound("debtScore.lessThanOrEqual=" + DEFAULT_DEBT_SCORE);

        // Get all the debtList where debtScore is less than or equal to SMALLER_DEBT_SCORE
        defaultDebtShouldNotBeFound("debtScore.lessThanOrEqual=" + SMALLER_DEBT_SCORE);
    }

    @Test
    @Transactional
    void getAllDebtsByDebtScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where debtScore is less than DEFAULT_DEBT_SCORE
        defaultDebtShouldNotBeFound("debtScore.lessThan=" + DEFAULT_DEBT_SCORE);

        // Get all the debtList where debtScore is less than (DEFAULT_DEBT_SCORE + 1)
        defaultDebtShouldBeFound("debtScore.lessThan=" + (DEFAULT_DEBT_SCORE + 1));
    }

    @Test
    @Transactional
    void getAllDebtsByDebtScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where debtScore is greater than DEFAULT_DEBT_SCORE
        defaultDebtShouldNotBeFound("debtScore.greaterThan=" + DEFAULT_DEBT_SCORE);

        // Get all the debtList where debtScore is greater than SMALLER_DEBT_SCORE
        defaultDebtShouldBeFound("debtScore.greaterThan=" + SMALLER_DEBT_SCORE);
    }

    @Test
    @Transactional
    void getAllDebtsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where createdBy equals to DEFAULT_CREATED_BY
        defaultDebtShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the debtList where createdBy equals to UPDATED_CREATED_BY
        defaultDebtShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDebtsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where createdBy not equals to DEFAULT_CREATED_BY
        defaultDebtShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the debtList where createdBy not equals to UPDATED_CREATED_BY
        defaultDebtShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDebtsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultDebtShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the debtList where createdBy equals to UPDATED_CREATED_BY
        defaultDebtShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDebtsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where createdBy is not null
        defaultDebtShouldBeFound("createdBy.specified=true");

        // Get all the debtList where createdBy is null
        defaultDebtShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDebtsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where createdBy contains DEFAULT_CREATED_BY
        defaultDebtShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the debtList where createdBy contains UPDATED_CREATED_BY
        defaultDebtShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDebtsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where createdBy does not contain DEFAULT_CREATED_BY
        defaultDebtShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the debtList where createdBy does not contain UPDATED_CREATED_BY
        defaultDebtShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDebtsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where createdDate equals to DEFAULT_CREATED_DATE
        defaultDebtShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the debtList where createdDate equals to UPDATED_CREATED_DATE
        defaultDebtShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDebtsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultDebtShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the debtList where createdDate not equals to UPDATED_CREATED_DATE
        defaultDebtShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDebtsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultDebtShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the debtList where createdDate equals to UPDATED_CREATED_DATE
        defaultDebtShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDebtsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where createdDate is not null
        defaultDebtShouldBeFound("createdDate.specified=true");

        // Get all the debtList where createdDate is null
        defaultDebtShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDebtsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultDebtShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the debtList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultDebtShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllDebtsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultDebtShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the debtList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultDebtShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllDebtsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultDebtShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the debtList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultDebtShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllDebtsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where lastModifiedBy is not null
        defaultDebtShouldBeFound("lastModifiedBy.specified=true");

        // Get all the debtList where lastModifiedBy is null
        defaultDebtShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDebtsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultDebtShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the debtList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultDebtShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllDebtsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultDebtShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the debtList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultDebtShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllDebtsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultDebtShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the debtList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultDebtShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllDebtsByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultDebtShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the debtList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultDebtShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllDebtsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultDebtShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the debtList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultDebtShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllDebtsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        // Get all the debtList where lastModifiedDate is not null
        defaultDebtShouldBeFound("lastModifiedDate.specified=true");

        // Get all the debtList where lastModifiedDate is null
        defaultDebtShouldNotBeFound("lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDebtsByProfileIsEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);
        Profile profile;
        if (TestUtil.findAll(em, Profile.class).isEmpty()) {
            profile = ProfileResourceIT.createEntity(em);
            em.persist(profile);
            em.flush();
        } else {
            profile = TestUtil.findAll(em, Profile.class).get(0);
        }
        em.persist(profile);
        em.flush();
        debt.setProfile(profile);
        debtRepository.saveAndFlush(debt);
        Long profileId = profile.getId();

        // Get all the debtList where profile equals to profileId
        defaultDebtShouldBeFound("profileId.equals=" + profileId);

        // Get all the debtList where profile equals to (profileId + 1)
        defaultDebtShouldNotBeFound("profileId.equals=" + (profileId + 1));
    }

    @Test
    @Transactional
    void getAllDebtsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);
        Currency currency;
        if (TestUtil.findAll(em, Currency.class).isEmpty()) {
            currency = CurrencyResourceIT.createEntity(em);
            em.persist(currency);
            em.flush();
        } else {
            currency = TestUtil.findAll(em, Currency.class).get(0);
        }
        em.persist(currency);
        em.flush();
        debt.setCurrency(currency);
        debtRepository.saveAndFlush(debt);
        Long currencyId = currency.getId();

        // Get all the debtList where currency equals to currencyId
        defaultDebtShouldBeFound("currencyId.equals=" + currencyId);

        // Get all the debtList where currency equals to (currencyId + 1)
        defaultDebtShouldNotBeFound("currencyId.equals=" + (currencyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDebtShouldBeFound(String filter) throws Exception {
        restDebtMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(debt.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].debtScore").value(hasItem(DEFAULT_DEBT_SCORE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restDebtMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDebtShouldNotBeFound(String filter) throws Exception {
        restDebtMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDebtMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDebt() throws Exception {
        // Get the debt
        restDebtMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDebt() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        int databaseSizeBeforeUpdate = debtRepository.findAll().size();

        // Update the debt
        Debt updatedDebt = debtRepository.findById(debt.getId()).get();
        // Disconnect from session so that the updates on updatedDebt are not directly saved in db
        em.detach(updatedDebt);
        updatedDebt
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .dueDate(UPDATED_DUE_DATE)
            .description(UPDATED_DESCRIPTION)
            .debtScore(UPDATED_DEBT_SCORE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        DebtDTO debtDTO = debtMapper.toDto(updatedDebt);

        restDebtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, debtDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(debtDTO))
            )
            .andExpect(status().isOk());

        // Validate the Debt in the database
        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeUpdate);
        Debt testDebt = debtList.get(debtList.size() - 1);
        assertThat(testDebt.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testDebt.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDebt.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testDebt.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDebt.getDebtScore()).isEqualTo(UPDATED_DEBT_SCORE);
        assertThat(testDebt.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDebt.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDebt.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testDebt.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingDebt() throws Exception {
        int databaseSizeBeforeUpdate = debtRepository.findAll().size();
        debt.setId(count.incrementAndGet());

        // Create the Debt
        DebtDTO debtDTO = debtMapper.toDto(debt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDebtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, debtDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(debtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Debt in the database
        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDebt() throws Exception {
        int databaseSizeBeforeUpdate = debtRepository.findAll().size();
        debt.setId(count.incrementAndGet());

        // Create the Debt
        DebtDTO debtDTO = debtMapper.toDto(debt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDebtMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(debtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Debt in the database
        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDebt() throws Exception {
        int databaseSizeBeforeUpdate = debtRepository.findAll().size();
        debt.setId(count.incrementAndGet());

        // Create the Debt
        DebtDTO debtDTO = debtMapper.toDto(debt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDebtMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(debtDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Debt in the database
        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDebtWithPatch() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        int databaseSizeBeforeUpdate = debtRepository.findAll().size();

        // Update the debt using partial update
        Debt partialUpdatedDebt = new Debt();
        partialUpdatedDebt.setId(debt.getId());

        partialUpdatedDebt
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .dueDate(UPDATED_DUE_DATE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE);

        restDebtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDebt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDebt))
            )
            .andExpect(status().isOk());

        // Validate the Debt in the database
        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeUpdate);
        Debt testDebt = debtList.get(debtList.size() - 1);
        assertThat(testDebt.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testDebt.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDebt.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testDebt.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDebt.getDebtScore()).isEqualTo(DEFAULT_DEBT_SCORE);
        assertThat(testDebt.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testDebt.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDebt.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testDebt.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateDebtWithPatch() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        int databaseSizeBeforeUpdate = debtRepository.findAll().size();

        // Update the debt using partial update
        Debt partialUpdatedDebt = new Debt();
        partialUpdatedDebt.setId(debt.getId());

        partialUpdatedDebt
            .amount(UPDATED_AMOUNT)
            .status(UPDATED_STATUS)
            .dueDate(UPDATED_DUE_DATE)
            .description(UPDATED_DESCRIPTION)
            .debtScore(UPDATED_DEBT_SCORE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restDebtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDebt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDebt))
            )
            .andExpect(status().isOk());

        // Validate the Debt in the database
        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeUpdate);
        Debt testDebt = debtList.get(debtList.size() - 1);
        assertThat(testDebt.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testDebt.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDebt.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testDebt.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDebt.getDebtScore()).isEqualTo(UPDATED_DEBT_SCORE);
        assertThat(testDebt.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testDebt.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testDebt.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testDebt.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingDebt() throws Exception {
        int databaseSizeBeforeUpdate = debtRepository.findAll().size();
        debt.setId(count.incrementAndGet());

        // Create the Debt
        DebtDTO debtDTO = debtMapper.toDto(debt);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDebtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, debtDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(debtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Debt in the database
        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDebt() throws Exception {
        int databaseSizeBeforeUpdate = debtRepository.findAll().size();
        debt.setId(count.incrementAndGet());

        // Create the Debt
        DebtDTO debtDTO = debtMapper.toDto(debt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDebtMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(debtDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Debt in the database
        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDebt() throws Exception {
        int databaseSizeBeforeUpdate = debtRepository.findAll().size();
        debt.setId(count.incrementAndGet());

        // Create the Debt
        DebtDTO debtDTO = debtMapper.toDto(debt);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDebtMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(debtDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Debt in the database
        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDebt() throws Exception {
        // Initialize the database
        debtRepository.saveAndFlush(debt);

        int databaseSizeBeforeDelete = debtRepository.findAll().size();

        // Delete the debt
        restDebtMockMvc
            .perform(delete(ENTITY_API_URL_ID, debt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Debt> debtList = debtRepository.findAll();
        assertThat(debtList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
