package com.jp.peluqueria.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jp.peluqueria.domain.Corte;

/**
 * Spring Data JPA repository for the Corte entity.
 */
public interface CorteRepository extends JpaRepository<Corte,Long> {

	public List<Corte> findByFechas(Timestamp  fechaDesde,Timestamp fechaHasta);
	public List<Corte> findByDesdeFecha(Timestamp  fechaDesde);
	public List<Corte> findByFechasTipoServicio(Timestamp  fechaDesde,Timestamp fechaHasta, String tipoServicio);
}
