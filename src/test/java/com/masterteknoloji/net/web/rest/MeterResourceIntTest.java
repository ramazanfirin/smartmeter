package com.masterteknoloji.net.web.rest;

import com.masterteknoloji.net.SmartmeterApp;

import com.masterteknoloji.net.domain.Meter;
import com.masterteknoloji.net.repository.MeterRepository;
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
 * Test class for the MeterResource REST controller.
 *
 * @see MeterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SmartmeterApp.class)
public class MeterResourceIntTest {

    private static final String DEFAULT_METER_NO = "AAAAAAAAAA";
    private static final String UPDATED_METER_NO = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_NO = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NO = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    @Autowired
    private MeterRepository meterRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMeterMockMvc;

    private Meter meter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MeterResource meterResource = new MeterResource(meterRepository);
        this.restMeterMockMvc = MockMvcBuilders.standaloneSetup(meterResource)
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
    public static Meter createEntity(EntityManager em) {
        Meter meter = new Meter()
            .meterNo(DEFAULT_METER_NO)
            .customerNo(DEFAULT_CUSTOMER_NO)
            .address(DEFAULT_ADDRESS);
        return meter;
    }

    @Before
    public void initTest() {
        meter = createEntity(em);
    }

    @Test
    @Transactional
    public void createMeter() throws Exception {
        int databaseSizeBeforeCreate = meterRepository.findAll().size();

        // Create the Meter
        restMeterMockMvc.perform(post("/api/meters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meter)))
            .andExpect(status().isCreated());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeCreate + 1);
        Meter testMeter = meterList.get(meterList.size() - 1);
        assertThat(testMeter.getMeterNo()).isEqualTo(DEFAULT_METER_NO);
        assertThat(testMeter.getCustomerNo()).isEqualTo(DEFAULT_CUSTOMER_NO);
        assertThat(testMeter.getAddress()).isEqualTo(DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    public void createMeterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = meterRepository.findAll().size();

        // Create the Meter with an existing ID
        meter.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMeterMockMvc.perform(post("/api/meters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meter)))
            .andExpect(status().isBadRequest());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMeters() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get all the meterList
        restMeterMockMvc.perform(get("/api/meters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(meter.getId().intValue())))
            .andExpect(jsonPath("$.[*].meterNo").value(hasItem(DEFAULT_METER_NO.toString())))
            .andExpect(jsonPath("$.[*].customerNo").value(hasItem(DEFAULT_CUSTOMER_NO.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())));
    }

    @Test
    @Transactional
    public void getMeter() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);

        // Get the meter
        restMeterMockMvc.perform(get("/api/meters/{id}", meter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(meter.getId().intValue()))
            .andExpect(jsonPath("$.meterNo").value(DEFAULT_METER_NO.toString()))
            .andExpect(jsonPath("$.customerNo").value(DEFAULT_CUSTOMER_NO.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMeter() throws Exception {
        // Get the meter
        restMeterMockMvc.perform(get("/api/meters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMeter() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);
        int databaseSizeBeforeUpdate = meterRepository.findAll().size();

        // Update the meter
        Meter updatedMeter = meterRepository.findOne(meter.getId());
        // Disconnect from session so that the updates on updatedMeter are not directly saved in db
        em.detach(updatedMeter);
        updatedMeter
            .meterNo(UPDATED_METER_NO)
            .customerNo(UPDATED_CUSTOMER_NO)
            .address(UPDATED_ADDRESS);

        restMeterMockMvc.perform(put("/api/meters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMeter)))
            .andExpect(status().isOk());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeUpdate);
        Meter testMeter = meterList.get(meterList.size() - 1);
        assertThat(testMeter.getMeterNo()).isEqualTo(UPDATED_METER_NO);
        assertThat(testMeter.getCustomerNo()).isEqualTo(UPDATED_CUSTOMER_NO);
        assertThat(testMeter.getAddress()).isEqualTo(UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void updateNonExistingMeter() throws Exception {
        int databaseSizeBeforeUpdate = meterRepository.findAll().size();

        // Create the Meter

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMeterMockMvc.perform(put("/api/meters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(meter)))
            .andExpect(status().isCreated());

        // Validate the Meter in the database
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMeter() throws Exception {
        // Initialize the database
        meterRepository.saveAndFlush(meter);
        int databaseSizeBeforeDelete = meterRepository.findAll().size();

        // Get the meter
        restMeterMockMvc.perform(delete("/api/meters/{id}", meter.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Meter> meterList = meterRepository.findAll();
        assertThat(meterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Meter.class);
        Meter meter1 = new Meter();
        meter1.setId(1L);
        Meter meter2 = new Meter();
        meter2.setId(meter1.getId());
        assertThat(meter1).isEqualTo(meter2);
        meter2.setId(2L);
        assertThat(meter1).isNotEqualTo(meter2);
        meter1.setId(null);
        assertThat(meter1).isNotEqualTo(meter2);
    }
}
