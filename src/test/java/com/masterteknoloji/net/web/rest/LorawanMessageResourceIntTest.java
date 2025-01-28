package com.masterteknoloji.net.web.rest;

import com.masterteknoloji.net.SmartmeterApp;

import com.masterteknoloji.net.domain.LorawanMessage;
import com.masterteknoloji.net.repository.LorawanMessageRepository;
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
 * Test class for the LorawanMessageResource REST controller.
 *
 * @see LorawanMessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartmeterApp.class)
public class LorawanMessageResourceIntTest {

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

    private static final String DEFAULT_F_PORT = "AAAAAAAAAA";
    private static final String UPDATED_F_PORT = "BBBBBBBBBB";

    private static final Long DEFAULT_F_CNT = 1L;
    private static final Long UPDATED_F_CNT = 2L;

    private static final String DEFAULT_IMAGE_ID = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_ID = "BBBBBBBBBB";

    @Autowired
    private LorawanMessageRepository lorawanMessageRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLorawanMessageMockMvc;

    private LorawanMessage lorawanMessage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LorawanMessageResource lorawanMessageResource = new LorawanMessageResource(lorawanMessageRepository);
        this.restLorawanMessageMockMvc = MockMvcBuilders.standaloneSetup(lorawanMessageResource)
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
    public static LorawanMessage createEntity(EntityManager em) {
        LorawanMessage lorawanMessage = new LorawanMessage()
            .base64Message(DEFAULT_BASE_64_MESSAGE)
            .hexMessage(DEFAULT_HEX_MESSAGE)
            .index(DEFAULT_INDEX)
            .totalMessageCount(DEFAULT_TOTAL_MESSAGE_COUNT)
            .insertDate(DEFAULT_INSERT_DATE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .batteryValue(DEFAULT_BATTERY_VALUE)
            .sensorValue(DEFAULT_SENSOR_VALUE)
            .fPort(DEFAULT_F_PORT)
            .fCnt(DEFAULT_F_CNT)
            .imageId(DEFAULT_IMAGE_ID);
        return lorawanMessage;
    }

    @Before
    public void initTest() {
        lorawanMessage = createEntity(em);
    }

    @Test
    @Transactional
    public void createLorawanMessage() throws Exception {
        int databaseSizeBeforeCreate = lorawanMessageRepository.findAll().size();

        // Create the LorawanMessage
        restLorawanMessageMockMvc.perform(post("/api/lorawan-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lorawanMessage)))
            .andExpect(status().isCreated());

        // Validate the LorawanMessage in the database
        List<LorawanMessage> lorawanMessageList = lorawanMessageRepository.findAll();
        assertThat(lorawanMessageList).hasSize(databaseSizeBeforeCreate + 1);
        LorawanMessage testLorawanMessage = lorawanMessageList.get(lorawanMessageList.size() - 1);
        assertThat(testLorawanMessage.getBase64Message()).isEqualTo(DEFAULT_BASE_64_MESSAGE);
        assertThat(testLorawanMessage.getHexMessage()).isEqualTo(DEFAULT_HEX_MESSAGE);
        assertThat(testLorawanMessage.getIndex()).isEqualTo(DEFAULT_INDEX);
        assertThat(testLorawanMessage.getTotalMessageCount()).isEqualTo(DEFAULT_TOTAL_MESSAGE_COUNT);
        assertThat(testLorawanMessage.getInsertDate()).isEqualTo(DEFAULT_INSERT_DATE);
        assertThat(testLorawanMessage.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testLorawanMessage.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testLorawanMessage.getBatteryValue()).isEqualTo(DEFAULT_BATTERY_VALUE);
        assertThat(testLorawanMessage.getSensorValue()).isEqualTo(DEFAULT_SENSOR_VALUE);
        assertThat(testLorawanMessage.getfPort()).isEqualTo(DEFAULT_F_PORT);
        assertThat(testLorawanMessage.getfCnt()).isEqualTo(DEFAULT_F_CNT);
        assertThat(testLorawanMessage.getImageId()).isEqualTo(DEFAULT_IMAGE_ID);
    }

    @Test
    @Transactional
    public void createLorawanMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lorawanMessageRepository.findAll().size();

        // Create the LorawanMessage with an existing ID
        lorawanMessage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLorawanMessageMockMvc.perform(post("/api/lorawan-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lorawanMessage)))
            .andExpect(status().isBadRequest());

        // Validate the LorawanMessage in the database
        List<LorawanMessage> lorawanMessageList = lorawanMessageRepository.findAll();
        assertThat(lorawanMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLorawanMessages() throws Exception {
        // Initialize the database
        lorawanMessageRepository.saveAndFlush(lorawanMessage);

        // Get all the lorawanMessageList
        restLorawanMessageMockMvc.perform(get("/api/lorawan-messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lorawanMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].base64Message").value(hasItem(DEFAULT_BASE_64_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].hexMessage").value(hasItem(DEFAULT_HEX_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX.intValue())))
            .andExpect(jsonPath("$.[*].totalMessageCount").value(hasItem(DEFAULT_TOTAL_MESSAGE_COUNT.intValue())))
            .andExpect(jsonPath("$.[*].insertDate").value(hasItem(sameInstant(DEFAULT_INSERT_DATE))))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].batteryValue").value(hasItem(DEFAULT_BATTERY_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].sensorValue").value(hasItem(DEFAULT_SENSOR_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].fPort").value(hasItem(DEFAULT_F_PORT.toString())))
            .andExpect(jsonPath("$.[*].fCnt").value(hasItem(DEFAULT_F_CNT.intValue())))
            .andExpect(jsonPath("$.[*].imageId").value(hasItem(DEFAULT_IMAGE_ID.toString())));
    }

    @Test
    @Transactional
    public void getLorawanMessage() throws Exception {
        // Initialize the database
        lorawanMessageRepository.saveAndFlush(lorawanMessage);

        // Get the lorawanMessage
        restLorawanMessageMockMvc.perform(get("/api/lorawan-messages/{id}", lorawanMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lorawanMessage.getId().intValue()))
            .andExpect(jsonPath("$.base64Message").value(DEFAULT_BASE_64_MESSAGE.toString()))
            .andExpect(jsonPath("$.hexMessage").value(DEFAULT_HEX_MESSAGE.toString()))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX.intValue()))
            .andExpect(jsonPath("$.totalMessageCount").value(DEFAULT_TOTAL_MESSAGE_COUNT.intValue()))
            .andExpect(jsonPath("$.insertDate").value(sameInstant(DEFAULT_INSERT_DATE)))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.batteryValue").value(DEFAULT_BATTERY_VALUE.doubleValue()))
            .andExpect(jsonPath("$.sensorValue").value(DEFAULT_SENSOR_VALUE.doubleValue()))
            .andExpect(jsonPath("$.fPort").value(DEFAULT_F_PORT.toString()))
            .andExpect(jsonPath("$.fCnt").value(DEFAULT_F_CNT.intValue()))
            .andExpect(jsonPath("$.imageId").value(DEFAULT_IMAGE_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLorawanMessage() throws Exception {
        // Get the lorawanMessage
        restLorawanMessageMockMvc.perform(get("/api/lorawan-messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLorawanMessage() throws Exception {
        // Initialize the database
        lorawanMessageRepository.saveAndFlush(lorawanMessage);
        int databaseSizeBeforeUpdate = lorawanMessageRepository.findAll().size();

        // Update the lorawanMessage
        LorawanMessage updatedLorawanMessage = lorawanMessageRepository.findOne(lorawanMessage.getId());
        // Disconnect from session so that the updates on updatedLorawanMessage are not directly saved in db
        em.detach(updatedLorawanMessage);
        updatedLorawanMessage
            .base64Message(UPDATED_BASE_64_MESSAGE)
            .hexMessage(UPDATED_HEX_MESSAGE)
            .index(UPDATED_INDEX)
            .totalMessageCount(UPDATED_TOTAL_MESSAGE_COUNT)
            .insertDate(UPDATED_INSERT_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .batteryValue(UPDATED_BATTERY_VALUE)
            .sensorValue(UPDATED_SENSOR_VALUE)
            .fPort(UPDATED_F_PORT)
            .fCnt(UPDATED_F_CNT)
            .imageId(UPDATED_IMAGE_ID);

        restLorawanMessageMockMvc.perform(put("/api/lorawan-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLorawanMessage)))
            .andExpect(status().isOk());

        // Validate the LorawanMessage in the database
        List<LorawanMessage> lorawanMessageList = lorawanMessageRepository.findAll();
        assertThat(lorawanMessageList).hasSize(databaseSizeBeforeUpdate);
        LorawanMessage testLorawanMessage = lorawanMessageList.get(lorawanMessageList.size() - 1);
        assertThat(testLorawanMessage.getBase64Message()).isEqualTo(UPDATED_BASE_64_MESSAGE);
        assertThat(testLorawanMessage.getHexMessage()).isEqualTo(UPDATED_HEX_MESSAGE);
        assertThat(testLorawanMessage.getIndex()).isEqualTo(UPDATED_INDEX);
        assertThat(testLorawanMessage.getTotalMessageCount()).isEqualTo(UPDATED_TOTAL_MESSAGE_COUNT);
        assertThat(testLorawanMessage.getInsertDate()).isEqualTo(UPDATED_INSERT_DATE);
        assertThat(testLorawanMessage.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testLorawanMessage.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testLorawanMessage.getBatteryValue()).isEqualTo(UPDATED_BATTERY_VALUE);
        assertThat(testLorawanMessage.getSensorValue()).isEqualTo(UPDATED_SENSOR_VALUE);
        assertThat(testLorawanMessage.getfPort()).isEqualTo(UPDATED_F_PORT);
        assertThat(testLorawanMessage.getfCnt()).isEqualTo(UPDATED_F_CNT);
        assertThat(testLorawanMessage.getImageId()).isEqualTo(UPDATED_IMAGE_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingLorawanMessage() throws Exception {
        int databaseSizeBeforeUpdate = lorawanMessageRepository.findAll().size();

        // Create the LorawanMessage

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLorawanMessageMockMvc.perform(put("/api/lorawan-messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lorawanMessage)))
            .andExpect(status().isCreated());

        // Validate the LorawanMessage in the database
        List<LorawanMessage> lorawanMessageList = lorawanMessageRepository.findAll();
        assertThat(lorawanMessageList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLorawanMessage() throws Exception {
        // Initialize the database
        lorawanMessageRepository.saveAndFlush(lorawanMessage);
        int databaseSizeBeforeDelete = lorawanMessageRepository.findAll().size();

        // Get the lorawanMessage
        restLorawanMessageMockMvc.perform(delete("/api/lorawan-messages/{id}", lorawanMessage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LorawanMessage> lorawanMessageList = lorawanMessageRepository.findAll();
        assertThat(lorawanMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LorawanMessage.class);
        LorawanMessage lorawanMessage1 = new LorawanMessage();
        lorawanMessage1.setId(1L);
        LorawanMessage lorawanMessage2 = new LorawanMessage();
        lorawanMessage2.setId(lorawanMessage1.getId());
        assertThat(lorawanMessage1).isEqualTo(lorawanMessage2);
        lorawanMessage2.setId(2L);
        assertThat(lorawanMessage1).isNotEqualTo(lorawanMessage2);
        lorawanMessage1.setId(null);
        assertThat(lorawanMessage1).isNotEqualTo(lorawanMessage2);
    }
}
