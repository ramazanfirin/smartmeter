package com.masterteknoloji.net.web.rest;

import com.masterteknoloji.net.SmartmeterApp;

import com.masterteknoloji.net.domain.VibrationProMessage;
import com.masterteknoloji.net.repository.VibrationProMessageRepository;
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
 * Test class for the VibrationProMessageResource REST controller.
 *
 * @see VibrationProMessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartmeterApp.class)
public class VibrationProMessageResourceIntTest {

    private static final Float DEFAULT_BATTERY_VALUE = 1F;
    private static final Float UPDATED_BATTERY_VALUE = 2F;

    private static final Float DEFAULT_X_AXIS_VALUE = 1F;
    private static final Float UPDATED_X_AXIS_VALUE = 2F;

    private static final Float DEFAULT_Y_AXIS_VALUE = 1F;
    private static final Float UPDATED_Y_AXIS_VALUE = 2F;

    private static final Float DEFAULT_Z_AXIS_VALUE = 1F;
    private static final Float UPDATED_Z_AXIS_VALUE = 2F;

    private static final Float DEFAULT_TEMPERATURE = 1F;
    private static final Float UPDATED_TEMPERATURE = 2F;

    @Autowired
    private VibrationProMessageRepository vibrationProMessageRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVibrationProMessageMockMvc;

    private VibrationProMessage vibrationProMessage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VibrationProMessageResource vibrationProMessageResource = new VibrationProMessageResource(vibrationProMessageRepository);
        this.restVibrationProMessageMockMvc = MockMvcBuilders.standaloneSetup(vibrationProMessageResource)
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
    public static VibrationProMessage createEntity(EntityManager em) {
        VibrationProMessage vibrationProMessage = new VibrationProMessage()
            .batteryValue(DEFAULT_BATTERY_VALUE)
            .xAxisValue(DEFAULT_X_AXIS_VALUE)
            .yAxisValue(DEFAULT_Y_AXIS_VALUE)
            .zAxisValue(DEFAULT_Z_AXIS_VALUE)
            .temperature(DEFAULT_TEMPERATURE);
        return vibrationProMessage;
    }

    @Before
    public void initTest() {
        vibrationProMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createVibrationProMessage() throws Exception {
        int databaseSizeBeforeCreate = vibrationProMessageRepository.findAll().size();

        // Create the VibrationProMessage
        restVibrationProMessageMockMvc.perform(post("/api/vibration-pro-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vibrationProMessage)))
            .andExpect(status().isCreated());

        // Validate the VibrationProMessage in the database
        List<VibrationProMessage> vibrationProMessageList = vibrationProMessageRepository.findAll();
        assertThat(vibrationProMessageList).hasSize(databaseSizeBeforeCreate + 1);
        VibrationProMessage testVibrationProMessage = vibrationProMessageList.get(vibrationProMessageList.size() - 1);
        assertThat(testVibrationProMessage.getBatteryValue()).isEqualTo(DEFAULT_BATTERY_VALUE);
        assertThat(testVibrationProMessage.getxAxisValue()).isEqualTo(DEFAULT_X_AXIS_VALUE);
        assertThat(testVibrationProMessage.getyAxisValue()).isEqualTo(DEFAULT_Y_AXIS_VALUE);
        assertThat(testVibrationProMessage.getzAxisValue()).isEqualTo(DEFAULT_Z_AXIS_VALUE);
        assertThat(testVibrationProMessage.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
    }

    @Test
    @Transactional
    public void createVibrationProMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vibrationProMessageRepository.findAll().size();

        // Create the VibrationProMessage with an existing ID
        vibrationProMessage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVibrationProMessageMockMvc.perform(post("/api/vibration-pro-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vibrationProMessage)))
            .andExpect(status().isBadRequest());

        // Validate the VibrationProMessage in the database
        List<VibrationProMessage> vibrationProMessageList = vibrationProMessageRepository.findAll();
        assertThat(vibrationProMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVibrationProMessages() throws Exception {
        // Initialize the database
        vibrationProMessageRepository.saveAndFlush(vibrationProMessage);

        // Get all the vibrationProMessageList
        restVibrationProMessageMockMvc.perform(get("/api/vibration-pro-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vibrationProMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].batteryValue").value(hasItem(DEFAULT_BATTERY_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].xAxisValue").value(hasItem(DEFAULT_X_AXIS_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].yAxisValue").value(hasItem(DEFAULT_Y_AXIS_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].zAxisValue").value(hasItem(DEFAULT_Z_AXIS_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE.doubleValue())));
    }

    @Test
    @Transactional
    public void getVibrationProMessage() throws Exception {
        // Initialize the database
        vibrationProMessageRepository.saveAndFlush(vibrationProMessage);

        // Get the vibrationProMessage
        restVibrationProMessageMockMvc.perform(get("/api/vibration-pro-messages/{id}", vibrationProMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vibrationProMessage.getId().intValue()))
            .andExpect(jsonPath("$.batteryValue").value(DEFAULT_BATTERY_VALUE.doubleValue()))
            .andExpect(jsonPath("$.xAxisValue").value(DEFAULT_X_AXIS_VALUE.doubleValue()))
            .andExpect(jsonPath("$.yAxisValue").value(DEFAULT_Y_AXIS_VALUE.doubleValue()))
            .andExpect(jsonPath("$.zAxisValue").value(DEFAULT_Z_AXIS_VALUE.doubleValue()))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVibrationProMessage() throws Exception {
        // Get the vibrationProMessage
        restVibrationProMessageMockMvc.perform(get("/api/vibration-pro-messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVibrationProMessage() throws Exception {
        // Initialize the database
        vibrationProMessageRepository.saveAndFlush(vibrationProMessage);
        int databaseSizeBeforeUpdate = vibrationProMessageRepository.findAll().size();

        // Update the vibrationProMessage
        VibrationProMessage updatedVibrationProMessage = vibrationProMessageRepository.findOne(vibrationProMessage.getId());
        // Disconnect from session so that the updates on updatedVibrationProMessage are not directly saved in db
        em.detach(updatedVibrationProMessage);
        updatedVibrationProMessage
            .batteryValue(UPDATED_BATTERY_VALUE)
            .xAxisValue(UPDATED_X_AXIS_VALUE)
            .yAxisValue(UPDATED_Y_AXIS_VALUE)
            .zAxisValue(UPDATED_Z_AXIS_VALUE)
            .temperature(UPDATED_TEMPERATURE);

        restVibrationProMessageMockMvc.perform(put("/api/vibration-pro-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVibrationProMessage)))
            .andExpect(status().isOk());

        // Validate the VibrationProMessage in the database
        List<VibrationProMessage> vibrationProMessageList = vibrationProMessageRepository.findAll();
        assertThat(vibrationProMessageList).hasSize(databaseSizeBeforeUpdate);
        VibrationProMessage testVibrationProMessage = vibrationProMessageList.get(vibrationProMessageList.size() - 1);
        assertThat(testVibrationProMessage.getBatteryValue()).isEqualTo(UPDATED_BATTERY_VALUE);
        assertThat(testVibrationProMessage.getxAxisValue()).isEqualTo(UPDATED_X_AXIS_VALUE);
        assertThat(testVibrationProMessage.getyAxisValue()).isEqualTo(UPDATED_Y_AXIS_VALUE);
        assertThat(testVibrationProMessage.getzAxisValue()).isEqualTo(UPDATED_Z_AXIS_VALUE);
        assertThat(testVibrationProMessage.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
    }

    @Test
    @Transactional
    public void updateNonExistingVibrationProMessage() throws Exception {
        int databaseSizeBeforeUpdate = vibrationProMessageRepository.findAll().size();

        // Create the VibrationProMessage

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVibrationProMessageMockMvc.perform(put("/api/vibration-pro-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vibrationProMessage)))
            .andExpect(status().isCreated());

        // Validate the VibrationProMessage in the database
        List<VibrationProMessage> vibrationProMessageList = vibrationProMessageRepository.findAll();
        assertThat(vibrationProMessageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVibrationProMessage() throws Exception {
        // Initialize the database
        vibrationProMessageRepository.saveAndFlush(vibrationProMessage);
        int databaseSizeBeforeDelete = vibrationProMessageRepository.findAll().size();

        // Get the vibrationProMessage
        restVibrationProMessageMockMvc.perform(delete("/api/vibration-pro-messages/{id}", vibrationProMessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VibrationProMessage> vibrationProMessageList = vibrationProMessageRepository.findAll();
        assertThat(vibrationProMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VibrationProMessage.class);
        VibrationProMessage vibrationProMessage1 = new VibrationProMessage();
        vibrationProMessage1.setId(1L);
        VibrationProMessage vibrationProMessage2 = new VibrationProMessage();
        vibrationProMessage2.setId(vibrationProMessage1.getId());
        assertThat(vibrationProMessage1).isEqualTo(vibrationProMessage2);
        vibrationProMessage2.setId(2L);
        assertThat(vibrationProMessage1).isNotEqualTo(vibrationProMessage2);
        vibrationProMessage1.setId(null);
        assertThat(vibrationProMessage1).isNotEqualTo(vibrationProMessage2);
    }
}
