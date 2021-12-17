/**
 * 
 */
package carlosgarcia642alu.grid.copy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import core.game.Observation;
import core.game.StateObservation;
import tools.Vector2d;
import uhu.carlosgarcia642alu.Constantes;
import uhu.carlosgarcia642alu.aestrella.Heuristica;

/**
 * @author LiTTo
 *
 */
public class Mapa {

	// =============================================================================
	// VARIABLES
	// =============================================================================

	private int ancho;
	private int alto;
	private int bloque;

	private ArrayList<ArrayList<Casilla>> tablero;

	private Casilla superman;
	private Casilla jail;

	private Casilla spawn_izquierda;
	private Casilla spawn_derecha;

	private ArrayList<Casilla> ladrones;
	private ArrayList<Casilla> civiles;
	private ArrayList<Casilla> cayendo;

	private int capturados;

	// =============================================================================
	// CONSTRUCTORES
	// =============================================================================

	/**
	 * Constructor con parametros.
	 * 
	 * @param ancho
	 * @param alto
	 * @param bloque
	 * @param grid
	 */
	public Mapa(int ancho, int alto, int bloque, StateObservation percepcion) {
		this.ancho = ancho;
		this.alto = alto;
		this.bloque = bloque;

		this.tablero = new ArrayList<ArrayList<Casilla>>();

		this.superman = new Casilla();
		this.jail = new Casilla();

		this.spawn_izquierda = new Casilla(0, this.alto - 3, Constantes.SUELO);
		this.spawn_derecha = new Casilla(this.ancho - 1, this.alto - 3,
				Constantes.SUELO);

		this.ladrones = new ArrayList<Casilla>();
		this.civiles = new ArrayList<Casilla>();
		this.cayendo = new ArrayList<Casilla>();

		this.capturados = 0;

		generaCasillas();
		cargaInmovable(percepcion.getImmovablePositions());
		cargaMovable(percepcion.getMovablePositions());
		cargaNPC(percepcion.getNPCPositions());
		cargaAvatar(percepcion.getAvatarPosition());
		asignaVecinos();
	}

	// =============================================================================
	// GETs y SETs
	// =============================================================================

	/**
	 * Devuelve el ancho del mapa.
	 * 
	 * @return Ancho del mapa.
	 */
	public int getAncho() {
		return this.ancho;
	}

	/**
	 * Devuelve el alto del mapa.
	 * 
	 * @return Alto del mapa.
	 */
	public int getAlto() {
		return this.alto;
	}

	/**
	 * Devuelve la casilla correspondiente al spawn de la izquierda.
	 * 
	 * @return Casilla spawn izquierda.
	 */
	public Casilla getSpawnIzquierda() {
		return this.spawn_izquierda;
	}

	/**
	 * Devuelve la casilla correspondiente al spawn de la derecha.
	 * 
	 * @return Casilla spawn derecha.
	 */
	public Casilla getSpawnDerecha() {
		return this.spawn_derecha;
	}

	/**
	 * Devuelve la casilla donde se encuentra el avatar dentro del tablero.
	 * 
	 * @return Casilla superman.
	 */
	public Casilla getAvatar() {
		return this.superman;
	}

	/**
	 * Devuelve la casilla donde se encuentra la carcel dentro del tablero.
	 * 
	 * @return Casilla jail.
	 */
	public Casilla getJail() {
		return this.jail;
	}

	/**
	 * Devuelve el numero de ladrones cargados en el mapa y ordenados en funcion de
	 * la distancia al avatar.
	 * 
	 * @return Numero de ladrones actuales en el mapa.
	 */
	public ArrayList<Casilla> getLadrones() {
		return this.ladrones;
	}

	/**
	 * Devuelve el numero de civiles cargados en el mapa.
	 * 
	 * @return Numero de civiles actuales en el mapa.
	 */
	public ArrayList<Casilla> getCiviles() {
		return this.civiles;
	}

	/**
	 * Devuelve el numero de civiles observados que han cambiado su estado a
	 * cayendo.
	 * 
	 * @return Numero de civiles cayendo
	 */
	public ArrayList<Casilla> getCayendo() {
		return this.cayendo;
	}

	public int getCapturados() {
		return this.capturados;
	}

	/**
	 * Devuelve el nodo que se encuentra en las coordenadas pasadas por parametro
	 * 
	 * @param x Coordenada X del nodo
	 * @param y Coordenada Y del nodo
	 * @return Casilla que se encuentra en la posicion X e Y del mapa
	 */
	public Casilla getNodo(int x, int y) {
		return this.tablero.get(x).get(y);
	}

	/**
	 * Cambia el estado de la casilla que se encuentra en la misma posicion que la
	 * casilla pasada por parametro.
	 * 
	 * @param n Casilla a la que se cambiara el estado.
	 */
	public void setNodo(Casilla n) {
		this.tablero.get(n.getX()).get(n.getY()).setEstado(n.getEstado());
	}

	// =============================================================================
	// METODOS
	// =============================================================================

	/**
	 * Actualiza las observaciones del mapa y vuelve a realizar todas las
	 * reasignaciones.
	 * 
	 * @param percepcion
	 */
	public void actualiza(StateObservation percepcion) {
		this.tablero.clear();
		generaCasillas();
		cargaInmovable(percepcion.getImmovablePositions());
		cargaMovable(percepcion.getMovablePositions());
		cargaNPC(percepcion.getNPCPositions());
		cargaAvatar(percepcion.getAvatarPosition());
		cargaCapturados(percepcion.getAvatarResources());
		asignaVecinos();
	}

	/**
	 * Inicializa todas las casillas que forman el mapa, por defecto inicializamos
	 * todas como cielo.
	 * 
	 * @param grid Tabla con todos los obetos observados en el mapa.
	 */
	public void generaCasillas() {
		for (int x = 0; x < this.ancho; x++) {
			this.tablero.add(new ArrayList<Casilla>());
			for (int y = 0; y < this.alto; y++) {
				this.tablero.get(x).add(new Casilla(x, y, Constantes.CIELO));
			}
		}
	}

	/**
	 * Asigna a las casillas, donde se encuentran los objetos inmovibles, su estado
	 * correspondiente.
	 * 
	 * @param percepcion Observacion del estado actual.
	 */
	private void cargaInmovable(ArrayList<Observation>[] inmovable) {
		if (inmovable != null) {
			for (int i = 0; i < inmovable.length; i++) {
				for (int j = 0; j < inmovable[i].size(); j++) {
					int x = (int) inmovable[i].get(j).position.x / this.bloque;
					int y = (int) inmovable[i].get(j).position.y / this.bloque;

					switch (inmovable[i].get(j).itype) {
					case Constantes.muro_tipo:
						this.tablero.get(x).get(y).setEstado(Constantes.MURO);
						break;
					case Constantes.nube_tipo:
						this.tablero.get(x).get(y).setEstado(Constantes.NUBE);
						break;
					case Constantes.suelo_tipo:
						this.tablero.get(x).get(y).setEstado(Constantes.SUELO);
						break;
					case Constantes.jail_tipo:
						/*
						 * Vamos a ponerlo por defecto como un lugar no accesible
						 * para evitar que pase por error y encarcele a ladrones
						 * cuando no nos interese.
						 */
						this.tablero.get(x).get(y).setEstado(Constantes.MURO);
						this.jail = this.tablero.get(x).get(y);
						break;
					}

				}
			}
		}
	}

	/**
	 * Asigna a las casillas, donde se encuentran los objetos moviles, su estado
	 * correspondiente.
	 * 
	 * @param percepcion Observacion del estado actual.
	 */
	private void cargaMovable(ArrayList<Observation>[] movable) {
		if (movable != null) {
			for (int i = 0; i < movable.length; i++) {
				for (int j = 0; j < movable[i].size(); j++) {
					int x = (int) movable[i].get(j).position.x / this.bloque;
					int y = (int) movable[i].get(j).position.y / this.bloque;

					switch (movable[i].get(j).itype) {
					case Constantes.civilN_tipo:
						this.tablero.get(x).get(y).setEstado(Constantes.CIVILN);
						this.civiles.add(this.tablero.get(x).get(y));
						break;
					case Constantes.civilC_tipo:
						this.tablero.get(x).get(y).setEstado(Constantes.CIVILC);
						this.tablero.get(x).get(y).setDistanciaHastaDestino(0);
						this.tablero.get(x).get(y).setDistanciaHastaInicio(Heuristica
								.calcularDistancia(x, this.alto - 1, x, y));
						this.cayendo.add(this.tablero.get(x).get(y));
						Collections.sort(this.cayendo);
						break;
					case Constantes.bala_tipo:
						this.tablero.get(x).get(y).setEstado(Constantes.BALA);
						break;
					}
				}
			}
		}

	}

	/**
	 * Carga en el mapa las casillas donde se encuentran NPCs.
	 * 
	 * @param percepcion Observacion del estado actual.
	 */
	private void cargaNPC(ArrayList<Observation>[] NPC) {
		if (NPC != null) {
			for (int i = 0; i < NPC.length; i++) {
				for (int j = 0; j < NPC[i].size(); j++) {
					int x = (int) NPC[i].get(j).position.x / this.bloque;
					int y = (int) NPC[i].get(j).position.y / this.bloque;

					switch (NPC[i].get(j).itype) {
					case Constantes.ladronD_tipo:
					case Constantes.ladronI_tipo:
						this.tablero.get(x).get(y).setEstado(Constantes.LADRON);
						this.tablero.get(x).get(y).setDistanciaHastaDestino(0);
						this.tablero.get(x).get(y).setDistanciaHastaInicio(
								Heuristica.calcularDistancia(this.superman.getX(),
										this.superman.getY(), x, y));
						this.ladrones.add(this.tablero.get(x).get(y));
						Collections.sort(this.ladrones);
						break;
					}
				}
			}
		}

	}

	/**
	 * Asigna la posicion del avatar dentro del tablero y guarda la casilla en la
	 * variable superman.
	 * 
	 * @param percepcion Observaci�n del estado actual.
	 */
	private void cargaAvatar(Vector2d pos) {
		int x = (int) pos.x / this.bloque;
		int y = (int) pos.y / this.bloque;

		this.tablero.get(x).get(y).setEstado(Constantes.SUPERMAN);
		this.superman = this.tablero.get(x).get(y);
	}

	public void cargaCapturados(HashMap<Integer, Integer> recursos) {
		if (recursos.get(13) != null) {
			this.capturados = recursos.get(13).intValue();
		}
	}

	/**
	 * Asigna los vecinosa cada nodo que forma el mapa, mediante la vecindad tipo 4,
	 * esto no hace posible realizar movimientos en diagonal.
	 */
	public void asignaVecinos() {
		for (int x = 0; x < this.ancho; x++) {
			for (int y = 0; y < this.alto; y++) {

				// Si no es borde superior
				if (y != 0) {
					this.tablero.get(x).get(y)
							.setVecinoArriba(this.tablero.get(x).get(y - 1));
				}

				// Si no es borde inferior
				if (y != alto - 1) {
					this.tablero.get(x).get(y)
							.setVecinoAbajo(this.tablero.get(x).get(y + 1));
				}

				// Si no es borde izquierdo
				if (x != 0) {
					this.tablero.get(x).get(y)
							.setVecinoIzquierda(this.tablero.get(x - 1).get(y));
				}

				// Si no es borde derecho
				if (x != ancho - 1) {
					this.tablero.get(x).get(y)
							.setVecinoDerecha(this.tablero.get(x + 1).get(y));
				}
			}
		}
	}

	/**
	 * Visualiza en la consola el mapa, representando el tipo de estado de cada
	 * casilla, es decir, su contenido.
	 */
	public void visualiza() {
		System.out.println();

		for (int i = 0; i < this.alto; i++) {
			for (int j = 0; j < this.ancho; j++) {
				System.out.print(this.tablero.get(j).get(i).getEstado() + " ");
			}
			System.out.println();
		}
		System.out.println("Capturados = " + this.capturados);

		System.out.println();

	}

}
