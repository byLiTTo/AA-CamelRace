/**
 * 
 */
package uhu.carlosgarcia642alu.grid;

import java.util.ArrayList;

/**
 * @author LiTTo
 *
 */
public class Casilla implements Comparable<Casilla> {

	// =============================================================================
	// VARIABLES
	// =============================================================================

	private int x;
	private int y;
	private String estado;

	private float distanciaHastaInicio;
	private float distanciaHastaDestino;

	private Casilla casillaAnterior;

	private Casilla vecinoArriba;
	private Casilla vecinoAbajo;
	private Casilla vecinoIzquierda;
	private Casilla vecinoDerecha;

	private ArrayList<Casilla> vecinos;

	// =============================================================================
	// CONSTRUCTORES
	// =============================================================================

	/**
	 * Constructor publico sin parametros.
	 */
	public Casilla() {
	}

	/**
	 * Constructor con parametros.
	 * 
	 * @param x      Coordenada X de la casilla.
	 * @param y      Coordenada Y de la casilla.
	 * @param estado Estado en el que se encuentra la casilla;
	 */
	public Casilla(int x, int y, String estado) {
		this.x = x;
		this.y = y;
		this.estado = estado;
		this.distanciaHastaInicio = Float.MAX_VALUE;

		vecinos = new ArrayList<Casilla>();
	}

	// =============================================================================
	// GETs y SETs
	// =============================================================================

	/**
	 * 
	 * @return Coordenada X de la casilla.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Cambia el valor de la coordenada X de la casilla por el pasado por parametro.
	 * 
	 * @param x Valor de la coordena X por la que se va a cambiar.
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * 
	 * @return Coordenada Y de la casilla.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Cambia el valor de la coordenada Y de la casilla por el pasado por parametro.
	 * 
	 * @param x Valor de la coordena Y por la que se va a cambiar.
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * 
	 * @return Estado en el que se encuentra la casilla.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Cambia el estado en el que se encuentra la casilla por el pasado por
	 * parametro.
	 * 
	 * @param estado Valor del estado por el que se va a cambiar.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return Distancia desde la casilla actual hasta la casilla de inicio de un
	 *         camino que se vaya a tomar o calcular.
	 */
	public float getDistanciaHastaInicio() {
		return this.distanciaHastaInicio;
	}

	/**
	 * Cambia el valor de la distancia entre la casilla de inicio y la casilla
	 * actual pasada por parametro.
	 * 
	 * @param distanciaHastaAvatar Valor de la distancia por la que se va a cambiar.
	 */
	public void setDistanciaHastaInicio(float distanciaHastaInicio) {
		this.distanciaHastaInicio = distanciaHastaInicio;
	}

	/**
	 * 
	 * @return Distancia desde la casilla actual al final del camino.
	 */
	public float getDistanciaHastaDestino() {
		return this.distanciaHastaDestino;
	}

	/**
	 * Cambia el valor de la distancia entre el final del camino y la casilla actual
	 * por el pasado por parametro.
	 * 
	 * @param distanciaHastaBandera
	 */
	public void setDistanciaHastaDestino(float distanciaHastaDestino) {
		this.distanciaHastaDestino = distanciaHastaDestino;
	}

	/**
	 * 
	 * @return Casilla anterior en el camino mas corto calculado.
	 */
	public Casilla getCasillaAnterior() {
		return this.casillaAnterior;
	}

	/**
	 * Cambia el valor de la casilla anterior para apuntar a la pasada por
	 * parametro.
	 * 
	 * @param casillaAnterior Casilla que se convertira en la anterior de la actual.
	 */
	public void setCasillaAnterior(Casilla casillaAnterior) {
		this.casillaAnterior = casillaAnterior;
	}

	/**
	 * 
	 * @return Casilla situada arriba en el mapa.
	 */
	public Casilla getVecinoArriba() {
		return vecinoArriba;
	}

	/**
	 * Cambia el vecino de arriba del nodo en cuestion. Si el nodo ya es vecino, lo
	 * borra y lo inserta como superior.
	 * 
	 * @param vecinoArriba
	 */
	public void setVecinoArriba(Casilla vecinoArriba) {
		if (vecinos.contains(vecinoArriba)) {
			vecinos.remove(vecinoArriba);
		}
		vecinos.add(vecinoArriba);
		this.vecinoArriba = vecinoArriba;
	}

	/**
	 * 
	 * @return Casilla situada abajo en el mapa.
	 */
	public Casilla getVecinoAbajo() {
		return vecinoAbajo;
	}

	/**
	 * Cambia el vecino de abajo del nodo en cuestion. Si el nodo ya es vecino, lo
	 * borra y lo aade como inferior.
	 * 
	 * @param vecinoAbajo
	 */
	public void setVecinoAbajo(Casilla vecinoAbajo) {
		if (vecinos.contains(vecinoAbajo)) {
			vecinos.remove(vecinoAbajo);
		}
		vecinos.add(vecinoAbajo);
		this.vecinoAbajo = vecinoAbajo;
	}

	/**
	 * 
	 * @return Casilla situada a la izquierda en el mapa.
	 */
	public Casilla getVecinoIzquierda() {
		return vecinoIzquierda;
	}

	/**
	 * Cambia el vecino de la izquierda del nodo en cuestion. Si el nodo ya es
	 * vecino, lo borra y lo inserta como vecino izquierdo.
	 * 
	 * @param vecinoIzquierda
	 */
	public void setVecinoIzquierda(Casilla vecinoIzquierda) {
		if (vecinos.contains(vecinoIzquierda)) {
			vecinos.remove(vecinoIzquierda);
		}
		vecinos.add(vecinoIzquierda);
		this.vecinoIzquierda = vecinoIzquierda;
	}

	/**
	 * 
	 * @return Casilla situada a la derecha en el mapa.
	 */
	public Casilla getVecinoDerecha() {
		return vecinoDerecha;
	}

	/**
	 * Cambia el vecino de la derecha del nodo en cuestion. Si el nodo ya es vecino,
	 * lo borra y lo inserta como vecino derecho.
	 * 
	 * @param vecinoDerecha
	 */
	public void setVecinoDerecha(Casilla vecinoDerecha) {
		if (vecinos.contains(vecinoDerecha)) {
			vecinos.remove(vecinoDerecha);
		}
		vecinos.add(vecinoDerecha);
		this.vecinoDerecha = vecinoDerecha;
	}

	/**
	 * 
	 * @return Vector con todos los vecinos de la casilla actual. Formada por
	 *         vecindad de tipo 4. (Arriba, abajo, izquierda y derecha).
	 */
	public ArrayList<Casilla> getVecinos() {
		return this.vecinos;
	}

	/**
	 * Cambia el vector de vecinos de la casilla actual.
	 * 
	 * @param vecinos Vector de los nuevos vecinos de la casilla en cuesti�n.
	 */
	public void setVecinos(ArrayList<Casilla> vecinos) {
		this.vecinos = vecinos;
	}

	// =============================================================================
	// SOBRECARGAS
	// =============================================================================

	@Override
	public boolean equals(Object o) {
		return (((Casilla) o).getX() == this.x) && (((Casilla) o).getY() == this.y);
	}

	@Override
	public int compareTo(Casilla n) {
		if ((this.getDistanciaHastaInicio()
				+ this.getDistanciaHastaDestino()) < (n.getDistanciaHastaInicio()
						+ n.getDistanciaHastaDestino())) {
			return -1;
		} else if ((this.getDistanciaHastaInicio()
				+ this.getDistanciaHastaDestino()) > (n.getDistanciaHastaInicio()
						+ n.getDistanciaHastaDestino())) {
			return 1;
		} else {
			return 0;
		}
	}

}
