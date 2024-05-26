package com.practica.genericas;


public class Persona {
	private String email;
	private DatosPersonales datosPersonales;
	private Ubicacion ubicacion;

	public Persona(DatosPersonales datosPersonales, String email, Ubicacion ubicacion){
		this.datosPersonales = datosPersonales;
		this.email = email;
		this.ubicacion = ubicacion;
	}

	public String getNombre() {
		return datosPersonales.getNombre();
	}

	public String getApellidos() {
		return datosPersonales.getApellidos();
	}

	public String getDocumento() {
		return datosPersonales.getDocumento();
	}

	public String getEmail() {
		return email;
	}

	public String getDireccion() {
		return ubicacion.getDireccion();
	}

	public String getCp() {
		return ubicacion.getCp();
	}

	public FechaHora getFechaNacimiento() {
		return datosPersonales.getFechaNacimiento();
	}

	@Override
	public String toString() {
		FechaHora fecha = getFechaNacimiento();
		String cadena = "";
		// Documento
		cadena += String.format("%s;", getDocumento());
		// Nombre y apellidos
		cadena += String.format("%s,%s;", getApellidos(), getNombre());
		// correo electrónico
		cadena += String.format("%s;", getEmail());
        // Direccion y código postal
		cadena += String.format("%s,%s;", getDireccion(), getCp());
        // Fecha de nacimiento
		cadena+=String.format("%02d/%02d/%04d\n", fecha.getFecha().getDia(), 
        		fecha.getFecha().getMes(), 
        		fecha.getFecha().getAnio());

		return cadena;
	}
}
