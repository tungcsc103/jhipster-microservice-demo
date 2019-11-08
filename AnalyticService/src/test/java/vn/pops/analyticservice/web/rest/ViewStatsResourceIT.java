package vn.pops.analyticservice.web.rest;

import vn.pops.analyticservice.AbstractCassandraTest;
import vn.pops.analyticservice.AnalyticServiceApp;
import vn.pops.analyticservice.domain.ViewStats;
import vn.pops.analyticservice.repository.ViewStatsRepository;
import vn.pops.analyticservice.service.ViewStatsService;
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


import java.util.List;
import java.util.UUID;

import static vn.pops.analyticservice.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ViewStatsResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = AnalyticServiceApp.class)
public class ViewStatsResourceIT extends AbstractCassandraTest {

    private static final Long DEFAULT_TOTAL_VIEW = 1L;
    private static final Long UPDATED_TOTAL_VIEW = 2L;
    private static final Long SMALLER_TOTAL_VIEW = 1L - 1L;

    private static final Long DEFAULT_TOTAL_LIKE = 1L;
    private static final Long UPDATED_TOTAL_LIKE = 2L;
    private static final Long SMALLER_TOTAL_LIKE = 1L - 1L;

    @Autowired
    private ViewStatsRepository viewStatsRepository;

    @Autowired
    private ViewStatsService viewStatsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restViewStatsMockMvc;

    private ViewStats viewStats;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ViewStatsResource viewStatsResource = new ViewStatsResource(viewStatsService);
        this.restViewStatsMockMvc = MockMvcBuilders.standaloneSetup(viewStatsResource)
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
    public static ViewStats createEntity() {
        ViewStats viewStats = new ViewStats()
            .totalView(DEFAULT_TOTAL_VIEW)
            .totalLike(DEFAULT_TOTAL_LIKE);
        return viewStats;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViewStats createUpdatedEntity() {
        ViewStats viewStats = new ViewStats()
            .totalView(UPDATED_TOTAL_VIEW)
            .totalLike(UPDATED_TOTAL_LIKE);
        return viewStats;
    }

    @BeforeEach
    public void initTest() {
        viewStatsRepository.deleteAll();
        viewStats = createEntity();
    }

    @Test
    public void createViewStats() throws Exception {
        int databaseSizeBeforeCreate = viewStatsRepository.findAll().size();

        // Create the ViewStats
        restViewStatsMockMvc.perform(post("/api/view-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(viewStats)))
            .andExpect(status().isCreated());

        // Validate the ViewStats in the database
        List<ViewStats> viewStatsList = viewStatsRepository.findAll();
        assertThat(viewStatsList).hasSize(databaseSizeBeforeCreate + 1);
        ViewStats testViewStats = viewStatsList.get(viewStatsList.size() - 1);
        assertThat(testViewStats.getTotalView()).isEqualTo(DEFAULT_TOTAL_VIEW);
        assertThat(testViewStats.getTotalLike()).isEqualTo(DEFAULT_TOTAL_LIKE);
    }

    @Test
    public void createViewStatsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = viewStatsRepository.findAll().size();

        // Create the ViewStats with an existing ID
        viewStats.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restViewStatsMockMvc.perform(post("/api/view-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(viewStats)))
            .andExpect(status().isBadRequest());

        // Validate the ViewStats in the database
        List<ViewStats> viewStatsList = viewStatsRepository.findAll();
        assertThat(viewStatsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void getAllViewStats() throws Exception {
        // Initialize the database
        viewStats.setId(UUID.randomUUID());
        viewStatsRepository.save(viewStats);

        // Get all the viewStatsList
        restViewStatsMockMvc.perform(get("/api/view-stats"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(viewStats.getId().toString())))
            .andExpect(jsonPath("$.[*].totalView").value(hasItem(DEFAULT_TOTAL_VIEW.intValue())))
            .andExpect(jsonPath("$.[*].totalLike").value(hasItem(DEFAULT_TOTAL_LIKE.intValue())));
    }
    
    @Test
    public void getViewStats() throws Exception {
        // Initialize the database
        viewStats.setId(UUID.randomUUID());
        viewStatsRepository.save(viewStats);

        // Get the viewStats
        restViewStatsMockMvc.perform(get("/api/view-stats/{id}", viewStats.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(viewStats.getId().toString()))
            .andExpect(jsonPath("$.totalView").value(DEFAULT_TOTAL_VIEW.intValue()))
            .andExpect(jsonPath("$.totalLike").value(DEFAULT_TOTAL_LIKE.intValue()));
    }

    @Test
    public void getNonExistingViewStats() throws Exception {
        // Get the viewStats
        restViewStatsMockMvc.perform(get("/api/view-stats/{id}", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateViewStats() throws Exception {
        // Initialize the database
        viewStats.setId(UUID.randomUUID());
        viewStatsService.save(viewStats);

        int databaseSizeBeforeUpdate = viewStatsRepository.findAll().size();

        // Update the viewStats
        ViewStats updatedViewStats = viewStatsRepository.findById(viewStats.getId()).get();
        updatedViewStats
            .totalView(UPDATED_TOTAL_VIEW)
            .totalLike(UPDATED_TOTAL_LIKE);

        restViewStatsMockMvc.perform(put("/api/view-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedViewStats)))
            .andExpect(status().isOk());

        // Validate the ViewStats in the database
        List<ViewStats> viewStatsList = viewStatsRepository.findAll();
        assertThat(viewStatsList).hasSize(databaseSizeBeforeUpdate);
        ViewStats testViewStats = viewStatsList.get(viewStatsList.size() - 1);
        assertThat(testViewStats.getTotalView()).isEqualTo(UPDATED_TOTAL_VIEW);
        assertThat(testViewStats.getTotalLike()).isEqualTo(UPDATED_TOTAL_LIKE);
    }

    @Test
    public void updateNonExistingViewStats() throws Exception {
        int databaseSizeBeforeUpdate = viewStatsRepository.findAll().size();

        // Create the ViewStats

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewStatsMockMvc.perform(put("/api/view-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(viewStats)))
            .andExpect(status().isBadRequest());

        // Validate the ViewStats in the database
        List<ViewStats> viewStatsList = viewStatsRepository.findAll();
        assertThat(viewStatsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteViewStats() throws Exception {
        // Initialize the database
        viewStats.setId(UUID.randomUUID());
        viewStatsService.save(viewStats);

        int databaseSizeBeforeDelete = viewStatsRepository.findAll().size();

        // Delete the viewStats
        restViewStatsMockMvc.perform(delete("/api/view-stats/{id}", viewStats.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ViewStats> viewStatsList = viewStatsRepository.findAll();
        assertThat(viewStatsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ViewStats.class);
        ViewStats viewStats1 = new ViewStats();
        viewStats1.setId(UUID.randomUUID());
        ViewStats viewStats2 = new ViewStats();
        viewStats2.setId(viewStats1.getId());
        assertThat(viewStats1).isEqualTo(viewStats2);
        viewStats2.setId(UUID.randomUUID());
        assertThat(viewStats1).isNotEqualTo(viewStats2);
        viewStats1.setId(null);
        assertThat(viewStats1).isNotEqualTo(viewStats2);
    }
}
