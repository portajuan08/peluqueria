package com.jp.peluqueria.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jp.peluqueria.domain.Corte;

/**
 * Spring Data JPA repository for the Corte entity.
 */
public interface CorteRepository extends JpaRepository<Corte,Long> {

	public List<Corte> findByFechas(Date  fechaDesde,Date fechaHasta);
	public List<Corte> findByDesdeFecha(Date  fechaDesde);
}
