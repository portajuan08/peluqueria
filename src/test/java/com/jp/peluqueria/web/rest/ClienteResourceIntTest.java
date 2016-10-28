package com.jp.peluqueria.web.rest;

import com.jp.peluqueria.PeluqueriaApp;

import com.jp.peluqueria.domain.Cliente;
import com.jp.peluqueria.repository.ClienteRepository;
import com.jp.peluqueria.repository.search.ClienteSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ClienteResource REST controller.
 *
 * @see ClienteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PeluqueriaApp.class)
public class ClienteResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAA";
    private static final String UPDATED_NOMBRE = "BBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAA";
    private static final String UPDATED_APELLIDO = "BBBBB";

    private static final LocalDate DEFAULT_FECHA_NACIMIENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_NACIMIENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CELULAR = "AAAAA";
    private static final String UPDATED_CELULAR = "BBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAA";
    private static final String UPDATED_DIRECCION = "BBBBB";

    private static final String DEFAULT_OBSERVACION = "AAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBB";

    @Inject
    private ClienteRepository clienteRepository;

    @Inject
    private ClienteSearchRepository clienteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restClienteMockMvc;

    private Cliente cliente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClienteResource clienteResource = new ClienteResource();
        ReflectionTestUtils.setField(clienteResource, "clienteSearchRepository", clienteSearchRepository);
        ReflectionTestUtils.setField(clienteResource, "clienteRepository", clienteRepository);
        this.restClienteMockMvc = MockMvcBuilders.standaloneSetup(clienteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cliente createEntity(EntityManager em) {
        Cliente cliente = new Cliente()
                .nombre(DEFAULT_NOMBRE)
                .apellido(DEFAULT_APELLIDO)
                .fecha_nacimiento(DEFAULT_FECHA_NACIMIENTO)
                .celular(DEFAULT_CELULAR)
                .direccion(DEFAULT_DIRECCION)
                .observacion(DEFAULT_OBSERVACION);
        return cliente;
    }

    @Before
    public void initTest() {
        clienteSearchRepository.deleteAll();
        cliente = createEntity(em);
    }

    @Test
    @Transactional
    public void createCliente() throws Exception {
        int databaseSizeBeforeCreate = clienteRepository.findAll().size();

        // Create the Cliente

        restClienteMockMvc.perform(post("/api/clientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cliente)))
                .andExpect(status().isCreated());

        // Validate the Cliente in the database
        List<Cliente> clientes = clienteRepository.findAll();
        assertThat(clientes).hasSize(databaseSizeBeforeCreate + 1);
        Cliente testCliente = clientes.get(clientes.size() - 1);
        assertThat(testCliente.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCliente.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testCliente.getFecha_nacimiento()).isEqualTo(DEFAULT_FECHA_NACIMIENTO);
        assertThat(testCliente.getCelular()).isEqualTo(DEFAULT_CELULAR);
        assertThat(testCliente.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testCliente.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);

        // Validate the Cliente in ElasticSearch
        Cliente clienteEs = clienteSearchRepository.findOne(testCliente.getId());
        assertThat(clienteEs).isEqualToComparingFieldByField(testCliente);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteRepository.findAll().size();
        // set the field null
        cliente.setNombre(null);

        // Create the Cliente, which fails.

        restClienteMockMvc.perform(post("/api/clientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cliente)))
                .andExpect(status().isBadRequest());

        List<Cliente> clientes = clienteRepository.findAll();
        assertThat(clientes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApellidoIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteRepository.findAll().size();
        // set the field null
        cliente.setApellido(null);

        // Create the Cliente, which fails.

        restClienteMockMvc.perform(post("/api/clientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cliente)))
                .andExpect(status().isBadRequest());

        List<Cliente> clientes = clienteRepository.findAll();
        assertThat(clientes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFecha_nacimientoIsRequired() throws Exception {
        int databaseSizeBeforeTest = clienteRepository.findAll().size();
        // set the field null
        cliente.setFecha_nacimiento(null);

        // Create the Cliente, which fails.

        restClienteMockMvc.perform(post("/api/clientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cliente)))
                .andExpect(status().isBadRequest());

        List<Cliente> clientes = clienteRepository.findAll();
        assertThat(clientes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClientes() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clientes
        restClienteMockMvc.perform(get("/api/clientes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
                .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
                .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO.toString())))
                .andExpect(jsonPath("$.[*].fecha_nacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
                .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR.toString())))
                .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION.toString())))
                .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION.toString())));
    }

    @Test
    @Transactional
    public void getCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get the cliente
        restClienteMockMvc.perform(get("/api/clientes/{id}", cliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cliente.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO.toString()))
            .andExpect(jsonPath("$.fecha_nacimiento").value(DEFAULT_FECHA_NACIMIENTO.toString()))
            .andExpect(jsonPath("$.celular").value(DEFAULT_CELULAR.toString()))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION.toString()))
            .andExpect(jsonPath("$.observacion").value(DEFAULT_OBSERVACION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCliente() throws Exception {
        // Get the cliente
        restClienteMockMvc.perform(get("/api/clientes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);
        clienteSearchRepository.save(cliente);
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Update the cliente
        Cliente updatedCliente = clienteRepository.findOne(cliente.getId());
        updatedCliente
                .nombre(UPDATED_NOMBRE)
                .apellido(UPDATED_APELLIDO)
                .fecha_nacimiento(UPDATED_FECHA_NACIMIENTO)
                .celular(UPDATED_CELULAR)
                .direccion(UPDATED_DIRECCION)
                .observacion(UPDATED_OBSERVACION);

        restClienteMockMvc.perform(put("/api/clientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCliente)))
                .andExpect(status().isOk());

        // Validate the Cliente in the database
        List<Cliente> clientes = clienteRepository.findAll();
        assertThat(clientes).hasSize(databaseSizeBeforeUpdate);
        Cliente testCliente = clientes.get(clientes.size() - 1);
        assertThat(testCliente.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCliente.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testCliente.getFecha_nacimiento()).isEqualTo(UPDATED_FECHA_NACIMIENTO);
        assertThat(testCliente.getCelular()).isEqualTo(UPDATED_CELULAR);
        assertThat(testCliente.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testCliente.getObservacion()).isEqualTo(UPDATED_OBSERVACION);

        // Validate the Cliente in ElasticSearch
        Cliente clienteEs = clienteSearchRepository.findOne(testCliente.getId());
        assertThat(clienteEs).isEqualToComparingFieldByField(testCliente);
    }

    @Test
    @Transactional
    public void deleteCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);
        clienteSearchRepository.save(cliente);
        int databaseSizeBeforeDelete = clienteRepository.findAll().size();

        // Get the cliente
        restClienteMockMvc.perform(delete("/api/clientes/{id}", cliente.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean clienteExistsInEs = clienteSearchRepository.exists(cliente.getId());
        assertThat(clienteExistsInEs).isFalse();

        // Validate the database is empty
        List<Cliente> clientes = clienteRepository.findAll();
        assertThat(clientes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);
        clienteSearchRepository.save(cliente);

        // Search the cliente
        restClienteMockMvc.perform(get("/api/_search/clientes?query=id:" + cliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO.toString())))
            .andExpect(jsonPath("$.[*].fecha_nacimiento").value(hasItem(DEFAULT_FECHA_NACIMIENTO.toString())))
            .andExpect(jsonPath("$.[*].celular").value(hasItem(DEFAULT_CELULAR.toString())))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION.toString())))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION.toString())));
    }
}
