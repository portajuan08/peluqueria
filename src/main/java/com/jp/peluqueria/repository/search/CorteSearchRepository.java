package com.jp.peluqueria.repository.search;

import com.jp.peluqueria.domain.Corte;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Corte entity.
 */
public interface CorteSearchRepository extends ElasticsearchRepository<Corte, Long> {
}
