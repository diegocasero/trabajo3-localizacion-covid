package com.practica.ems.covid;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.practica.excecption.EmsDuplicateLocationException;
import com.practica.excecption.EmsDuplicatePersonException;
import com.practica.excecption.EmsInvalidNumberOfDataException;
import com.practica.excecption.EmsInvalidTypeException;
import com.practica.excecption.EmsLocalizationNotFoundException;
import com.practica.excecption.EmsPersonNotFoundException;
import com.practica.genericas.*;
import com.practica.lista.ListaContactos;

public class ContactosCovid {
	private Poblacion poblacion;
	private Localizacion localizacion;
	private ListaContactos listaContactos;

	public ContactosCovid() {
		this.poblacion = new Poblacion();
		this.localizacion = new Localizacion();
		this.listaContactos = new ListaContactos();
	}

	public Poblacion getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(Poblacion poblacion) {
		this.poblacion = poblacion;
	}

	public Localizacion getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(Localizacion localizacion) {
		this.localizacion = localizacion;
	}
	
	

	public ListaContactos getListaContactos() {
		return listaContactos;
	}

	public void setListaContactos(ListaContactos listaContactos) {
		this.listaContactos = listaContactos;
	}

	public void loadData(String data, boolean reset) throws EmsInvalidTypeException, EmsInvalidNumberOfDataException,
			EmsDuplicatePersonException, EmsDuplicateLocationException {
		// borro información anterior
		if (reset) {
			this.poblacion = new Poblacion();
			this.localizacion = new Localizacion();
			this.listaContactos = new ListaContactos();
		}
		String datas[] = dividirEntrada(data);
		insertarDatos(datas);
	}

	public void loadDataFile(String fichero, boolean reset) {
		FileReader fr = null;
		try {
			File archivo = new File(fichero);
			fr = new FileReader(archivo);
			BufferedReader br = new BufferedReader(fr);
			if (reset)
				resetear();
			leerFichero(br);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cerrarFichero(fr);
		}
	}

	public int findPersona(String documento) throws EmsPersonNotFoundException {
		int pos;
		try {
			pos = this.poblacion.findPersona(documento);
			return pos;
		} catch (EmsPersonNotFoundException e) {
			throw new EmsPersonNotFoundException();
		}
	}

	public int findLocalizacion(String documento, String fecha, String hora) throws EmsLocalizationNotFoundException {

		int pos;
		try {
			pos = localizacion.findLocalizacion(documento, fecha, hora);
			return pos;
		} catch (EmsLocalizationNotFoundException e) {
			throw new EmsLocalizationNotFoundException();
		}
	}

	public List<PosicionPersona> localizacionPersona(String documento) throws EmsPersonNotFoundException {
		int cont = 0;
		List<PosicionPersona> lista = new ArrayList<PosicionPersona>();
		Iterator<PosicionPersona> it = this.localizacion.getLista().iterator();
		while (it.hasNext()) {
			PosicionPersona pp = it.next();
			if (pp.getDocumento().equals(documento)) {
				cont++;
				lista.add(pp);
			}
		}
		if (cont == 0)
			throw new EmsPersonNotFoundException();
		else
			return lista;
	}

	public boolean delPersona(String documento) throws EmsPersonNotFoundException {
		int cont = 0, pos = -1;
		Iterator<Persona> it = this.poblacion.getLista().iterator();
		while (it.hasNext()) {
			Persona persona = it.next();
			if (persona.getDocumento().equals(documento)) {
				pos = cont;
			}
			cont++;
		}
		if (pos == -1) {
			throw new EmsPersonNotFoundException();
		}
		this.poblacion.getLista().remove(pos);
		return false;
	}

	private void resetear(){
		this.poblacion = new Poblacion();
		this.localizacion = new Localizacion();
		this.listaContactos = new ListaContactos();
	}

	private void leerFichero(BufferedReader br) throws IOException, EmsInvalidNumberOfDataException, EmsDuplicateLocationException, EmsInvalidTypeException, EmsDuplicatePersonException {
		String datas[] = null, data = null;
		while ((data = br.readLine()) != null) {
			datas = dividirEntrada(data.trim());
			insertarDatos(datas);
		}
	}

	private void cerrarFichero(FileReader fr){
		try {
			if (null != fr) {
				fr.close();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	private String[] dividirEntrada(String input) {
		String cadenas[] = input.split("\\n");
		return cadenas;
	}

	private String[] dividirLineaData(String data) {
		String cadenas[] = data.split("\\;");
		return cadenas;
	}

	private Persona crearPersona(String[] data) {
		DatosPersonales datosPersonales = crearDatosPersonales(data);
		String email = data[4];
		Ubicacion ubicacion = crearUbicacion(data);
		return new Persona(datosPersonales, email, ubicacion);
	}

	private DatosPersonales crearDatosPersonales(String[] data){
		String documento = data[1];
		String nombre = data[2];
		String apellidos = data[3];
		FechaHora fechaNacimiento = Utilidades.parsearFecha(data[7]);
		return new DatosPersonales(documento, nombre, apellidos, fechaNacimiento);
	}

	private Ubicacion crearUbicacion(String[] data){
		String direccion = data[5];
		String cp = data[6];
		return new Ubicacion(direccion, cp);
	}

	private PosicionPersona crearPosicionPersona(String[] data) {
		PosicionPersona posicionPersona = new PosicionPersona();
		String fecha = null, hora;
		float latitud = 0, longitud;
		for (int i = 1; i < Constantes.MAX_DATOS_LOCALIZACION; i++) {
			String s = data[i];
			switch (i) {
			case 1:
				posicionPersona.setDocumento(s);
				break;
			case 2:
				fecha = data[i];
				break;
			case 3:
				hora = data[i];
				posicionPersona.setFechaPosicion(Utilidades.parsearFecha(fecha, hora));
				break;
			case 4:
				latitud = Float.parseFloat(s);
				break;
			case 5:
				longitud = Float.parseFloat(s);
				posicionPersona.setCoordenada(new Coordenada(latitud, longitud));
				break;
			}
		}
		return posicionPersona;
	}

	private void insertarDatos(String[] datas) throws EmsDuplicateLocationException, EmsInvalidTypeException, EmsInvalidNumberOfDataException, EmsDuplicatePersonException {
		for (String linea : datas) {
			String datos[] = this.dividirLineaData(linea);
			if (!datos[0].equals("PERSONA") && !datos[0].equals("LOCALIZACION")) {
				throw new EmsInvalidTypeException();
			}
			if (datos[0].equals("PERSONA")) {
				insertarDatosPersona(datos);
			}
			if (datos[0].equals("LOCALIZACION")) {
				insertarDatosLocalizacion(datos);
			}
		}
	}

	private void insertarDatosPersona(String[] datos) throws EmsInvalidNumberOfDataException, EmsDuplicatePersonException {
		if (datos.length != Constantes.MAX_DATOS_PERSONA) {
			throw new EmsInvalidNumberOfDataException("El número de datos para PERSONA es menor de 8");
		}
		this.poblacion.addPersona(this.crearPersona(datos));
	}

	private void insertarDatosLocalizacion(String[] datos) throws EmsInvalidNumberOfDataException, EmsDuplicateLocationException {
		if (datos.length != Constantes.MAX_DATOS_LOCALIZACION) {
			throw new EmsInvalidNumberOfDataException("El número de datos para LOCALIZACION es menor de 6");
		}
		PosicionPersona pp = this.crearPosicionPersona(datos);
		this.localizacion.addLocalizacion(pp);
		this.listaContactos.insertarNodoTemporal(pp);
	}
}
