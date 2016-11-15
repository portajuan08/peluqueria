package com.jp.peluqueria.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.elasticsearch.annotations.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jp.peluqueria.domain.enumeration.Tipo_corte;

/**
 * A Corte.
 */
@Entity
@Table(name = "corteDTO")
@Document(indexName = "corteDTO")
public class CorteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private ZonedDateTime fecha;

    @Column(name = "precio", precision=10, scale=2)
    private BigDecimal precio;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_corte", nullable = false)
    private Tipo_corte tipo_corte;

    @Column(name = "detalle")
    private String detalle;

    @ManyToOne
    @NotNull
    @JsonIgnore
    private ClienteDTO clienteDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFecha() {
        return fecha;
    }

    public CorteDTO fecha(ZonedDateTime fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(ZonedDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public CorteDTO precio(BigDecimal precio) {
        this.precio = precio;
        return this;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Tipo_corte getTipo_corte() {
        return tipo_corte;
    }

    public CorteDTO tipo_corte(Tipo_corte tipo_corte) {
        this.tipo_corte = tipo_corte;
        return this;
    }

    public void setTipo_corte(Tipo_corte tipo_corte) {
        this.tipo_corte = tipo_corte;
    }

    public String getDetalle() {
        return detalle;
    }

    public CorteDTO detalle(String detalle) {
        this.detalle = detalle;
        return this;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public ClienteDTO getClienteDTO() {
        return clienteDTO;
    }

    public CorteDTO clienteDTO(ClienteDTO cliente) {
        this.clienteDTO = cliente;
        return this;
    }

    public void setClienteDTO(ClienteDTO cliente) {
        this.clienteDTO = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CorteDTO corte = (CorteDTO) o;
        if(corte.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, corte.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Corte{" +
            "id=" + id +
            ", fecha='" + fecha + "'" +
            ", precio='" + precio + "'" +
            ", tipo_corte='" + tipo_corte + "'" +
            ", detalle='" + detalle + "'" +
            '}';
    }
}
