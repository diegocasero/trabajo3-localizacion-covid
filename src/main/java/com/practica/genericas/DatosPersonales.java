package com.practica.genericas;

public class DatosPersonales {
    private String documento, nombre, apellidos;
    FechaHora fechaNacimiento;

    public DatosPersonales(String documento, String nombre, String apellidos, FechaHora fechaNacimiento){
        this.documento = documento;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDocumento() {
        return documento;
    }

    public FechaHora getFechaNacimiento() {
        return fechaNacimiento;
    }
}
