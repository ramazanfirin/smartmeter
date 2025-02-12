package com.masterteknoloji.net.web.rest;

import com.masterteknoloji.net.SmartmeterApp;

import com.masterteknoloji.net.domain.M2mMessage;
import com.masterteknoloji.net.repository.M2mMessageRepository;
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

/**
 * Test class for the M2mMessageResource REST controller.
 *
 * @see M2mMessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartmeterApp.class)
public class M2mMessageResourceIntTest {

    private static final String DEFAULT_BASE_64_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_BASE_64_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_HEX_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_HEX_MESSAGE = "BBBBBBBBBB";

    private static final Long DEFAULT_INDEX = 1L;
    private static final Long UPDATED_INDEX = 2L;

    private static final Long DEFAULT_TOTAL_MESSAGE_COUNT = 1L;
    private static final Long UPDATED_TOTAL_MESSAGE_COUNT = 2L;

    private static final ZonedDateTime DEFAULT_INSERT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_INSERT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Float DEFAULT_BATTERY_VALUE = 1F;
    private static final Float UPDATED_BATTERY_VALUE = 2F;

    private static final Float DEFAULT_SENSOR_VALUE = 1F;
    private static final Float UPDATED_SENSOR_VALUE = 2F;

    private static final Long DEFAULT_PORT = 1L;
    private static final Long UPDATED_PORT = 2L;

    private static final Boolean DEFAULT_IMAGE_DATA = false;
    private static final Boolean UPDATED_IMAGE_DATA = true;

    @Autowired
    private M2mMessageRepository m2mMessageRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restM2mMessageMockMvc;

    private M2mMessage m2mMessage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final M2mMessageResource m2mMessageResource = new M2mMessageResource(m2mMessageRepository);
        this.restM2mMessageMockMvc = MockMvcBuilders.standaloneSetup(m2mMessageResource)
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
    public static M2mMessage createEntity(EntityManager em) {
        M2mMessage m2mMessage = new M2mMessage()
            .base64Message(DEFAULT_BASE_64_MESSAGE)
            .hexMessage(DEFAULT_HEX_MESSAGE)
            .index(DEFAULT_INDEX)
            .totalMessageCount(DEFAULT_TOTAL_MESSAGE_COUNT)
            .insertDate(DEFAULT_INSERT_DATE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .batteryValue(DEFAULT_BATTERY_VALUE)
            .sensorValue(DEFAULT_SENSOR_VALUE)
            .port(DEFAULT_PORT)
            .imageData(DEFAULT_IMAGE_DATA);
        return m2mMessage;
    }

    @Before
    public void initTest() {
        m2mMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createM2mMessage() throws Exception {
        int databaseSizeBeforeCreate = m2mMessageRepository.findAll().size();

        // Create the M2mMessage
        restM2mMessageMockMvc.perform(post("/api/m-2-m-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(m2mMessage)))
            .andExpect(status().isCreated());

        // Validate the M2mMessage in the database
        List<M2mMessage> m2mMessageList = m2mMessageRepository.findAll();
        assertThat(m2mMessageList).hasSize(databaseSizeBeforeCreate + 1);
        M2mMessage testM2mMessage = m2mMessageList.get(m2mMessageList.size() - 1);
        assertThat(testM2mMessage.getBase64Message()).isEqualTo(DEFAULT_BASE_64_MESSAGE);
        assertThat(testM2mMessage.getHexMessage()).isEqualTo(DEFAULT_HEX_MESSAGE);
        assertThat(testM2mMessage.getIndex()).isEqualTo(DEFAULT_INDEX);
        assertThat(testM2mMessage.getTotalMessageCount()).isEqualTo(DEFAULT_TOTAL_MESSAGE_COUNT);
        assertThat(testM2mMessage.getInsertDate()).isEqualTo(DEFAULT_INSERT_DATE);
        assertThat(testM2mMessage.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testM2mMessage.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testM2mMessage.getBatteryValue()).isEqualTo(DEFAULT_BATTERY_VALUE);
        assertThat(testM2mMessage.getSensorValue()).isEqualTo(DEFAULT_SENSOR_VALUE);
        assertThat(testM2mMessage.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testM2mMessage.isImageData()).isEqualTo(DEFAULT_IMAGE_DATA);
    }

    @Test
    @Transactional
    public void createM2mMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = m2mMessageRepository.findAll().size();

        // Create the M2mMessage with an existing ID
        m2mMessage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restM2mMessageMockMvc.perform(post("/api/m-2-m-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(m2mMessage)))
            .andExpect(status().isBadRequest());

        // Validate the M2mMessage in the database
        List<M2mMessage> m2mMessageList = m2mMessageRepository.findAll();
        assertThat(m2mMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllM2mMessages() throws Exception {
        // Initialize the database
        m2mMessageRepository.saveAndFlush(m2mMessage);

        // Get all the m2mMessageList
        restM2mMessageMockMvc.perform(get("/api/m-2-m-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(m2mMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].base64Message").value(hasItem(DEFAULT_BASE_64_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].hexMessage").value(hasItem(DEFAULT_HEX_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].totalMessageCount").value(hasItem(DEFAULT_TOTAL_MESSAGE_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].insertDate").value(hasItem(sameInstant(DEFAULT_INSERT_DATE))))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].batteryValue").value(hasItem(DEFAULT_BATTERY_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].sensorValue").value(hasItem(DEFAULT_SENSOR_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT.intValue())))
            .andExpect(jsonPath("$.[*].imageData").value(hasItem(DEFAULT_IMAGE_DATA.booleanValue())));
    }

    @Test
    @Transactional
    public void getM2mMessage() throws Exception {
        // Initialize the database
        m2mMessageRepository.saveAndFlush(m2mMessage);

        // Get the m2mMessage
        restM2mMessageMockMvc.perform(get("/api/m-2-m-messages/{id}", m2mMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(m2mMessage.getId().intValue()))
            .andExpect(jsonPath("$.base64Message").value(DEFAULT_BASE_64_MESSAGE.toString()))
            .andExpect(jsonPath("$.hexMessage").value(DEFAULT_HEX_MESSAGE.toString()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX.intValue()))
            .andExpect(jsonPath("$.totalMessageCount").value(DEFAULT_TOTAL_MESSAGE_COUNT.intValue()))
            .andExpect(jsonPath("$.insertDate").value(sameInstant(DEFAULT_INSERT_DATE)))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.batteryValue").value(DEFAULT_BATTERY_VALUE.doubleValue()))
            .andExpect(jsonPath("$.sensorValue").value(DEFAULT_SENSOR_VALUE.doubleValue()))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT.intValue()))
            .andExpect(jsonPath("$.imageData").value(DEFAULT_IMAGE_DATA.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingM2mMessage() throws Exception {
        // Get the m2mMessage
        restM2mMessageMockMvc.perform(get("/api/m-2-m-messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateM2mMessage() throws Exception {
        // Initialize the database
        m2mMessageRepository.saveAndFlush(m2mMessage);
        int databaseSizeBeforeUpdate = m2mMessageRepository.findAll().size();

        // Update the m2mMessage
        M2mMessage updatedM2mMessage = m2mMessageRepository.findOne(m2mMessage.getId());
        // Disconnect from session so that the updates on updatedM2mMessage are not directly saved in db
        em.detach(updatedM2mMessage);
        updatedM2mMessage
            .base64Message(UPDATED_BASE_64_MESSAGE)
            .hexMessage(UPDATED_HEX_MESSAGE)
            .index(UPDATED_INDEX)
            .totalMessageCount(UPDATED_TOTAL_MESSAGE_COUNT)
            .insertDate(UPDATED_INSERT_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .batteryValue(UPDATED_BATTERY_VALUE)
            .sensorValue(UPDATED_SENSOR_VALUE)
            .port(UPDATED_PORT)
            .imageData(UPDATED_IMAGE_DATA);

        restM2mMessageMockMvc.perform(put("/api/m-2-m-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedM2mMessage)))
            .andExpect(status().isOk());

        // Validate the M2mMessage in the database
        List<M2mMessage> m2mMessageList = m2mMessageRepository.findAll();
        assertThat(m2mMessageList).hasSize(databaseSizeBeforeUpdate);
        M2mMessage testM2mMessage = m2mMessageList.get(m2mMessageList.size() - 1);
        assertThat(testM2mMessage.getBase64Message()).isEqualTo(UPDATED_BASE_64_MESSAGE);
        assertThat(testM2mMessage.getHexMessage()).isEqualTo(UPDATED_HEX_MESSAGE);
        assertThat(testM2mMessage.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testM2mMessage.getTotalMessageCount()).isEqualTo(UPDATED_TOTAL_MESSAGE_COUNT);
        assertThat(testM2mMessage.getInsertDate()).isEqualTo(UPDATED_INSERT_DATE);
        assertThat(testM2mMessage.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testM2mMessage.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testM2mMessage.getBatteryValue()).isEqualTo(UPDATED_BATTERY_VALUE);
        assertThat(testM2mMessage.getSensorValue()).isEqualTo(UPDATED_SENSOR_VALUE);
        assertThat(testM2mMessage.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testM2mMessage.isImageData()).isEqualTo(UPDATED_IMAGE_DATA);
    }

    @Test
    @Transactional
    public void updateNonExistingM2mMessage() throws Exception {
        int databaseSizeBeforeUpdate = m2mMessageRepository.findAll().size();

        // Create the M2mMessage

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restM2mMessageMockMvc.perform(put("/api/m-2-m-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(m2mMessage)))
            .andExpect(status().isCreated());

        // Validate the M2mMessage in the database
        List<M2mMessage> m2mMessageList = m2mMessageRepository.findAll();
        assertThat(m2mMessageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteM2mMessage() throws Exception {
        // Initialize the database
        m2mMessageRepository.saveAndFlush(m2mMessage);
        int databaseSizeBeforeDelete = m2mMessageRepository.findAll().size();

        // Get the m2mMessage
        restM2mMessageMockMvc.perform(delete("/api/m-2-m-messages/{id}", m2mMessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<M2mMessage> m2mMessageList = m2mMessageRepository.findAll();
        assertThat(m2mMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(M2mMessage.class);
        M2mMessage m2mMessage1 = new M2mMessage();
        m2mMessage1.setId(1L);
        M2mMessage m2mMessage2 = new M2mMessage();
        m2mMessage2.setId(m2mMessage1.getId());
        assertThat(m2mMessage1).isEqualTo(m2mMessage2);
        m2mMessage2.setId(2L);
        assertThat(m2mMessage1).isNotEqualTo(m2mMessage2);
        m2mMessage1.setId(null);
        assertThat(m2mMessage1).isNotEqualTo(m2mMessage2);
    }
}
