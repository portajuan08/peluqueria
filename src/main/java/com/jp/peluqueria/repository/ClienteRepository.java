package com.jp.peluqueria.repository;

import com.jp.peluqueria.domain.Cliente;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Cliente entity.
 */
@SuppressWarnings("unused")
public interface ClienteRepository extends JpaRepository<Cliente,Long> {

}
