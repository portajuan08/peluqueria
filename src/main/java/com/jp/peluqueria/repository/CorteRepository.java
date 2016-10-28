package com.jp.peluqueria.repository;

import com.jp.peluqueria.domain.Corte;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Corte entity.
 */
@SuppressWarnings("unused")
public interface CorteRepository extends JpaRepository<Corte,Long> {

}
