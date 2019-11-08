package vn.pops.statsservive.web.rest;

import vn.pops.statsservive.StatsServiceApp;
import vn.pops.statsservive.domain.UserStats;
import vn.pops.statsservive.repository.UserStatsRepository;
import vn.pops.statsservive.service.UserStatsService;
import vn.pops.statsservive.service.dto.UserStatsDTO;
import vn.pops.statsservive.service.mapper.UserStatsMapper;
import vn.pops.statsservive.web.rest.errors.ExceptionTranslator;

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

import static vn.pops.statsservive.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UserStatsResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = StatsServiceApp.class)
public class UserStatsResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Long DEFAULT_TOTAL_VIEW = 1L;
    private static final Long UPDATED_TOTAL_VIEW = 2L;
    private static final Long SMALLER_TOTAL_VIEW = 1L - 1L;

    private static final Long DEFAULT_TOTAL_LIKE = 1L;
    private static final Long UPDATED_TOTAL_LIKE = 2L;
    private static final Long SMALLER_TOTAL_LIKE = 1L - 1L;

    @Autowired
    private UserStatsRepository userStatsRepository;

    @Autowired
    private UserStatsMapper userStatsMapper;

    @Autowired
    private UserStatsService userStatsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private Validator validator;

    private MockMvc restUserStatsMockMvc;

    private UserStats userStats;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserStatsResource userStatsResource = new UserStatsResource(userStatsService);
        this.restUserStatsMockMvc = MockMvcBuilders.standaloneSetup(userStatsResource)
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
    public static UserStats createEntity() {
        UserStats userStats = new UserStats()
            .userId(DEFAULT_USER_ID)
            .totalView(DEFAULT_TOTAL_VIEW)
            .totalLike(DEFAULT_TOTAL_LIKE);
        return userStats;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserStats createUpdatedEntity() {
        UserStats userStats = new UserStats()
            .userId(UPDATED_USER_ID)
            .totalView(UPDATED_TOTAL_VIEW)
            .totalLike(UPDATED_TOTAL_LIKE);
        return userStats;
    }

    @BeforeEach
    public void initTest() {
        userStatsRepository.deleteAll();
        userStats = createEntity();
    }

    @Test
    public void createUserStats() throws Exception {
        int databaseSizeBeforeCreate = userStatsRepository.findAll().size();

        // Create the UserStats
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);
        restUserStatsMockMvc.perform(post("/api/user-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userStatsDTO)))
            .andExpect(status().isCreated());

        // Validate the UserStats in the database
        List<UserStats> userStatsList = userStatsRepository.findAll();
        assertThat(userStatsList).hasSize(databaseSizeBeforeCreate + 1);
        UserStats testUserStats = userStatsList.get(userStatsList.size() - 1);
        assertThat(testUserStats.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testUserStats.getTotalView()).isEqualTo(DEFAULT_TOTAL_VIEW);
        assertThat(testUserStats.getTotalLike()).isEqualTo(DEFAULT_TOTAL_LIKE);
    }

    @Test
    public void createUserStatsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userStatsRepository.findAll().size();

        // Create the UserStats with an existing ID
        userStats.setId("existing_id");
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserStatsMockMvc.perform(post("/api/user-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userStatsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserStats in the database
        List<UserStats> userStatsList = userStatsRepository.findAll();
        assertThat(userStatsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = userStatsRepository.findAll().size();
        // set the field null
        userStats.setUserId(null);

        // Create the UserStats, which fails.
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);

        restUserStatsMockMvc.perform(post("/api/user-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userStatsDTO)))
            .andExpect(status().isBadRequest());

        List<UserStats> userStatsList = userStatsRepository.findAll();
        assertThat(userStatsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    public void getAllUserStats() throws Exception {
        // Initialize the database
        userStatsRepository.save(userStats);

        // Get all the userStatsList
        restUserStatsMockMvc.perform(get("/api/user-stats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userStats.getId())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.toString())))
            .andExpect(jsonPath("$.[*].totalView").value(hasItem(DEFAULT_TOTAL_VIEW.intValue())))
            .andExpect(jsonPath("$.[*].totalLike").value(hasItem(DEFAULT_TOTAL_LIKE.intValue())));
    }
    
    @Test
    public void getUserStats() throws Exception {
        // Initialize the database
        userStatsRepository.save(userStats);

        // Get the userStats
        restUserStatsMockMvc.perform(get("/api/user-stats/{id}", userStats.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userStats.getId()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.toString()))
            .andExpect(jsonPath("$.totalView").value(DEFAULT_TOTAL_VIEW.intValue()))
            .andExpect(jsonPath("$.totalLike").value(DEFAULT_TOTAL_LIKE.intValue()));
    }

    @Test
    public void getNonExistingUserStats() throws Exception {
        // Get the userStats
        restUserStatsMockMvc.perform(get("/api/user-stats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateUserStats() throws Exception {
        // Initialize the database
        userStatsRepository.save(userStats);

        int databaseSizeBeforeUpdate = userStatsRepository.findAll().size();

        // Update the userStats
        UserStats updatedUserStats = userStatsRepository.findById(userStats.getId()).get();
        updatedUserStats
            .userId(UPDATED_USER_ID)
            .totalView(UPDATED_TOTAL_VIEW)
            .totalLike(UPDATED_TOTAL_LIKE);
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(updatedUserStats);

        restUserStatsMockMvc.perform(put("/api/user-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userStatsDTO)))
            .andExpect(status().isOk());

        // Validate the UserStats in the database
        List<UserStats> userStatsList = userStatsRepository.findAll();
        assertThat(userStatsList).hasSize(databaseSizeBeforeUpdate);
        UserStats testUserStats = userStatsList.get(userStatsList.size() - 1);
        assertThat(testUserStats.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testUserStats.getTotalView()).isEqualTo(UPDATED_TOTAL_VIEW);
        assertThat(testUserStats.getTotalLike()).isEqualTo(UPDATED_TOTAL_LIKE);
    }

    @Test
    public void updateNonExistingUserStats() throws Exception {
        int databaseSizeBeforeUpdate = userStatsRepository.findAll().size();

        // Create the UserStats
        UserStatsDTO userStatsDTO = userStatsMapper.toDto(userStats);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserStatsMockMvc.perform(put("/api/user-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userStatsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserStats in the database
        List<UserStats> userStatsList = userStatsRepository.findAll();
        assertThat(userStatsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deleteUserStats() throws Exception {
        // Initialize the database
        userStatsRepository.save(userStats);

        int databaseSizeBeforeDelete = userStatsRepository.findAll().size();

        // Delete the userStats
        restUserStatsMockMvc.perform(delete("/api/user-stats/{id}", userStats.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserStats> userStatsList = userStatsRepository.findAll();
        assertThat(userStatsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserStats.class);
        UserStats userStats1 = new UserStats();
        userStats1.setId("id1");
        UserStats userStats2 = new UserStats();
        userStats2.setId(userStats1.getId());
        assertThat(userStats1).isEqualTo(userStats2);
        userStats2.setId("id2");
        assertThat(userStats1).isNotEqualTo(userStats2);
        userStats1.setId(null);
        assertThat(userStats1).isNotEqualTo(userStats2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserStatsDTO.class);
        UserStatsDTO userStatsDTO1 = new UserStatsDTO();
        userStatsDTO1.setId("id1");
        UserStatsDTO userStatsDTO2 = new UserStatsDTO();
        assertThat(userStatsDTO1).isNotEqualTo(userStatsDTO2);
        userStatsDTO2.setId(userStatsDTO1.getId());
        assertThat(userStatsDTO1).isEqualTo(userStatsDTO2);
        userStatsDTO2.setId("id2");
        assertThat(userStatsDTO1).isNotEqualTo(userStatsDTO2);
        userStatsDTO1.setId(null);
        assertThat(userStatsDTO1).isNotEqualTo(userStatsDTO2);
    }
}
