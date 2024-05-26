package com.practica.genericas;

public class Ubicacion {
    private String direccion, cp;

    public Ubicacion(String direccion, String cp){
        this.direccion = direccion;
        this.cp = cp;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getCp() {
        return cp;
    }
}
