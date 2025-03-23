package com.masterteknoloji.net.web.rest;

import com.masterteknoloji.net.SmartmeterApp;

import com.masterteknoloji.net.domain.Sensor;
import com.masterteknoloji.net.repository.SensorRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.masterteknoloji.net.web.rest.TestUtil.sameInstant;
import static com.masterteknoloji.net.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.masterteknoloji.net.domain.enumeration.ConnectionType;
import com.masterteknoloji.net.domain.enumeration.Type;
/**
 * Test class for the SensorResource REST controller.
 *
 * @see SensorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartmeterApp.class)
public class SensorResourceIntTest {

    private static final String DEFAULT_DEV_EUI = "AAAAAAAAAA";
    private static final String UPDATED_DEV_EUI = "BBBBBBBBBB";

    private static final String DEFAULT_APP_EUI = "AAAAAAAAAA";
    private static final String UPDATED_APP_EUI = "BBBBBBBBBB";

    private static final String DEFAULT_APP_KEY = "AAAAAAAAAA";
    private static final String UPDATED_APP_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_IMEI = "AAAAAAAAAA";
    private static final String UPDATED_IMEI = "BBBBBBBBBB";

    private static final ConnectionType DEFAULT_CONNECTION_TYPE = ConnectionType.LORA;
    private static final ConnectionType UPDATED_CONNECTION_TYPE = ConnectionType.M2M;

    private static final Type DEFAULT_TYPE = Type.VIBRATION;
    private static final Type UPDATED_TYPE = Type.BUTTON;

    private static final String DEFAULT_THINGS_BOARD_DEVICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_THINGS_BOARD_DEVICE_ID = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LAST_SEEN_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_SEEN_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LAST_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MESSAGE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_LAST_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_LAST_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_LAST_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_LAST_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSensorMockMvc;

    private Sensor sensor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SensorResource sensorResource = new SensorResource(sensorRepository);
        this.restSensorMockMvc = MockMvcBuilders.standaloneSetup(sensorResource)
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
    public static Sensor createEntity(EntityManager em) {
        Sensor sensor = new Sensor()
            .devEui(DEFAULT_DEV_EUI)
            .appEui(DEFAULT_APP_EUI)
            .appKey(DEFAULT_APP_KEY)
            .imei(DEFAULT_IMEI)
            .connectionType(DEFAULT_CONNECTION_TYPE)
            .type(DEFAULT_TYPE)
            .thingsBoardDeviceId(DEFAULT_THINGS_BOARD_DEVICE_ID)
            .lastSeenDate(DEFAULT_LAST_SEEN_DATE)
            .lastMessage(DEFAULT_LAST_MESSAGE)
            .lastImage(DEFAULT_LAST_IMAGE)
            .lastImageContentType(DEFAULT_LAST_IMAGE_CONTENT_TYPE);
        return sensor;
    }

    @Before
    public void initTest() {
        sensor = createEntity(em);
    }

    @Test
    @Transactional
    public void createSensor() throws Exception {
        int databaseSizeBeforeCreate = sensorRepository.findAll().size();

        // Create the Sensor
        restSensorMockMvc.perform(post("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isCreated());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate + 1);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getDevEui()).isEqualTo(DEFAULT_DEV_EUI);
        assertThat(testSensor.getAppEui()).isEqualTo(DEFAULT_APP_EUI);
        assertThat(testSensor.getAppKey()).isEqualTo(DEFAULT_APP_KEY);
        assertThat(testSensor.getImei()).isEqualTo(DEFAULT_IMEI);
        assertThat(testSensor.getConnectionType()).isEqualTo(DEFAULT_CONNECTION_TYPE);
        assertThat(testSensor.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSensor.getThingsBoardDeviceId()).isEqualTo(DEFAULT_THINGS_BOARD_DEVICE_ID);
        assertThat(testSensor.getLastSeenDate()).isEqualTo(DEFAULT_LAST_SEEN_DATE);
        assertThat(testSensor.getLastMessage()).isEqualTo(DEFAULT_LAST_MESSAGE);
        assertThat(testSensor.getLastImage()).isEqualTo(DEFAULT_LAST_IMAGE);
        assertThat(testSensor.getLastImageContentType()).isEqualTo(DEFAULT_LAST_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createSensorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sensorRepository.findAll().size();

        // Create the Sensor with an existing ID
        sensor.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensorMockMvc.perform(post("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isBadRequest());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDevEuiIsRequired() throws Exception {
        int databaseSizeBeforeTest = sensorRepository.findAll().size();
        // set the field null
        sensor.setDevEui(null);

        // Create the Sensor, which fails.

        restSensorMockMvc.perform(post("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isBadRequest());

        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSensors() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensorList
        restSensorMockMvc.perform(get("/api/sensors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensor.getId().intValue())))
            .andExpect(jsonPath("$.[*].devEui").value(hasItem(DEFAULT_DEV_EUI.toString())))
            .andExpect(jsonPath("$.[*].appEui").value(hasItem(DEFAULT_APP_EUI.toString())))
            .andExpect(jsonPath("$.[*].appKey").value(hasItem(DEFAULT_APP_KEY.toString())))
            .andExpect(jsonPath("$.[*].imei").value(hasItem(DEFAULT_IMEI.toString())))
            .andExpect(jsonPath("$.[*].connectionType").value(hasItem(DEFAULT_CONNECTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].thingsBoardDeviceId").value(hasItem(DEFAULT_THINGS_BOARD_DEVICE_ID.toString())))
            .andExpect(jsonPath("$.[*].lastSeenDate").value(hasItem(sameInstant(DEFAULT_LAST_SEEN_DATE))))
            .andExpect(jsonPath("$.[*].lastMessage").value(hasItem(DEFAULT_LAST_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].lastImageContentType").value(hasItem(DEFAULT_LAST_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].lastImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_LAST_IMAGE))));
    }

    @Test
    @Transactional
    public void getSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get the sensor
        restSensorMockMvc.perform(get("/api/sensors/{id}", sensor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(sensor.getId().intValue()))
            .andExpect(jsonPath("$.devEui").value(DEFAULT_DEV_EUI.toString()))
            .andExpect(jsonPath("$.appEui").value(DEFAULT_APP_EUI.toString()))
            .andExpect(jsonPath("$.appKey").value(DEFAULT_APP_KEY.toString()))
            .andExpect(jsonPath("$.imei").value(DEFAULT_IMEI.toString()))
            .andExpect(jsonPath("$.connectionType").value(DEFAULT_CONNECTION_TYPE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.thingsBoardDeviceId").value(DEFAULT_THINGS_BOARD_DEVICE_ID.toString()))
            .andExpect(jsonPath("$.lastSeenDate").value(sameInstant(DEFAULT_LAST_SEEN_DATE)))
            .andExpect(jsonPath("$.lastMessage").value(DEFAULT_LAST_MESSAGE.toString()))
            .andExpect(jsonPath("$.lastImageContentType").value(DEFAULT_LAST_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.lastImage").value(Base64Utils.encodeToString(DEFAULT_LAST_IMAGE)));
    }

    @Test
    @Transactional
    public void getNonExistingSensor() throws Exception {
        // Get the sensor
        restSensorMockMvc.perform(get("/api/sensors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Update the sensor
        Sensor updatedSensor = sensorRepository.findOne(sensor.getId());
        // Disconnect from session so that the updates on updatedSensor are not directly saved in db
        em.detach(updatedSensor);
        updatedSensor
            .devEui(UPDATED_DEV_EUI)
            .appEui(UPDATED_APP_EUI)
            .appKey(UPDATED_APP_KEY)
            .imei(UPDATED_IMEI)
            .connectionType(UPDATED_CONNECTION_TYPE)
            .type(UPDATED_TYPE)
            .thingsBoardDeviceId(UPDATED_THINGS_BOARD_DEVICE_ID)
            .lastSeenDate(UPDATED_LAST_SEEN_DATE)
            .lastMessage(UPDATED_LAST_MESSAGE)
            .lastImage(UPDATED_LAST_IMAGE)
            .lastImageContentType(UPDATED_LAST_IMAGE_CONTENT_TYPE);

        restSensorMockMvc.perform(put("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSensor)))
            .andExpect(status().isOk());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensorList.get(sensorList.size() - 1);
        assertThat(testSensor.getDevEui()).isEqualTo(UPDATED_DEV_EUI);
        assertThat(testSensor.getAppEui()).isEqualTo(UPDATED_APP_EUI);
        assertThat(testSensor.getAppKey()).isEqualTo(UPDATED_APP_KEY);
        assertThat(testSensor.getImei()).isEqualTo(UPDATED_IMEI);
        assertThat(testSensor.getConnectionType()).isEqualTo(UPDATED_CONNECTION_TYPE);
        assertThat(testSensor.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSensor.getThingsBoardDeviceId()).isEqualTo(UPDATED_THINGS_BOARD_DEVICE_ID);
        assertThat(testSensor.getLastSeenDate()).isEqualTo(UPDATED_LAST_SEEN_DATE);
        assertThat(testSensor.getLastMessage()).isEqualTo(UPDATED_LAST_MESSAGE);
        assertThat(testSensor.getLastImage()).isEqualTo(UPDATED_LAST_IMAGE);
        assertThat(testSensor.getLastImageContentType()).isEqualTo(UPDATED_LAST_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingSensor() throws Exception {
        int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Create the Sensor

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSensorMockMvc.perform(put("/api/sensors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(sensor)))
            .andExpect(status().isCreated());

        // Validate the Sensor in the database
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);
        int databaseSizeBeforeDelete = sensorRepository.findAll().size();

        // Get the sensor
        restSensorMockMvc.perform(delete("/api/sensors/{id}", sensor.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Sensor> sensorList = sensorRepository.findAll();
        assertThat(sensorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sensor.class);
        Sensor sensor1 = new Sensor();
        sensor1.setId(1L);
        Sensor sensor2 = new Sensor();
        sensor2.setId(sensor1.getId());
        assertThat(sensor1).isEqualTo(sensor2);
        sensor2.setId(2L);
        assertThat(sensor1).isNotEqualTo(sensor2);
        sensor1.setId(null);
        assertThat(sensor1).isNotEqualTo(sensor2);
    }
}
