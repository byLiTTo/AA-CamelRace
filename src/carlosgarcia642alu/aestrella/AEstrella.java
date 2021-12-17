/**
 * 
 */
package uhu.carlosgarcia642alu.aestrella;

import java.util.ArrayList;
import java.util.Collections;

import ontology.Types.ACTIONS;
import uhu.carlosgarcia642alu.Constantes;
import uhu.carlosgarcia642alu.grid.Camino;
import uhu.carlosgarcia642alu.grid.Casilla;
import uhu.carlosgarcia642alu.grid.Mapa;

/**
 * @author LiTTo
 *
 */
public class AEstrella {

	// =============================================================================
	// VARIABLES
	// =============================================================================

	private Mapa mapa;
	private Casilla inicio;
	private Casilla destino;

	private ArrayList<Casilla> listaCerrados;
	private ArrayList<Casilla> listaAbiertos;
	private Camino camino;

	// =============================================================================
	// CONSTRUCTORES
	// =============================================================================

	/**
	 * Constructor con parametros de la clase.
	 * 
	 * @param m
	 */
	public AEstrella(Mapa m) {
		this.mapa = m;
		this.inicio = new Casilla();
		this.destino = new Casilla();

		this.listaCerrados = new ArrayList<Casilla>();
		this.listaAbiertos = new ArrayList<Casilla>();
	}

	// =============================================================================
	// GETs y SETs
	// =============================================================================

	/**
	 * Devuelve el camino completo que se ha calculado.
	 * 
	 * @return
	 */
	public Camino getCamino() {
		return camino;
	}

	// =============================================================================
	// METODOS
	// =============================================================================

	/**
	 * Metodo que calcula el camino minimo entre dos puntos pasados por parametro,
	 * en el mapa en cuestion.
	 * 
	 * @param m Mapa donde se calculara el camino.
	 * @param i Casilla inicial de donde comienza el camino.
	 * @param f Casilla destino, donde acaba el camino.
	 * @return Primera accion a realizar para recorrer el camino.
	 */
	public ACTIONS calculaCamino(Mapa m, Casilla i, Casilla f) {
		this.mapa = m;
		this.inicio = i;
		this.destino = f;

		listaCerrados = new ArrayList<Casilla>();
		listaAbiertos = new ArrayList<Casilla>();

		if (this.mapa.getNodo(this.destino.getX(), this.destino.getY())
				.getEstado() == Constantes.MURO) {
			return ACTIONS.ACTION_NIL;
		}

		this.listaCerrados.clear();
		this.listaAbiertos.clear();

		this.mapa.getNodo(this.inicio.getX(), this.inicio.getY())
				.setDistanciaHastaInicio(0);
		this.listaAbiertos
				.add(this.mapa.getNodo(this.inicio.getX(), this.inicio.getY()));

		while (this.listaAbiertos.size() != 0) {
			Casilla actual = this.listaAbiertos.get(0);

			if (actual.getX() == this.destino.getX()
					&& actual.getY() == this.destino.getY()) {
				return reconstruirCamino(actual);
			} else {
				this.listaAbiertos.remove(actual);
				this.listaCerrados.add(actual);

				for (Casilla vecino : actual.getVecinos()) {
					boolean mejorOpcion = false;

					if (this.listaCerrados.contains(vecino)) {
						continue;
					}

					if (vecino.getEstado() != Constantes.MURO) {
						float distanciaVecino = Heuristica.calcularDistancia(
								vecino.getX(), vecino.getY(), this.inicio.getX(),
								this.inicio.getY())
								+ Heuristica.calcularDistancia(vecino.getX(),
										vecino.getY(), this.destino.getX(),
										this.destino.getY());

						if (!this.listaAbiertos.contains(vecino)) {
							this.listaAbiertos.add(vecino);
							Collections.sort(this.listaAbiertos);
							mejorOpcion = true;
						} else if (distanciaVecino < (actual
								.getDistanciaHastaInicio()
								+ actual.getDistanciaHastaDestino())) {
							mejorOpcion = true;
						} else {
							mejorOpcion = false;
						}

						if (mejorOpcion) {
							vecino.setCasillaAnterior(actual);
							vecino.setDistanciaHastaInicio(Heuristica
									.calcularDistancia(vecino.getX(), vecino.getY(),
											inicio.getX(), inicio.getY()));
							vecino.setDistanciaHastaDestino(
									Heuristica.calcularDistancia(vecino.getX(),
											vecino.getY(), this.destino.getX(),
											this.destino.getY()));
						}
					}
				}
			}
		}

		return null;

	}

	/**
	 * Recorre el camino desde el la meta hasta el jugador para devolver el camino
	 * que hay que seguir para llegar a la meta.
	 * 
	 * @param n Casilla del cual se parte
	 * @return Camino de nodos desde el avatar hasta la meta
	 */
	public ACTIONS reconstruirCamino(Casilla n) {
		Camino c = new Camino();
		while (n.getCasillaAnterior() != null) {
			c.addFront(n);
			n = n.getCasillaAnterior();
		}
		this.camino = c;
		if (camino.size() != 0) {
			return calculaMovimiento(camino.get(0));
		} else {
			return ACTIONS.ACTION_NIL;
		}
	}

	/**
	 * Calcula la accion a realizar para pasar del inicio del camino al segundo
	 * punto.
	 * 
	 * @param n Punto del camino al que se desea mover.
	 * @return Accion para llegar a dicho punto.
	 */
	public ACTIONS calculaMovimiento(Casilla n) {
		ACTIONS accion = null;
		int x = inicio.getX();
		int y = inicio.getY();

		int nx = n.getX();
		int ny = n.getY();

		if (nx < x && ny == y) {
			accion = ACTIONS.ACTION_LEFT;
			// System.out.println("ACCION: <");
		} else if (nx > x && ny == y) {
			accion = ACTIONS.ACTION_RIGHT;
			// System.out.println("ACCION: >");
		} else if (nx == x && ny < y) {
			accion = ACTIONS.ACTION_UP;
			// System.out.println("ACCION: ^");
		} else if (nx == x && ny > y) {
			accion = ACTIONS.ACTION_DOWN;
			// System.out.println("ACCION: v");
		} else {
			accion = ACTIONS.ACTION_NIL;
			// System.out.println("ACCION: -");
		}
		return accion;
	}

}
