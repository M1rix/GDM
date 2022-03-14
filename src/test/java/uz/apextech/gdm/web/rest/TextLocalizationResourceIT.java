package uz.apextech.gdm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import uz.apextech.gdm.domain.TextLocalization;
import uz.apextech.gdm.repository.TextLocalizationRepository;
import uz.apextech.gdm.service.criteria.TextLocalizationCriteria;
import uz.apextech.gdm.service.dto.TextLocalizationDTO;
import uz.apextech.gdm.service.mapper.TextLocalizationMapper;

/**
 * Integration tests for the {@link TextLocalizationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TextLocalizationResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALE = "AA";
    private static final String UPDATED_LOCALE = "BB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/text-localizations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TextLocalizationRepository textLocalizationRepository;

    @Autowired
    private TextLocalizationMapper textLocalizationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTextLocalizationMockMvc;

    private TextLocalization textLocalization;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TextLocalization createEntity(EntityManager em) {
        TextLocalization textLocalization = new TextLocalization()
            .code(DEFAULT_CODE)
            .message(DEFAULT_MESSAGE)
            .locale(DEFAULT_LOCALE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return textLocalization;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TextLocalization createUpdatedEntity(EntityManager em) {
        TextLocalization textLocalization = new TextLocalization()
            .code(UPDATED_CODE)
            .message(UPDATED_MESSAGE)
            .locale(UPDATED_LOCALE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return textLocalization;
    }

    @BeforeEach
    public void initTest() {
        textLocalization = createEntity(em);
    }

    @Test
    @Transactional
    void createTextLocalization() throws Exception {
        int databaseSizeBeforeCreate = textLocalizationRepository.findAll().size();
        // Create the TextLocalization
        TextLocalizationDTO textLocalizationDTO = textLocalizationMapper.toDto(textLocalization);
        restTextLocalizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(textLocalizationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TextLocalization in the database
        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeCreate + 1);
        TextLocalization testTextLocalization = textLocalizationList.get(textLocalizationList.size() - 1);
        assertThat(testTextLocalization.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTextLocalization.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testTextLocalization.getLocale()).isEqualTo(DEFAULT_LOCALE);
        assertThat(testTextLocalization.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTextLocalization.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTextLocalization.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testTextLocalization.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void createTextLocalizationWithExistingId() throws Exception {
        // Create the TextLocalization with an existing ID
        textLocalization.setId(1L);
        TextLocalizationDTO textLocalizationDTO = textLocalizationMapper.toDto(textLocalization);

        int databaseSizeBeforeCreate = textLocalizationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTextLocalizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(textLocalizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TextLocalization in the database
        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = textLocalizationRepository.findAll().size();
        // set the field null
        textLocalization.setCode(null);

        // Create the TextLocalization, which fails.
        TextLocalizationDTO textLocalizationDTO = textLocalizationMapper.toDto(textLocalization);

        restTextLocalizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(textLocalizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = textLocalizationRepository.findAll().size();
        // set the field null
        textLocalization.setMessage(null);

        // Create the TextLocalization, which fails.
        TextLocalizationDTO textLocalizationDTO = textLocalizationMapper.toDto(textLocalization);

        restTextLocalizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(textLocalizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocaleIsRequired() throws Exception {
        int databaseSizeBeforeTest = textLocalizationRepository.findAll().size();
        // set the field null
        textLocalization.setLocale(null);

        // Create the TextLocalization, which fails.
        TextLocalizationDTO textLocalizationDTO = textLocalizationMapper.toDto(textLocalization);

        restTextLocalizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(textLocalizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        int databaseSizeBeforeTest = textLocalizationRepository.findAll().size();
        // set the field null
        textLocalization.setCreatedBy(null);

        // Create the TextLocalization, which fails.
        TextLocalizationDTO textLocalizationDTO = textLocalizationMapper.toDto(textLocalization);

        restTextLocalizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(textLocalizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = textLocalizationRepository.findAll().size();
        // set the field null
        textLocalization.setCreatedDate(null);

        // Create the TextLocalization, which fails.
        TextLocalizationDTO textLocalizationDTO = textLocalizationMapper.toDto(textLocalization);

        restTextLocalizationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(textLocalizationDTO))
            )
            .andExpect(status().isBadRequest());

        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTextLocalizations() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList
        restTextLocalizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(textLocalization.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].locale").value(hasItem(DEFAULT_LOCALE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTextLocalization() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get the textLocalization
        restTextLocalizationMockMvc
            .perform(get(ENTITY_API_URL_ID, textLocalization.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(textLocalization.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.locale").value(DEFAULT_LOCALE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getTextLocalizationsByIdFiltering() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        Long id = textLocalization.getId();

        defaultTextLocalizationShouldBeFound("id.equals=" + id);
        defaultTextLocalizationShouldNotBeFound("id.notEquals=" + id);

        defaultTextLocalizationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTextLocalizationShouldNotBeFound("id.greaterThan=" + id);

        defaultTextLocalizationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTextLocalizationShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where code equals to DEFAULT_CODE
        defaultTextLocalizationShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the textLocalizationList where code equals to UPDATED_CODE
        defaultTextLocalizationShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where code not equals to DEFAULT_CODE
        defaultTextLocalizationShouldNotBeFound("code.notEquals=" + DEFAULT_CODE);

        // Get all the textLocalizationList where code not equals to UPDATED_CODE
        defaultTextLocalizationShouldBeFound("code.notEquals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where code in DEFAULT_CODE or UPDATED_CODE
        defaultTextLocalizationShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the textLocalizationList where code equals to UPDATED_CODE
        defaultTextLocalizationShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where code is not null
        defaultTextLocalizationShouldBeFound("code.specified=true");

        // Get all the textLocalizationList where code is null
        defaultTextLocalizationShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCodeContainsSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where code contains DEFAULT_CODE
        defaultTextLocalizationShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the textLocalizationList where code contains UPDATED_CODE
        defaultTextLocalizationShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where code does not contain DEFAULT_CODE
        defaultTextLocalizationShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the textLocalizationList where code does not contain UPDATED_CODE
        defaultTextLocalizationShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where message equals to DEFAULT_MESSAGE
        defaultTextLocalizationShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the textLocalizationList where message equals to UPDATED_MESSAGE
        defaultTextLocalizationShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByMessageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where message not equals to DEFAULT_MESSAGE
        defaultTextLocalizationShouldNotBeFound("message.notEquals=" + DEFAULT_MESSAGE);

        // Get all the textLocalizationList where message not equals to UPDATED_MESSAGE
        defaultTextLocalizationShouldBeFound("message.notEquals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultTextLocalizationShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the textLocalizationList where message equals to UPDATED_MESSAGE
        defaultTextLocalizationShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where message is not null
        defaultTextLocalizationShouldBeFound("message.specified=true");

        // Get all the textLocalizationList where message is null
        defaultTextLocalizationShouldNotBeFound("message.specified=false");
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByMessageContainsSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where message contains DEFAULT_MESSAGE
        defaultTextLocalizationShouldBeFound("message.contains=" + DEFAULT_MESSAGE);

        // Get all the textLocalizationList where message contains UPDATED_MESSAGE
        defaultTextLocalizationShouldNotBeFound("message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where message does not contain DEFAULT_MESSAGE
        defaultTextLocalizationShouldNotBeFound("message.doesNotContain=" + DEFAULT_MESSAGE);

        // Get all the textLocalizationList where message does not contain UPDATED_MESSAGE
        defaultTextLocalizationShouldBeFound("message.doesNotContain=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLocaleIsEqualToSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where locale equals to DEFAULT_LOCALE
        defaultTextLocalizationShouldBeFound("locale.equals=" + DEFAULT_LOCALE);

        // Get all the textLocalizationList where locale equals to UPDATED_LOCALE
        defaultTextLocalizationShouldNotBeFound("locale.equals=" + UPDATED_LOCALE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLocaleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where locale not equals to DEFAULT_LOCALE
        defaultTextLocalizationShouldNotBeFound("locale.notEquals=" + DEFAULT_LOCALE);

        // Get all the textLocalizationList where locale not equals to UPDATED_LOCALE
        defaultTextLocalizationShouldBeFound("locale.notEquals=" + UPDATED_LOCALE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLocaleIsInShouldWork() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where locale in DEFAULT_LOCALE or UPDATED_LOCALE
        defaultTextLocalizationShouldBeFound("locale.in=" + DEFAULT_LOCALE + "," + UPDATED_LOCALE);

        // Get all the textLocalizationList where locale equals to UPDATED_LOCALE
        defaultTextLocalizationShouldNotBeFound("locale.in=" + UPDATED_LOCALE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLocaleIsNullOrNotNull() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where locale is not null
        defaultTextLocalizationShouldBeFound("locale.specified=true");

        // Get all the textLocalizationList where locale is null
        defaultTextLocalizationShouldNotBeFound("locale.specified=false");
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLocaleContainsSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where locale contains DEFAULT_LOCALE
        defaultTextLocalizationShouldBeFound("locale.contains=" + DEFAULT_LOCALE);

        // Get all the textLocalizationList where locale contains UPDATED_LOCALE
        defaultTextLocalizationShouldNotBeFound("locale.contains=" + UPDATED_LOCALE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLocaleNotContainsSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where locale does not contain DEFAULT_LOCALE
        defaultTextLocalizationShouldNotBeFound("locale.doesNotContain=" + DEFAULT_LOCALE);

        // Get all the textLocalizationList where locale does not contain UPDATED_LOCALE
        defaultTextLocalizationShouldBeFound("locale.doesNotContain=" + UPDATED_LOCALE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where createdBy equals to DEFAULT_CREATED_BY
        defaultTextLocalizationShouldBeFound("createdBy.equals=" + DEFAULT_CREATED_BY);

        // Get all the textLocalizationList where createdBy equals to UPDATED_CREATED_BY
        defaultTextLocalizationShouldNotBeFound("createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCreatedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where createdBy not equals to DEFAULT_CREATED_BY
        defaultTextLocalizationShouldNotBeFound("createdBy.notEquals=" + DEFAULT_CREATED_BY);

        // Get all the textLocalizationList where createdBy not equals to UPDATED_CREATED_BY
        defaultTextLocalizationShouldBeFound("createdBy.notEquals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where createdBy in DEFAULT_CREATED_BY or UPDATED_CREATED_BY
        defaultTextLocalizationShouldBeFound("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY);

        // Get all the textLocalizationList where createdBy equals to UPDATED_CREATED_BY
        defaultTextLocalizationShouldNotBeFound("createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where createdBy is not null
        defaultTextLocalizationShouldBeFound("createdBy.specified=true");

        // Get all the textLocalizationList where createdBy is null
        defaultTextLocalizationShouldNotBeFound("createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where createdBy contains DEFAULT_CREATED_BY
        defaultTextLocalizationShouldBeFound("createdBy.contains=" + DEFAULT_CREATED_BY);

        // Get all the textLocalizationList where createdBy contains UPDATED_CREATED_BY
        defaultTextLocalizationShouldNotBeFound("createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where createdBy does not contain DEFAULT_CREATED_BY
        defaultTextLocalizationShouldNotBeFound("createdBy.doesNotContain=" + DEFAULT_CREATED_BY);

        // Get all the textLocalizationList where createdBy does not contain UPDATED_CREATED_BY
        defaultTextLocalizationShouldBeFound("createdBy.doesNotContain=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where createdDate equals to DEFAULT_CREATED_DATE
        defaultTextLocalizationShouldBeFound("createdDate.equals=" + DEFAULT_CREATED_DATE);

        // Get all the textLocalizationList where createdDate equals to UPDATED_CREATED_DATE
        defaultTextLocalizationShouldNotBeFound("createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCreatedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where createdDate not equals to DEFAULT_CREATED_DATE
        defaultTextLocalizationShouldNotBeFound("createdDate.notEquals=" + DEFAULT_CREATED_DATE);

        // Get all the textLocalizationList where createdDate not equals to UPDATED_CREATED_DATE
        defaultTextLocalizationShouldBeFound("createdDate.notEquals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where createdDate in DEFAULT_CREATED_DATE or UPDATED_CREATED_DATE
        defaultTextLocalizationShouldBeFound("createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE);

        // Get all the textLocalizationList where createdDate equals to UPDATED_CREATED_DATE
        defaultTextLocalizationShouldNotBeFound("createdDate.in=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where createdDate is not null
        defaultTextLocalizationShouldBeFound("createdDate.specified=true");

        // Get all the textLocalizationList where createdDate is null
        defaultTextLocalizationShouldNotBeFound("createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLastModifiedByIsEqualToSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where lastModifiedBy equals to DEFAULT_LAST_MODIFIED_BY
        defaultTextLocalizationShouldBeFound("lastModifiedBy.equals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the textLocalizationList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultTextLocalizationShouldNotBeFound("lastModifiedBy.equals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLastModifiedByIsNotEqualToSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where lastModifiedBy not equals to DEFAULT_LAST_MODIFIED_BY
        defaultTextLocalizationShouldNotBeFound("lastModifiedBy.notEquals=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the textLocalizationList where lastModifiedBy not equals to UPDATED_LAST_MODIFIED_BY
        defaultTextLocalizationShouldBeFound("lastModifiedBy.notEquals=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLastModifiedByIsInShouldWork() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where lastModifiedBy in DEFAULT_LAST_MODIFIED_BY or UPDATED_LAST_MODIFIED_BY
        defaultTextLocalizationShouldBeFound("lastModifiedBy.in=" + DEFAULT_LAST_MODIFIED_BY + "," + UPDATED_LAST_MODIFIED_BY);

        // Get all the textLocalizationList where lastModifiedBy equals to UPDATED_LAST_MODIFIED_BY
        defaultTextLocalizationShouldNotBeFound("lastModifiedBy.in=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLastModifiedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where lastModifiedBy is not null
        defaultTextLocalizationShouldBeFound("lastModifiedBy.specified=true");

        // Get all the textLocalizationList where lastModifiedBy is null
        defaultTextLocalizationShouldNotBeFound("lastModifiedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLastModifiedByContainsSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where lastModifiedBy contains DEFAULT_LAST_MODIFIED_BY
        defaultTextLocalizationShouldBeFound("lastModifiedBy.contains=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the textLocalizationList where lastModifiedBy contains UPDATED_LAST_MODIFIED_BY
        defaultTextLocalizationShouldNotBeFound("lastModifiedBy.contains=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLastModifiedByNotContainsSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where lastModifiedBy does not contain DEFAULT_LAST_MODIFIED_BY
        defaultTextLocalizationShouldNotBeFound("lastModifiedBy.doesNotContain=" + DEFAULT_LAST_MODIFIED_BY);

        // Get all the textLocalizationList where lastModifiedBy does not contain UPDATED_LAST_MODIFIED_BY
        defaultTextLocalizationShouldBeFound("lastModifiedBy.doesNotContain=" + UPDATED_LAST_MODIFIED_BY);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where lastModifiedDate equals to DEFAULT_LAST_MODIFIED_DATE
        defaultTextLocalizationShouldBeFound("lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the textLocalizationList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultTextLocalizationShouldNotBeFound("lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLastModifiedDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where lastModifiedDate not equals to DEFAULT_LAST_MODIFIED_DATE
        defaultTextLocalizationShouldNotBeFound("lastModifiedDate.notEquals=" + DEFAULT_LAST_MODIFIED_DATE);

        // Get all the textLocalizationList where lastModifiedDate not equals to UPDATED_LAST_MODIFIED_DATE
        defaultTextLocalizationShouldBeFound("lastModifiedDate.notEquals=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where lastModifiedDate in DEFAULT_LAST_MODIFIED_DATE or UPDATED_LAST_MODIFIED_DATE
        defaultTextLocalizationShouldBeFound("lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE);

        // Get all the textLocalizationList where lastModifiedDate equals to UPDATED_LAST_MODIFIED_DATE
        defaultTextLocalizationShouldNotBeFound("lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTextLocalizationsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        // Get all the textLocalizationList where lastModifiedDate is not null
        defaultTextLocalizationShouldBeFound("lastModifiedDate.specified=true");

        // Get all the textLocalizationList where lastModifiedDate is null
        defaultTextLocalizationShouldNotBeFound("lastModifiedDate.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTextLocalizationShouldBeFound(String filter) throws Exception {
        restTextLocalizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(textLocalization.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].locale").value(hasItem(DEFAULT_LOCALE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restTextLocalizationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTextLocalizationShouldNotBeFound(String filter) throws Exception {
        restTextLocalizationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTextLocalizationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTextLocalization() throws Exception {
        // Get the textLocalization
        restTextLocalizationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTextLocalization() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        int databaseSizeBeforeUpdate = textLocalizationRepository.findAll().size();

        // Update the textLocalization
        TextLocalization updatedTextLocalization = textLocalizationRepository.findById(textLocalization.getId()).get();
        // Disconnect from session so that the updates on updatedTextLocalization are not directly saved in db
        em.detach(updatedTextLocalization);
        updatedTextLocalization
            .code(UPDATED_CODE)
            .message(UPDATED_MESSAGE)
            .locale(UPDATED_LOCALE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        TextLocalizationDTO textLocalizationDTO = textLocalizationMapper.toDto(updatedTextLocalization);

        restTextLocalizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, textLocalizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(textLocalizationDTO))
            )
            .andExpect(status().isOk());

        // Validate the TextLocalization in the database
        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeUpdate);
        TextLocalization testTextLocalization = textLocalizationList.get(textLocalizationList.size() - 1);
        assertThat(testTextLocalization.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTextLocalization.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testTextLocalization.getLocale()).isEqualTo(UPDATED_LOCALE);
        assertThat(testTextLocalization.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTextLocalization.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTextLocalization.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTextLocalization.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingTextLocalization() throws Exception {
        int databaseSizeBeforeUpdate = textLocalizationRepository.findAll().size();
        textLocalization.setId(count.incrementAndGet());

        // Create the TextLocalization
        TextLocalizationDTO textLocalizationDTO = textLocalizationMapper.toDto(textLocalization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTextLocalizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, textLocalizationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(textLocalizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TextLocalization in the database
        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTextLocalization() throws Exception {
        int databaseSizeBeforeUpdate = textLocalizationRepository.findAll().size();
        textLocalization.setId(count.incrementAndGet());

        // Create the TextLocalization
        TextLocalizationDTO textLocalizationDTO = textLocalizationMapper.toDto(textLocalization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextLocalizationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(textLocalizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TextLocalization in the database
        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTextLocalization() throws Exception {
        int databaseSizeBeforeUpdate = textLocalizationRepository.findAll().size();
        textLocalization.setId(count.incrementAndGet());

        // Create the TextLocalization
        TextLocalizationDTO textLocalizationDTO = textLocalizationMapper.toDto(textLocalization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextLocalizationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(textLocalizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TextLocalization in the database
        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTextLocalizationWithPatch() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        int databaseSizeBeforeUpdate = textLocalizationRepository.findAll().size();

        // Update the textLocalization using partial update
        TextLocalization partialUpdatedTextLocalization = new TextLocalization();
        partialUpdatedTextLocalization.setId(textLocalization.getId());

        partialUpdatedTextLocalization.code(UPDATED_CODE).message(UPDATED_MESSAGE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTextLocalizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTextLocalization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTextLocalization))
            )
            .andExpect(status().isOk());

        // Validate the TextLocalization in the database
        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeUpdate);
        TextLocalization testTextLocalization = textLocalizationList.get(textLocalizationList.size() - 1);
        assertThat(testTextLocalization.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTextLocalization.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testTextLocalization.getLocale()).isEqualTo(DEFAULT_LOCALE);
        assertThat(testTextLocalization.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testTextLocalization.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testTextLocalization.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testTextLocalization.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateTextLocalizationWithPatch() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        int databaseSizeBeforeUpdate = textLocalizationRepository.findAll().size();

        // Update the textLocalization using partial update
        TextLocalization partialUpdatedTextLocalization = new TextLocalization();
        partialUpdatedTextLocalization.setId(textLocalization.getId());

        partialUpdatedTextLocalization
            .code(UPDATED_CODE)
            .message(UPDATED_MESSAGE)
            .locale(UPDATED_LOCALE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restTextLocalizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTextLocalization.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTextLocalization))
            )
            .andExpect(status().isOk());

        // Validate the TextLocalization in the database
        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeUpdate);
        TextLocalization testTextLocalization = textLocalizationList.get(textLocalizationList.size() - 1);
        assertThat(testTextLocalization.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTextLocalization.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testTextLocalization.getLocale()).isEqualTo(UPDATED_LOCALE);
        assertThat(testTextLocalization.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testTextLocalization.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testTextLocalization.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testTextLocalization.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingTextLocalization() throws Exception {
        int databaseSizeBeforeUpdate = textLocalizationRepository.findAll().size();
        textLocalization.setId(count.incrementAndGet());

        // Create the TextLocalization
        TextLocalizationDTO textLocalizationDTO = textLocalizationMapper.toDto(textLocalization);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTextLocalizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, textLocalizationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(textLocalizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TextLocalization in the database
        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTextLocalization() throws Exception {
        int databaseSizeBeforeUpdate = textLocalizationRepository.findAll().size();
        textLocalization.setId(count.incrementAndGet());

        // Create the TextLocalization
        TextLocalizationDTO textLocalizationDTO = textLocalizationMapper.toDto(textLocalization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextLocalizationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(textLocalizationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TextLocalization in the database
        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTextLocalization() throws Exception {
        int databaseSizeBeforeUpdate = textLocalizationRepository.findAll().size();
        textLocalization.setId(count.incrementAndGet());

        // Create the TextLocalization
        TextLocalizationDTO textLocalizationDTO = textLocalizationMapper.toDto(textLocalization);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTextLocalizationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(textLocalizationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TextLocalization in the database
        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTextLocalization() throws Exception {
        // Initialize the database
        textLocalizationRepository.saveAndFlush(textLocalization);

        int databaseSizeBeforeDelete = textLocalizationRepository.findAll().size();

        // Delete the textLocalization
        restTextLocalizationMockMvc
            .perform(delete(ENTITY_API_URL_ID, textLocalization.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TextLocalization> textLocalizationList = textLocalizationRepository.findAll();
        assertThat(textLocalizationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
