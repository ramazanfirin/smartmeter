package com.masterteknoloji.net.web.rest;

import com.masterteknoloji.net.SmartmeterApp;

import com.masterteknoloji.net.domain.VibrationEcoMessage;
import com.masterteknoloji.net.repository.VibrationEcoMessageRepository;
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
 * Test class for the VibrationEcoMessageResource REST controller.
 *
 * @see VibrationEcoMessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartmeterApp.class)
public class VibrationEcoMessageResourceIntTest {

    private static final Float DEFAULT_BATTERY_VALUE = 1F;
    private static final Float UPDATED_BATTERY_VALUE = 2F;

    private static final Float DEFAULT_X_AXIS_VALUE = 1F;
    private static final Float UPDATED_X_AXIS_VALUE = 2F;

    private static final Float DEFAULT_Y_AXIS_VALUE = 1F;
    private static final Float UPDATED_Y_AXIS_VALUE = 2F;

    private static final Float DEFAULT_Z_AXIS_VALUE = 1F;
    private static final Float UPDATED_Z_AXIS_VALUE = 2F;

    @Autowired
    private VibrationEcoMessageRepository vibrationEcoMessageRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVibrationEcoMessageMockMvc;

    private VibrationEcoMessage vibrationEcoMessage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VibrationEcoMessageResource vibrationEcoMessageResource = new VibrationEcoMessageResource(vibrationEcoMessageRepository);
        this.restVibrationEcoMessageMockMvc = MockMvcBuilders.standaloneSetup(vibrationEcoMessageResource)
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
    public static VibrationEcoMessage createEntity(EntityManager em) {
        VibrationEcoMessage vibrationEcoMessage = new VibrationEcoMessage()
            .batteryValue(DEFAULT_BATTERY_VALUE)
            .xAxisValue(DEFAULT_X_AXIS_VALUE)
            .yAxisValue(DEFAULT_Y_AXIS_VALUE)
            .zAxisValue(DEFAULT_Z_AXIS_VALUE);
        return vibrationEcoMessage;
    }

    @Before
    public void initTest() {
        vibrationEcoMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createVibrationEcoMessage() throws Exception {
        int databaseSizeBeforeCreate = vibrationEcoMessageRepository.findAll().size();

        // Create the VibrationEcoMessage
        restVibrationEcoMessageMockMvc.perform(post("/api/vibration-eco-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vibrationEcoMessage)))
            .andExpect(status().isCreated());

        // Validate the VibrationEcoMessage in the database
        List<VibrationEcoMessage> vibrationEcoMessageList = vibrationEcoMessageRepository.findAll();
        assertThat(vibrationEcoMessageList).hasSize(databaseSizeBeforeCreate + 1);
        VibrationEcoMessage testVibrationEcoMessage = vibrationEcoMessageList.get(vibrationEcoMessageList.size() - 1);
        assertThat(testVibrationEcoMessage.getBatteryValue()).isEqualTo(DEFAULT_BATTERY_VALUE);
        assertThat(testVibrationEcoMessage.getxAxisValue()).isEqualTo(DEFAULT_X_AXIS_VALUE);
        assertThat(testVibrationEcoMessage.getyAxisValue()).isEqualTo(DEFAULT_Y_AXIS_VALUE);
        assertThat(testVibrationEcoMessage.getzAxisValue()).isEqualTo(DEFAULT_Z_AXIS_VALUE);
    }

    @Test
    @Transactional
    public void createVibrationEcoMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vibrationEcoMessageRepository.findAll().size();

        // Create the VibrationEcoMessage with an existing ID
        vibrationEcoMessage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVibrationEcoMessageMockMvc.perform(post("/api/vibration-eco-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vibrationEcoMessage)))
            .andExpect(status().isBadRequest());

        // Validate the VibrationEcoMessage in the database
        List<VibrationEcoMessage> vibrationEcoMessageList = vibrationEcoMessageRepository.findAll();
        assertThat(vibrationEcoMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVibrationEcoMessages() throws Exception {
        // Initialize the database
        vibrationEcoMessageRepository.saveAndFlush(vibrationEcoMessage);

        // Get all the vibrationEcoMessageList
        restVibrationEcoMessageMockMvc.perform(get("/api/vibration-eco-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vibrationEcoMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].batteryValue").value(hasItem(DEFAULT_BATTERY_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].xAxisValue").value(hasItem(DEFAULT_X_AXIS_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].yAxisValue").value(hasItem(DEFAULT_Y_AXIS_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].zAxisValue").value(hasItem(DEFAULT_Z_AXIS_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    public void getVibrationEcoMessage() throws Exception {
        // Initialize the database
        vibrationEcoMessageRepository.saveAndFlush(vibrationEcoMessage);

        // Get the vibrationEcoMessage
        restVibrationEcoMessageMockMvc.perform(get("/api/vibration-eco-messages/{id}", vibrationEcoMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vibrationEcoMessage.getId().intValue()))
            .andExpect(jsonPath("$.batteryValue").value(DEFAULT_BATTERY_VALUE.doubleValue()))
            .andExpect(jsonPath("$.xAxisValue").value(DEFAULT_X_AXIS_VALUE.doubleValue()))
            .andExpect(jsonPath("$.yAxisValue").value(DEFAULT_Y_AXIS_VALUE.doubleValue()))
            .andExpect(jsonPath("$.zAxisValue").value(DEFAULT_Z_AXIS_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVibrationEcoMessage() throws Exception {
        // Get the vibrationEcoMessage
        restVibrationEcoMessageMockMvc.perform(get("/api/vibration-eco-messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVibrationEcoMessage() throws Exception {
        // Initialize the database
        vibrationEcoMessageRepository.saveAndFlush(vibrationEcoMessage);
        int databaseSizeBeforeUpdate = vibrationEcoMessageRepository.findAll().size();

        // Update the vibrationEcoMessage
        VibrationEcoMessage updatedVibrationEcoMessage = vibrationEcoMessageRepository.findOne(vibrationEcoMessage.getId());
        // Disconnect from session so that the updates on updatedVibrationEcoMessage are not directly saved in db
        em.detach(updatedVibrationEcoMessage);
        updatedVibrationEcoMessage
            .batteryValue(UPDATED_BATTERY_VALUE)
            .xAxisValue(UPDATED_X_AXIS_VALUE)
            .yAxisValue(UPDATED_Y_AXIS_VALUE)
            .zAxisValue(UPDATED_Z_AXIS_VALUE);

        restVibrationEcoMessageMockMvc.perform(put("/api/vibration-eco-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVibrationEcoMessage)))
            .andExpect(status().isOk());

        // Validate the VibrationEcoMessage in the database
        List<VibrationEcoMessage> vibrationEcoMessageList = vibrationEcoMessageRepository.findAll();
        assertThat(vibrationEcoMessageList).hasSize(databaseSizeBeforeUpdate);
        VibrationEcoMessage testVibrationEcoMessage = vibrationEcoMessageList.get(vibrationEcoMessageList.size() - 1);
        assertThat(testVibrationEcoMessage.getBatteryValue()).isEqualTo(UPDATED_BATTERY_VALUE);
        assertThat(testVibrationEcoMessage.getxAxisValue()).isEqualTo(UPDATED_X_AXIS_VALUE);
        assertThat(testVibrationEcoMessage.getyAxisValue()).isEqualTo(UPDATED_Y_AXIS_VALUE);
        assertThat(testVibrationEcoMessage.getzAxisValue()).isEqualTo(UPDATED_Z_AXIS_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingVibrationEcoMessage() throws Exception {
        int databaseSizeBeforeUpdate = vibrationEcoMessageRepository.findAll().size();

        // Create the VibrationEcoMessage

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVibrationEcoMessageMockMvc.perform(put("/api/vibration-eco-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vibrationEcoMessage)))
            .andExpect(status().isCreated());

        // Validate the VibrationEcoMessage in the database
        List<VibrationEcoMessage> vibrationEcoMessageList = vibrationEcoMessageRepository.findAll();
        assertThat(vibrationEcoMessageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVibrationEcoMessage() throws Exception {
        // Initialize the database
        vibrationEcoMessageRepository.saveAndFlush(vibrationEcoMessage);
        int databaseSizeBeforeDelete = vibrationEcoMessageRepository.findAll().size();

        // Get the vibrationEcoMessage
        restVibrationEcoMessageMockMvc.perform(delete("/api/vibration-eco-messages/{id}", vibrationEcoMessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VibrationEcoMessage> vibrationEcoMessageList = vibrationEcoMessageRepository.findAll();
        assertThat(vibrationEcoMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VibrationEcoMessage.class);
        VibrationEcoMessage vibrationEcoMessage1 = new VibrationEcoMessage();
        vibrationEcoMessage1.setId(1L);
        VibrationEcoMessage vibrationEcoMessage2 = new VibrationEcoMessage();
        vibrationEcoMessage2.setId(vibrationEcoMessage1.getId());
        assertThat(vibrationEcoMessage1).isEqualTo(vibrationEcoMessage2);
        vibrationEcoMessage2.setId(2L);
        assertThat(vibrationEcoMessage1).isNotEqualTo(vibrationEcoMessage2);
        vibrationEcoMessage1.setId(null);
        assertThat(vibrationEcoMessage1).isNotEqualTo(vibrationEcoMessage2);
    }
}
