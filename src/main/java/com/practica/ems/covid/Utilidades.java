package com.practica.ems.covid;

import com.practica.genericas.FechaHora;

public class Utilidades {
    public static FechaHora parsearFecha (String fecha) {
        return parsearFecha(fecha, "0:0");
    }

    public static FechaHora parsearFecha (String fecha, String hora) {
        int dia, mes, anio;
        String[] valores = fecha.split("\\/");
        dia = Integer.parseInt(valores[0]);
        mes = Integer.parseInt(valores[1]);
        anio = Integer.parseInt(valores[2]);
        int minuto, segundo;
        valores = hora.split("\\:");
        minuto = Integer.parseInt(valores[0]);
        segundo = Integer.parseInt(valores[1]);
        FechaHora fechaHora = new FechaHora(dia, mes, anio, minuto, segundo);
        return fechaHora;
    }
}
