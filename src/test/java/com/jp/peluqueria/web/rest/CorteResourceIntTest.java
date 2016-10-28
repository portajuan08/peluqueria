package com.jp.peluqueria.web.rest;

import com.jp.peluqueria.PeluqueriaApp;

import com.jp.peluqueria.domain.Corte;
import com.jp.peluqueria.domain.Cliente;
import com.jp.peluqueria.repository.CorteRepository;
import com.jp.peluqueria.repository.search.CorteSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jp.peluqueria.domain.enumeration.Tipo_corte;
/**
 * Test class for the CorteResource REST controller.
 *
 * @see CorteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PeluqueriaApp.class)
public class CorteResourceIntTest {

    private static final ZonedDateTime DEFAULT_FECHA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FECHA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FECHA_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_FECHA);

    private static final BigDecimal DEFAULT_PRECIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO = new BigDecimal(2);

    private static final Tipo_corte DEFAULT_TIPO_CORTE = Tipo_corte.Corte;
    private static final Tipo_corte UPDATED_TIPO_CORTE = Tipo_corte.Alisado;

    private static final String DEFAULT_DETALLE = "AAAAA";
    private static final String UPDATED_DETALLE = "BBBBB";

    @Inject
    private CorteRepository corteRepository;

    @Inject
    private CorteSearchRepository corteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCorteMockMvc;

    private Corte corte;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CorteResource corteResource = new CorteResource();
        ReflectionTestUtils.setField(corteResource, "corteSearchRepository", corteSearchRepository);
        ReflectionTestUtils.setField(corteResource, "corteRepository", corteRepository);
        this.restCorteMockMvc = MockMvcBuilders.standaloneSetup(corteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Corte createEntity(EntityManager em) {
        Corte corte = new Corte()
                .fecha(DEFAULT_FECHA)
                .precio(DEFAULT_PRECIO)
                .tipo_corte(DEFAULT_TIPO_CORTE)
                .detalle(DEFAULT_DETALLE);
        // Add required entity
        Cliente cliente = ClienteResourceIntTest.createEntity(em);
        em.persist(cliente);
        em.flush();
        corte.setCliente(cliente);
        return corte;
    }

    @Before
    public void initTest() {
        corteSearchRepository.deleteAll();
        corte = createEntity(em);
    }

    @Test
    @Transactional
    public void createCorte() throws Exception {
        int databaseSizeBeforeCreate = corteRepository.findAll().size();

        // Create the Corte

        restCorteMockMvc.perform(post("/api/cortes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(corte)))
                .andExpect(status().isCreated());

        // Validate the Corte in the database
        List<Corte> cortes = corteRepository.findAll();
        assertThat(cortes).hasSize(databaseSizeBeforeCreate + 1);
        Corte testCorte = cortes.get(cortes.size() - 1);
        assertThat(testCorte.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testCorte.getPrecio()).isEqualTo(DEFAULT_PRECIO);
        assertThat(testCorte.getTipo_corte()).isEqualTo(DEFAULT_TIPO_CORTE);
        assertThat(testCorte.getDetalle()).isEqualTo(DEFAULT_DETALLE);

        // Validate the Corte in ElasticSearch
        Corte corteEs = corteSearchRepository.findOne(testCorte.getId());
        assertThat(corteEs).isEqualToComparingFieldByField(testCorte);
    }

    @Test
    @Transactional
    public void checkFechaIsRequired() throws Exception {
        int databaseSizeBeforeTest = corteRepository.findAll().size();
        // set the field null
        corte.setFecha(null);

        // Create the Corte, which fails.

        restCorteMockMvc.perform(post("/api/cortes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(corte)))
                .andExpect(status().isBadRequest());

        List<Corte> cortes = corteRepository.findAll();
        assertThat(cortes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTipo_corteIsRequired() throws Exception {
        int databaseSizeBeforeTest = corteRepository.findAll().size();
        // set the field null
        corte.setTipo_corte(null);

        // Create the Corte, which fails.

        restCorteMockMvc.perform(post("/api/cortes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(corte)))
                .andExpect(status().isBadRequest());

        List<Corte> cortes = corteRepository.findAll();
        assertThat(cortes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCortes() throws Exception {
        // Initialize the database
        corteRepository.saveAndFlush(corte);

        // Get all the cortes
        restCorteMockMvc.perform(get("/api/cortes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(corte.getId().intValue())))
                .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA_STR)))
                .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.intValue())))
                .andExpect(jsonPath("$.[*].tipo_corte").value(hasItem(DEFAULT_TIPO_CORTE.toString())))
                .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE.toString())));
    }

    @Test
    @Transactional
    public void getCorte() throws Exception {
        // Initialize the database
        corteRepository.saveAndFlush(corte);

        // Get the corte
        restCorteMockMvc.perform(get("/api/cortes/{id}", corte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(corte.getId().intValue()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA_STR))
            .andExpect(jsonPath("$.precio").value(DEFAULT_PRECIO.intValue()))
            .andExpect(jsonPath("$.tipo_corte").value(DEFAULT_TIPO_CORTE.toString()))
            .andExpect(jsonPath("$.detalle").value(DEFAULT_DETALLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCorte() throws Exception {
        // Get the corte
        restCorteMockMvc.perform(get("/api/cortes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCorte() throws Exception {
        // Initialize the database
        corteRepository.saveAndFlush(corte);
        corteSearchRepository.save(corte);
        int databaseSizeBeforeUpdate = corteRepository.findAll().size();

        // Update the corte
        Corte updatedCorte = corteRepository.findOne(corte.getId());
        updatedCorte
                .fecha(UPDATED_FECHA)
                .precio(UPDATED_PRECIO)
                .tipo_corte(UPDATED_TIPO_CORTE)
                .detalle(UPDATED_DETALLE);

        restCorteMockMvc.perform(put("/api/cortes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCorte)))
                .andExpect(status().isOk());

        // Validate the Corte in the database
        List<Corte> cortes = corteRepository.findAll();
        assertThat(cortes).hasSize(databaseSizeBeforeUpdate);
        Corte testCorte = cortes.get(cortes.size() - 1);
        assertThat(testCorte.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testCorte.getPrecio()).isEqualTo(UPDATED_PRECIO);
        assertThat(testCorte.getTipo_corte()).isEqualTo(UPDATED_TIPO_CORTE);
        assertThat(testCorte.getDetalle()).isEqualTo(UPDATED_DETALLE);

        // Validate the Corte in ElasticSearch
        Corte corteEs = corteSearchRepository.findOne(testCorte.getId());
        assertThat(corteEs).isEqualToComparingFieldByField(testCorte);
    }

    @Test
    @Transactional
    public void deleteCorte() throws Exception {
        // Initialize the database
        corteRepository.saveAndFlush(corte);
        corteSearchRepository.save(corte);
        int databaseSizeBeforeDelete = corteRepository.findAll().size();

        // Get the corte
        restCorteMockMvc.perform(delete("/api/cortes/{id}", corte.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean corteExistsInEs = corteSearchRepository.exists(corte.getId());
        assertThat(corteExistsInEs).isFalse();

        // Validate the database is empty
        List<Corte> cortes = corteRepository.findAll();
        assertThat(cortes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCorte() throws Exception {
        // Initialize the database
        corteRepository.saveAndFlush(corte);
        corteSearchRepository.save(corte);

        // Search the corte
        restCorteMockMvc.perform(get("/api/_search/cortes?query=id:" + corte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(corte.getId().intValue())))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA_STR)))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(DEFAULT_PRECIO.intValue())))
            .andExpect(jsonPath("$.[*].tipo_corte").value(hasItem(DEFAULT_TIPO_CORTE.toString())))
            .andExpect(jsonPath("$.[*].detalle").value(hasItem(DEFAULT_DETALLE.toString())));
    }
}
