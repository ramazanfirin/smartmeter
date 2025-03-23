package com.masterteknoloji.net.web.rest;

import com.masterteknoloji.net.SmartmeterApp;

import com.masterteknoloji.net.domain.CurrentMeterMessage;
import com.masterteknoloji.net.repository.CurrentMeterMessageRepository;
import com.masterteknoloji.net.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.masterteknoloji.net.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CurrentMeterMessageResource REST controller.
 *
 * @see CurrentMeterMessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartmeterApp.class)
public class CurrentMeterMessageResourceIntTest {

    private static final Float DEFAULT_BATTERY_VALUE = 1F;
    private static final Float UPDATED_BATTERY_VALUE = 2F;

    private static final Float DEFAULT_CURRENT = 1F;
    private static final Float UPDATED_CURRENT = 2F;

    private static final Float DEFAULT_TOTAL_ENERGY = 1F;
    private static final Float UPDATED_TOTAL_ENERGY = 2F;

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    @Autowired
    private CurrentMeterMessageRepository currentMeterMessageRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCurrentMeterMessageMockMvc;

    private CurrentMeterMessage currentMeterMessage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CurrentMeterMessageResource currentMeterMessageResource = new CurrentMeterMessageResource(currentMeterMessageRepository);
        this.restCurrentMeterMessageMockMvc = MockMvcBuilders.standaloneSetup(currentMeterMessageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CurrentMeterMessage createEntity(EntityManager em) {
        CurrentMeterMessage currentMeterMessage = new CurrentMeterMessage()
            .batteryValue(DEFAULT_BATTERY_VALUE)
            .current(DEFAULT_CURRENT)
            .totalEnergy(DEFAULT_TOTAL_ENERGY)
            .reason(DEFAULT_REASON);
        return currentMeterMessage;
    }

    @Before
    public void initTest() {
        currentMeterMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createCurrentMeterMessage() throws Exception {
        int databaseSizeBeforeCreate = currentMeterMessageRepository.findAll().size();

        // Create the CurrentMeterMessage
        restCurrentMeterMessageMockMvc.perform(post("/api/current-meter-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currentMeterMessage)))
            .andExpect(status().isCreated());

        // Validate the CurrentMeterMessage in the database
        List<CurrentMeterMessage> currentMeterMessageList = currentMeterMessageRepository.findAll();
        assertThat(currentMeterMessageList).hasSize(databaseSizeBeforeCreate + 1);
        CurrentMeterMessage testCurrentMeterMessage = currentMeterMessageList.get(currentMeterMessageList.size() - 1);
        assertThat(testCurrentMeterMessage.getBatteryValue()).isEqualTo(DEFAULT_BATTERY_VALUE);
        assertThat(testCurrentMeterMessage.getCurrent()).isEqualTo(DEFAULT_CURRENT);
        assertThat(testCurrentMeterMessage.getTotalEnergy()).isEqualTo(DEFAULT_TOTAL_ENERGY);
        assertThat(testCurrentMeterMessage.getReason()).isEqualTo(DEFAULT_REASON);
    }

    @Test
    @Transactional
    public void createCurrentMeterMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = currentMeterMessageRepository.findAll().size();

        // Create the CurrentMeterMessage with an existing ID
        currentMeterMessage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCurrentMeterMessageMockMvc.perform(post("/api/current-meter-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currentMeterMessage)))
            .andExpect(status().isBadRequest());

        // Validate the CurrentMeterMessage in the database
        List<CurrentMeterMessage> currentMeterMessageList = currentMeterMessageRepository.findAll();
        assertThat(currentMeterMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCurrentMeterMessages() throws Exception {
        // Initialize the database
        currentMeterMessageRepository.saveAndFlush(currentMeterMessage);

        // Get all the currentMeterMessageList
        restCurrentMeterMessageMockMvc.perform(get("/api/current-meter-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(currentMeterMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].batteryValue").value(hasItem(DEFAULT_BATTERY_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].current").value(hasItem(DEFAULT_CURRENT.doubleValue())))
            .andExpect(jsonPath("$.[*].totalEnergy").value(hasItem(DEFAULT_TOTAL_ENERGY.doubleValue())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())));
    }

    @Test
    @Transactional
    public void getCurrentMeterMessage() throws Exception {
        // Initialize the database
        currentMeterMessageRepository.saveAndFlush(currentMeterMessage);

        // Get the currentMeterMessage
        restCurrentMeterMessageMockMvc.perform(get("/api/current-meter-messages/{id}", currentMeterMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(currentMeterMessage.getId().intValue()))
            .andExpect(jsonPath("$.batteryValue").value(DEFAULT_BATTERY_VALUE.doubleValue()))
            .andExpect(jsonPath("$.current").value(DEFAULT_CURRENT.doubleValue()))
            .andExpect(jsonPath("$.totalEnergy").value(DEFAULT_TOTAL_ENERGY.doubleValue()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCurrentMeterMessage() throws Exception {
        // Get the currentMeterMessage
        restCurrentMeterMessageMockMvc.perform(get("/api/current-meter-messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCurrentMeterMessage() throws Exception {
        // Initialize the database
        currentMeterMessageRepository.saveAndFlush(currentMeterMessage);
        int databaseSizeBeforeUpdate = currentMeterMessageRepository.findAll().size();

        // Update the currentMeterMessage
        CurrentMeterMessage updatedCurrentMeterMessage = currentMeterMessageRepository.findOne(currentMeterMessage.getId());
        // Disconnect from session so that the updates on updatedCurrentMeterMessage are not directly saved in db
        em.detach(updatedCurrentMeterMessage);
        updatedCurrentMeterMessage
            .batteryValue(UPDATED_BATTERY_VALUE)
            .current(UPDATED_CURRENT)
            .totalEnergy(UPDATED_TOTAL_ENERGY)
            .reason(UPDATED_REASON);

        restCurrentMeterMessageMockMvc.perform(put("/api/current-meter-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCurrentMeterMessage)))
            .andExpect(status().isOk());

        // Validate the CurrentMeterMessage in the database
        List<CurrentMeterMessage> currentMeterMessageList = currentMeterMessageRepository.findAll();
        assertThat(currentMeterMessageList).hasSize(databaseSizeBeforeUpdate);
        CurrentMeterMessage testCurrentMeterMessage = currentMeterMessageList.get(currentMeterMessageList.size() - 1);
        assertThat(testCurrentMeterMessage.getBatteryValue()).isEqualTo(UPDATED_BATTERY_VALUE);
        assertThat(testCurrentMeterMessage.getCurrent()).isEqualTo(UPDATED_CURRENT);
        assertThat(testCurrentMeterMessage.getTotalEnergy()).isEqualTo(UPDATED_TOTAL_ENERGY);
        assertThat(testCurrentMeterMessage.getReason()).isEqualTo(UPDATED_REASON);
    }

    @Test
    @Transactional
    public void updateNonExistingCurrentMeterMessage() throws Exception {
        int databaseSizeBeforeUpdate = currentMeterMessageRepository.findAll().size();

        // Create the CurrentMeterMessage

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCurrentMeterMessageMockMvc.perform(put("/api/current-meter-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(currentMeterMessage)))
            .andExpect(status().isCreated());

        // Validate the CurrentMeterMessage in the database
        List<CurrentMeterMessage> currentMeterMessageList = currentMeterMessageRepository.findAll();
        assertThat(currentMeterMessageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCurrentMeterMessage() throws Exception {
        // Initialize the database
        currentMeterMessageRepository.saveAndFlush(currentMeterMessage);
        int databaseSizeBeforeDelete = currentMeterMessageRepository.findAll().size();

        // Get the currentMeterMessage
        restCurrentMeterMessageMockMvc.perform(delete("/api/current-meter-messages/{id}", currentMeterMessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CurrentMeterMessage> currentMeterMessageList = currentMeterMessageRepository.findAll();
        assertThat(currentMeterMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrentMeterMessage.class);
        CurrentMeterMessage currentMeterMessage1 = new CurrentMeterMessage();
        currentMeterMessage1.setId(1L);
        CurrentMeterMessage currentMeterMessage2 = new CurrentMeterMessage();
        currentMeterMessage2.setId(currentMeterMessage1.getId());
        assertThat(currentMeterMessage1).isEqualTo(currentMeterMessage2);
        currentMeterMessage2.setId(2L);
        assertThat(currentMeterMessage1).isNotEqualTo(currentMeterMessage2);
        currentMeterMessage1.setId(null);
        assertThat(currentMeterMessage1).isNotEqualTo(currentMeterMessage2);
    }
}
