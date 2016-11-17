package com.jp.peluqueria.domain.util;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.jp.peluqueria.domain.Cliente;
import com.jp.peluqueria.domain.ClienteDTO;
import com.jp.peluqueria.domain.Corte;
import com.jp.peluqueria.domain.CorteDTO;


public class UtilCliente{
	public static ClienteDTO getClienteDTO(Cliente cliente){
		ClienteDTO clienteDTO = new ClienteDTO();
		clienteDTO.setApellido(cliente.getApellido());
		clienteDTO.setNombre(cliente.getNombre());
		clienteDTO.setCelular(cliente.getCelular());
		clienteDTO.setDireccion(cliente.getDireccion());
		clienteDTO.setFecha_nacimiento(cliente.getFecha_nacimiento());
		clienteDTO.setObservacion(cliente.getObservacion());
		clienteDTO.setId(cliente.getId());
		Set<CorteDTO> cortesDTO = new HashSet<CorteDTO>();
		for (Corte corte : cliente.getCortes()){
			CorteDTO corteDTO = new CorteDTO();
			corteDTO.setDetalle(corte.getDetalle());
			corteDTO.setFecha(corte.getFecha());
			corteDTO.setPrecio(corte.getPrecio());
			corteDTO.setTipo_corte(corte.getTipo_corte());
			cortesDTO.add(corteDTO);
		}
		cortesDTO.stream().sorted(new CorteComparator()).collect(Collectors.toSet());
		clienteDTO.setCortes(cortesDTO);
		return clienteDTO;
	}
	
	public static class CorteComparator implements Comparator<CorteDTO> {
		public int compare(CorteDTO c1, CorteDTO c2) {
			// TODO Auto-generated method stub
			return c1.getFecha().compareTo(c2.getFecha());
		}
	}

}
