package com.jp.peluqueria.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jp.peluqueria.domain.Corte;

import com.jp.peluqueria.repository.CorteRepository;
import com.jp.peluqueria.repository.search.CorteSearchRepository;
import com.jp.peluqueria.web.rest.util.HeaderUtil;
import com.jp.peluqueria.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Corte.
 */
@RestController
@RequestMapping("/api")
public class CorteResource {

    private final Logger log = LoggerFactory.getLogger(CorteResource.class);
        
    @Inject
    private CorteRepository corteRepository;

    @Inject
    private CorteSearchRepository corteSearchRepository;

    /**
     * POST  /cortes : Create a new corte.
     *
     * @param corte the corte to create
     * @return the ResponseEntity with status 201 (Created) and with body the new corte, or with status 400 (Bad Request) if the corte has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cortes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Corte> createCorte(@Valid @RequestBody Corte corte) throws URISyntaxException {
    	System.out.println("entre a crear corte");
    	System.out.println("el corte es => " + corte);
        log.debug("REST request to save Corte : {}", corte);
        if (corte.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("corte", "idexists", "A new corte cannot already have an ID")).body(null);
        }
        Corte result = corteRepository.save(corte);
        corteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cortes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("corte", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cortes : Updates an existing corte.
     *
     * @param corte the corte to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated corte,
     * or with status 400 (Bad Request) if the corte is not valid,
     * or with status 500 (Internal Server Error) if the corte couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cortes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Corte> updateCorte(@Valid @RequestBody Corte corte) throws URISyntaxException {
    	System.out.println("entre a crear corte");
        log.debug("REST request to update Corte : {}", corte);
        if (corte.getId() == null) {
            return createCorte(corte);
        }
        Corte result = corteRepository.save(corte);
        corteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("corte", corte.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cortes : get all the cortes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cortes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/cortes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Corte>> getAllCortes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Cortes");
        Page<Corte> page = corteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cortes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cortes/:id : get the "id" corte.
     *
     * @param id the id of the corte to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the corte, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cortes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Corte> getCorte(@PathVariable Long id) {
        log.debug("REST request to get Corte : {}", id);
        Corte corte = corteRepository.findOne(id);
        return Optional.ofNullable(corte)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cortes/:id : delete the "id" corte.
     *
     * @param id the id of the corte to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cortes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCorte(@PathVariable Long id) {
        log.debug("REST request to delete Corte : {}", id);
        corteRepository.delete(id);
        corteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("corte", id.toString())).build();
    }

    /**
     * SEARCH  /_search/cortes?query=:query : search for the corte corresponding
     * to the query.
     *
     * @param query the query of the corte search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/cortes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Corte>> searchCortes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Cortes for query {}", query);
        Page<Corte> page = corteSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cortes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
