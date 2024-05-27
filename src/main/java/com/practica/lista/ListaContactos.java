package com.practica.lista;

import com.practica.genericas.Coordenada;
import com.practica.genericas.FechaHora;
import com.practica.genericas.PosicionPersona;

public class ListaContactos {
	private NodoTemporal lista;
	private int size;
	
	/**
	 * Insertamos en la lista de nodos temporales, y a la vez inserto en la lista de nodos de coordenadas. 
	 * En la lista de coordenadas metemos el documento de la persona que está en esa coordenada 
	 * en un instante 
	 */
	public void insertarNodoTemporal (PosicionPersona p) {
		NodoTemporal nt = buscarNodoTemporal(p);
		personaEnCoordenadas(p,nt);
	}

	private NodoTemporal buscarNodoTemporal(PosicionPersona p){
		NodoTemporal nt = lista, ant = null;
		boolean salir = false;
		while (nt!=null && !salir) {
			if(nt.getFecha().compareTo(p.getFechaPosicion())==0) {
				return nt;
			}else if(nt.getFecha().compareTo(p.getFechaPosicion())<0) {
				ant = nt;
				nt=nt.getSiguiente();
			}else if(nt.getFecha().compareTo(p.getFechaPosicion())>0) {
				salir=true;
			}
		}
		return crearNodoTemporal(ant, nt, p);
	}

	private NodoTemporal crearNodoTemporal(NodoTemporal ant, NodoTemporal sig, PosicionPersona p){
		NodoTemporal nuevo = new NodoTemporal();
		nuevo.setFecha(p.getFechaPosicion());
		if(ant!=null) {
			nuevo.setSiguiente(sig);
			ant.setSiguiente(nuevo);
		}else {
			nuevo.setSiguiente(lista);
			lista = nuevo;
		}
		this.size++;
		return nuevo;
	}

	private void personaEnCoordenadas(PosicionPersona p, NodoTemporal nt){
		NodoPosicion np = buscarNodoPosicion(nt, p.getCoordenada());
		np.incrementarNumPersonas();
	}

	private NodoPosicion buscarNodoPosicion(NodoTemporal nt, Coordenada coordenada){
		NodoPosicion npActual = nt.getListaCoordenadas();
		NodoPosicion npAnt=null;
		while (npActual!=null) {
			if(npActual.getCoordenada().equals(coordenada)) {
				return npActual;
			}else {
				npAnt = npActual;
				npActual = npActual.getSiguiente();
			}
		}
		return crearNodoPosicion(nt, npAnt, coordenada);
	}

	private NodoPosicion crearNodoPosicion(NodoTemporal nt, NodoPosicion npAnt, Coordenada coordenada){
		NodoPosicion npNuevo = new NodoPosicion(coordenada,0, null);
		if(nt.getListaCoordenadas()==null)
			nt.setListaCoordenadas(npNuevo);
		else
			npAnt.setSiguiente(npNuevo);
		return npNuevo;
	}

	public int personasEnCoordenadas () {
		NodoPosicion aux = this.lista.getListaCoordenadas();
		if(aux==null)
			return 0;
		else {
			int cont;
			for(cont=0;aux!=null;) {
				cont += aux.getNumPersonas();
				aux=aux.getSiguiente();
			}
			return cont;
		}
	}
	
	public int tamanioLista () {
		return this.size;
	}

	public String getPrimerNodo() {
		NodoTemporal aux = lista;
		String cadena = aux.getFecha().getFecha().toString();
		cadena+= ";" +  aux.getFecha().getHora().toString();
		return cadena;
	}

	/**
	 * Métodos para comprobar que insertamos de manera correcta en las listas de 
	 * coordenadas, no tienen una utilidad en sí misma, más allá de comprobar que
	 * nuestra lista funciona de manera correcta.
	 */
	public int numPersonasEntreDosInstantes(FechaHora inicio, FechaHora fin) {
		if(this.size==0)
			return 0;
		NodoTemporal aux = lista;
		int cont = 0;
		cont = 0;
		while(aux!=null) {
			if(aux.getFecha().compareTo(inicio)>=0 && aux.getFecha().compareTo(fin)<=0) {
				NodoPosicion nodo = aux.getListaCoordenadas();
				while(nodo!=null) {
					cont = cont + nodo.getNumPersonas();
					nodo = nodo.getSiguiente();
				}				
				aux = aux.getSiguiente();
			}else {
				aux=aux.getSiguiente();
			}
		}
		return cont;
	}
	
	
	
	public int numNodosCoordenadaEntreDosInstantes(FechaHora inicio, FechaHora fin) {
		if(this.size==0)
			return 0;
		NodoTemporal aux = lista;
		int cont = 0;
		cont = 0;
		while(aux!=null) {
			if(aux.getFecha().compareTo(inicio)>=0 && aux.getFecha().compareTo(fin)<=0) {
				NodoPosicion nodo = aux.getListaCoordenadas();
				while(nodo!=null) {
					cont = cont + 1;
					nodo = nodo.getSiguiente();
				}				
				aux = aux.getSiguiente();
			}else {
				aux=aux.getSiguiente();
			}
		}
		return cont;
	}
	
	
	
	@Override
	public String toString() {
		String cadena="";
		int cont;
		cont=0;
		NodoTemporal aux = lista;
		for(cont=1; cont<size; cont++) {
			cadena += aux.getFecha().getFecha().toString();
			cadena += ";" +  aux.getFecha().getHora().toString() + " ";
			aux=aux.getSiguiente();
		}
		cadena += aux.getFecha().getFecha().toString();
		cadena += ";" +  aux.getFecha().getHora().toString();
		return cadena;
	}
	
	
	
}
