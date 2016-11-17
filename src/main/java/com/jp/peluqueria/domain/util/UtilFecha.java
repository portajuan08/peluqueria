package com.jp.peluqueria.domain.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class UtilFecha{
	public static Timestamp getFechaPrimerHora(Long fecha) {
		
		Calendar c1 = GregorianCalendar.getInstance();
		c1.setTimeInMillis(fecha);
		c1.set(Calendar.HOUR_OF_DAY,0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		Timestamp oFecha = new Timestamp(c1.getTime().getTime());
		return oFecha;
	}
	
	public static Timestamp getFechaUltimaHora(Long fecha){
		
		Calendar c1 = GregorianCalendar.getInstance();
		c1.setTimeInMillis(fecha);
		c1.set(Calendar.HOUR_OF_DAY,23);
		c1.set(Calendar.MINUTE, 59);
		c1.set(Calendar.SECOND, 59);
		c1.set(Calendar.MILLISECOND, 0);
		Timestamp oFecha = new Timestamp(c1.getTime().getTime());
		return oFecha;
	}
}
