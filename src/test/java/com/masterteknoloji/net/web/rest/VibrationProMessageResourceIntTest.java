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

    private static final Float DEFAULT_TEMPERATURE = 1F;
    private static final Float UPDATED_TEMPERATURE = 2F;

    private static final Float DEFAULT_X_VELOCITY = 1F;
    private static final Float UPDATED_X_VELOCITY = 2F;

    private static final Float DEFAULT_X_ACCELERATION = 1F;
    private static final Float UPDATED_X_ACCELERATION = 2F;

    private static final Float DEFAULT_Y_VELOCITY = 1F;
    private static final Float UPDATED_Y_VELOCITY = 2F;

    private static final Float DEFAULT_Y_ACCELERATION = 1F;
    private static final Float UPDATED_Y_ACCELERATION = 2F;

    private static final Float DEFAULT_Z_VELOCITY = 1F;
    private static final Float UPDATED_Z_VELOCITY = 2F;

    private static final Float DEFAULT_Z_ACCELERATION = 1F;
    private static final Float UPDATED_Z_ACCELERATION = 2F;

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
            .temperature(DEFAULT_TEMPERATURE)
            .xVelocity(DEFAULT_X_VELOCITY)
            .xAcceleration(DEFAULT_X_ACCELERATION)
            .yVelocity(DEFAULT_Y_VELOCITY)
            .yAcceleration(DEFAULT_Y_ACCELERATION)
            .zVelocity(DEFAULT_Z_VELOCITY)
            .zAcceleration(DEFAULT_Z_ACCELERATION);
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
        assertThat(testVibrationProMessage.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testVibrationProMessage.getxVelocity()).isEqualTo(DEFAULT_X_VELOCITY);
        assertThat(testVibrationProMessage.getxAcceleration()).isEqualTo(DEFAULT_X_ACCELERATION);
        assertThat(testVibrationProMessage.getyVelocity()).isEqualTo(DEFAULT_Y_VELOCITY);
        assertThat(testVibrationProMessage.getyAcceleration()).isEqualTo(DEFAULT_Y_ACCELERATION);
        assertThat(testVibrationProMessage.getzVelocity()).isEqualTo(DEFAULT_Z_VELOCITY);
        assertThat(testVibrationProMessage.getzAcceleration()).isEqualTo(DEFAULT_Z_ACCELERATION);
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
            .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE.doubleValue())))
            .andExpect(jsonPath("$.[*].xVelocity").value(hasItem(DEFAULT_X_VELOCITY.doubleValue())))
            .andExpect(jsonPath("$.[*].xAcceleration").value(hasItem(DEFAULT_X_ACCELERATION.doubleValue())))
            .andExpect(jsonPath("$.[*].yVelocity").value(hasItem(DEFAULT_Y_VELOCITY.doubleValue())))
            .andExpect(jsonPath("$.[*].yAcceleration").value(hasItem(DEFAULT_Y_ACCELERATION.doubleValue())))
            .andExpect(jsonPath("$.[*].zVelocity").value(hasItem(DEFAULT_Z_VELOCITY.doubleValue())))
            .andExpect(jsonPath("$.[*].zAcceleration").value(hasItem(DEFAULT_Z_ACCELERATION.doubleValue())));
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
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE.doubleValue()))
            .andExpect(jsonPath("$.xVelocity").value(DEFAULT_X_VELOCITY.doubleValue()))
            .andExpect(jsonPath("$.xAcceleration").value(DEFAULT_X_ACCELERATION.doubleValue()))
            .andExpect(jsonPath("$.yVelocity").value(DEFAULT_Y_VELOCITY.doubleValue()))
            .andExpect(jsonPath("$.yAcceleration").value(DEFAULT_Y_ACCELERATION.doubleValue()))
            .andExpect(jsonPath("$.zVelocity").value(DEFAULT_Z_VELOCITY.doubleValue()))
            .andExpect(jsonPath("$.zAcceleration").value(DEFAULT_Z_ACCELERATION.doubleValue()));
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
            .temperature(UPDATED_TEMPERATURE)
            .xVelocity(UPDATED_X_VELOCITY)
            .xAcceleration(UPDATED_X_ACCELERATION)
            .yVelocity(UPDATED_Y_VELOCITY)
            .yAcceleration(UPDATED_Y_ACCELERATION)
            .zVelocity(UPDATED_Z_VELOCITY)
            .zAcceleration(UPDATED_Z_ACCELERATION);

        restVibrationProMessageMockMvc.perform(put("/api/vibration-pro-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVibrationProMessage)))
            .andExpect(status().isOk());

        // Validate the VibrationProMessage in the database
        List<VibrationProMessage> vibrationProMessageList = vibrationProMessageRepository.findAll();
        assertThat(vibrationProMessageList).hasSize(databaseSizeBeforeUpdate);
        VibrationProMessage testVibrationProMessage = vibrationProMessageList.get(vibrationProMessageList.size() - 1);
        assertThat(testVibrationProMessage.getBatteryValue()).isEqualTo(UPDATED_BATTERY_VALUE);
        assertThat(testVibrationProMessage.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testVibrationProMessage.getxVelocity()).isEqualTo(UPDATED_X_VELOCITY);
        assertThat(testVibrationProMessage.getxAcceleration()).isEqualTo(UPDATED_X_ACCELERATION);
        assertThat(testVibrationProMessage.getyVelocity()).isEqualTo(UPDATED_Y_VELOCITY);
        assertThat(testVibrationProMessage.getyAcceleration()).isEqualTo(UPDATED_Y_ACCELERATION);
        assertThat(testVibrationProMessage.getzVelocity()).isEqualTo(UPDATED_Z_VELOCITY);
        assertThat(testVibrationProMessage.getzAcceleration()).isEqualTo(UPDATED_Z_ACCELERATION);
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
