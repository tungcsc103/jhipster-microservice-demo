package vn.pops.analyticservice.web.rest;

import vn.pops.analyticservice.AbstractCassandraTest;
import vn.pops.analyticservice.AnalyticServiceApp;
import vn.pops.analyticservice.domain.ViewLog;
import vn.pops.analyticservice.repository.ViewLogRepository;
import vn.pops.analyticservice.service.ViewLogService;
import vn.pops.analyticservice.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;


import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static vn.pops.analyticservice.web.rest.TestUtil.sameInstant;
import static vn.pops.analyticservice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ViewLogResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = AnalyticServiceApp.class)
public class ViewLogResourceIT extends AbstractCassandraTest {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_ID = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_ID = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    @Autowired
    private ViewLogRepository viewLogRepository;

    @Autowired
    private ViewLogService viewLogService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restViewLogMockMvc;

    private ViewLog viewLog;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ViewLogResource viewLogResource = new ViewLogResource(viewLogService);
        this.restViewLogMockMvc = MockMvcBuilders.standaloneSetup(viewLogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViewLog createEntity() {
        ViewLog viewLog = new ViewLog()
            .userId(DEFAULT_USER_ID)
            .entityType(DEFAULT_ENTITY_TYPE)
            .entityId(DEFAULT_ENTITY_ID)
            .date(DEFAULT_DATE);
        return viewLog;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViewLog createUpdatedEntity() {
        ViewLog viewLog = new ViewLog()
            .userId(UPDATED_USER_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .date(UPDATED_DATE);
        return viewLog;
    }

    @BeforeEach
    public void initTest() {
        viewLogRepository.deleteAll();
        viewLog = createEntity();
    }

    @Test
    public void createViewLog() throws Exception {
        int databaseSizeBeforeCreate = viewLogRepository.findAll().size();

        // Create the ViewLog
        restViewLogMockMvc.perform(post("/api/view-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(viewLog)))
            .andExpect(status().isCreated());

        // Validate the ViewLog in the database
        List<ViewLog> viewLogList = viewLogRepository.findAll();
        assertThat(viewLogList).hasSize(databaseSizeBeforeCreate + 1);
        ViewLog testViewLog = viewLogList.get(viewLogList.size() - 1);
        assertThat(testViewLog.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testViewLog.getEntityType()).isEqualTo(DEFAULT_ENTITY_TYPE);
        assertThat(testViewLog.getEntityId()).isEqualTo(DEFAULT_ENTITY_ID);
        assertThat(testViewLog.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    public void createViewLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = viewLogRepository.findAll().size();

        // Create the ViewLog with an existing ID
        viewLog.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restViewLogMockMvc.perform(post("/api/view-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(viewLog)))
            .andExpect(status().isBadRequest());

        // Validate the ViewLog in the database
        List<ViewLog> viewLogList = viewLogRepository.findAll();
        assertThat(viewLogList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = viewLogRepository.findAll().size();
        // set the field null
        viewLog.setUserId(null);

        // Create the ViewLog, which fails.

        restViewLogMockMvc.perform(post("/api/view-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(viewLog)))
            .andExpect(status().isBadRequest());

        List<ViewLog> viewLogList = viewLogRepository.findAll();
        assertThat(viewLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void checkEntityTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = viewLogRepository.findAll().size();
        // set the field null
        viewLog.setEntityType(null);

        // Create the ViewLog, which fails.

        restViewLogMockMvc.perform(post("/api/view-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(viewLog)))
            .andExpect(status().isBadRequest());

        List<ViewLog> viewLogList = viewLogRepository.findAll();
        assertThat(viewLogList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllViewLogs() throws Exception {
        // Initialize the database
        viewLog.setId(UUID.randomUUID());
        viewLogRepository.save(viewLog);

        // Get all the viewLogList
        restViewLogMockMvc.perform(get("/api/view-logs"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(viewLog.getId().toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }
    
    @Test
    public void getViewLog() throws Exception {
        // Initialize the database
        viewLog.setId(UUID.randomUUID());
        viewLogRepository.save(viewLog);

        // Get the viewLog
        restViewLogMockMvc.perform(get("/api/view-logs/{id}", viewLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(viewLog.getId().toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE.toString()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    public void getNonExistingViewLog() throws Exception {
        // Get the viewLog
        restViewLogMockMvc.perform(get("/api/view-logs/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateViewLog() throws Exception {
        // Initialize the database
        viewLog.setId(UUID.randomUUID());
        viewLogService.save(viewLog);

        int databaseSizeBeforeUpdate = viewLogRepository.findAll().size();

        // Update the viewLog
        ViewLog updatedViewLog = viewLogRepository.findById(viewLog.getId()).get();
        updatedViewLog
            .userId(UPDATED_USER_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .date(UPDATED_DATE);

        restViewLogMockMvc.perform(put("/api/view-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedViewLog)))
            .andExpect(status().isOk());

        // Validate the ViewLog in the database
        List<ViewLog> viewLogList = viewLogRepository.findAll();
        assertThat(viewLogList).hasSize(databaseSizeBeforeUpdate);
        ViewLog testViewLog = viewLogList.get(viewLogList.size() - 1);
        assertThat(testViewLog.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testViewLog.getEntityType()).isEqualTo(UPDATED_ENTITY_TYPE);
        assertThat(testViewLog.getEntityId()).isEqualTo(UPDATED_ENTITY_ID);
        assertThat(testViewLog.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    public void updateNonExistingViewLog() throws Exception {
        int databaseSizeBeforeUpdate = viewLogRepository.findAll().size();

        // Create the ViewLog

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewLogMockMvc.perform(put("/api/view-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(viewLog)))
            .andExpect(status().isBadRequest());

        // Validate the ViewLog in the database
        List<ViewLog> viewLogList = viewLogRepository.findAll();
        assertThat(viewLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteViewLog() throws Exception {
        // Initialize the database
        viewLog.setId(UUID.randomUUID());
        viewLogService.save(viewLog);

        int databaseSizeBeforeDelete = viewLogRepository.findAll().size();

        // Delete the viewLog
        restViewLogMockMvc.perform(delete("/api/view-logs/{id}", viewLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ViewLog> viewLogList = viewLogRepository.findAll();
        assertThat(viewLogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ViewLog.class);
        ViewLog viewLog1 = new ViewLog();
        viewLog1.setId(UUID.randomUUID());
        ViewLog viewLog2 = new ViewLog();
        viewLog2.setId(viewLog1.getId());
        assertThat(viewLog1).isEqualTo(viewLog2);
        viewLog2.setId(UUID.randomUUID());
        assertThat(viewLog1).isNotEqualTo(viewLog2);
        viewLog1.setId(null);
        assertThat(viewLog1).isNotEqualTo(viewLog2);
    }
}
