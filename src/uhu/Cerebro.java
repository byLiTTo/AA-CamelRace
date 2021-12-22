/**
 * 
 */
package uhu;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;

import core.game.StateObservation;
import ontology.Types.ACTIONS;
import uhu.arbol.*;
import uhu.grid.*;
import uhu.juego15.preguntas.*;
import uhu.juego15.estados.*;
import uhu.mdp.QLearning;

import static uhu.Constantes.*;

/**
 * @author LiTTo
 *
 */
public class Cerebro {

	// =============================================================================
	// VARIABLES
	// =============================================================================

	private Mapa mapa;
	private QLearning qlearning;
	private STATES currentState;
	private STATES lastState;

	private Nodo raiz;

	// Nodos de decision - Preguntas
	private TIENES_MURO TIENES_MURO;
	private TIENES_MURO_ARRIBA TIENES_MURO_ARRIBA;
	private TIENES_MURO_ABAJO TIENES_MURO_ABAJO;
	private TIENES_MURO_IZQUIERDA TIENES_MURO_IZQUIERDA;
	private TIENES_MURO_DERECHA TIENES_MURO_DERECHA;

	// Nodos hojas - Estados
	private ORIENTE_CON_PARED_IZQUIERDA ORIENTE_CON_PARED_IZQUIERDA;
	private ORIENTE_CON_PARED_IZQUIERDA_ABAJO ORIENTE_CON_PARED_IZQUIERDA_ABAJO;
	private ORIENTE_CON_PARED_ABAJO ORIENTE_CON_PARED_ABAJO;
	private ORIENTE_CON_PARED_ABAJO_DERECHA ORIENTE_CON_PARED_ABAJO_DERECHA;
	private ORIENTE_CON_PARED_DERECHA ORIENTE_CON_PARED_DERECHA;
	private ORIENTE_CON_PARED_DERECHA_ARRIBA ORIENTE_CON_PARED_DERECHA_ARRIBA;
	private ORIENTE_CON_PARED_ARRIBA ORIENTE_CON_PARED_ARRIBA;
	private ORIENTE_SIN_PARED ORIENTE_SIN_PARED;

	private ACTIONS lastAction;
	private SENTIDO sentido;
	private ORIENTACION orientacion;

	private double reward;
	private double globalReward;

	// =============================================================================
	// CONSTRUCTORES
	// =============================================================================

	/**
	 * Constructor de la clase cerebro que crea un mapa y genera el arbol de
	 * decision.
	 * 
	 * @param percepcion observacion del estado actual.
	 */
	public Cerebro(StateObservation percepcion) {
		Dimension dim = percepcion.getWorldDimension();
		int bloque = percepcion.getBlockSize();

		this.mapa = new Mapa(dim.width / bloque, dim.height / bloque, bloque, percepcion);

		if (this.mapa.getAvatar().getX() - this.mapa.getColumnaPortal() < 0) {
			this.sentido = SENTIDO.ORIENTE;
			this.lastAction = ACTIONS.ACTION_DOWN;
			this.currentState = STATES.ORIENTE_PARED_IZQUIERDA;
			this.lastState = STATES.ORIENTE_PARED_IZQUIERDA;
			this.orientacion = ORIENTACION.SUR;

		} else {
			this.sentido = SENTIDO.OCCIDENTE;
			this.lastAction = ACTIONS.ACTION_UP;
//			this.currentState = STATES.ORIENTE_CON_PARED_IZQUIERDA;
//			this.lastState = STATES.ORIENTE_CON_PARED_IZQUIERDA;
		}

		this.qlearning = new QLearning(getStates(), getActions(), new String("QTABLE.txt"));

		this.reward = 0;
		this.globalReward = 0;

//		generaArbol();
	}

	// =============================================================================
	// GETs Y SETs
	// =============================================================================

	/**
	 * Devuelve el mapa que tiene el cerebro en su memoria.
	 * 
	 * @return Mapa generado.
	 */
	public Mapa getMapa() {
		return this.mapa;
	}

	public ACTIONS getLastAction() {
		return this.lastAction;
	}

	// =============================================================================
	// METODOS
	// =============================================================================

	public void percibe(StateObservation percepcion) {
		analizarMapa(percepcion);
		actualizaState(percepcion);
	}

	/**
	 * @param percepcion Observacion del estado actual.
	 */
	private void analizarMapa(StateObservation percepcion) {
		this.mapa.actualiza(percepcion, Visualizaciones.NADA);
	}

	private void actualizaState(StateObservation percepcion) {
//		System.out.println("Antes de actualizar");
//		System.out.println("lastState: " + lastState);
//		System.out.println("currentState: " + currentState);
//		this.currentState = this.raiz.decidir(this);

//		System.out.println("\nDespues de actualizar");
//		System.out.println("lastState: " + lastState);
//		System.out.println("currentState: " + currentState);

		if (!tienesMuroArriba() && !tienesMuroAbajo() && !tienesMuroIzquierda() && !tienesMuroDerecha()) {
			if (tienesDiagonalArribaDerecha()) {
				this.lastState = this.currentState;
				this.currentState = STATES.ORIENTE_PARED_DIAGONAL_ARRIBA_DERECHA;
				System.out.println(currentState);
				return;
			} else if (tienesDiagonalArribaIzquierda()) {
				this.lastState = this.currentState;
				this.currentState = STATES.ORIENTE_PARED_DIAGONAL_ARRIBA_IZQUIERDA;
				System.out.println(currentState);
				return;
			} else if (tienesDiagonalAbajoDerecha()) {
				this.lastState = this.currentState;
				this.currentState = STATES.ORIENTE_PARED_DIAGONAL_ABAJO_DERECHA;
				System.out.println(currentState);
				return;
			} else if (tienesDiagonalAbajoIzquierda()) {
				this.lastState = this.currentState;
				this.currentState = STATES.ORIENTE_PARED_DIAGONAL_ABAJO_IZQUIERDA;
				System.out.println(currentState);
				return;
			}

		} else if (!tienesMuroArriba() && !tienesMuroAbajo() && tienesMuroIzquierda() && !tienesMuroDerecha()) {
			this.lastState = this.currentState;
			this.currentState = STATES.ORIENTE_PARED_IZQUIERDA;
			System.out.println(currentState);
			return;
		} else if (!tienesMuroArriba() && tienesMuroAbajo() && tienesMuroIzquierda() && !tienesMuroDerecha()) {
			this.lastState = this.currentState;
			this.currentState = STATES.ORIENTE_PARED_IZQUIERDA_ABAJO;
			System.out.println(currentState);
			return;
		} else if (!tienesMuroArriba() && tienesMuroAbajo() && !tienesMuroIzquierda() && !tienesMuroDerecha()) {
			this.lastState = this.currentState;
			this.currentState = STATES.ORIENTE_PARED_ABAJO;
			System.out.println(currentState);
			return;
		} else if (!tienesMuroArriba() && tienesMuroAbajo() && !tienesMuroIzquierda() && tienesMuroDerecha()) {
			this.lastState = this.currentState;
			this.currentState = STATES.ORIENTE_PARED_ABAJO_DERECHA;
			System.out.println(currentState);
			return;
		} else if (!tienesMuroArriba() && !tienesMuroAbajo() && !tienesMuroIzquierda() && tienesMuroDerecha()) {
			this.lastState = this.currentState;
			this.currentState = STATES.ORIENTE_PARED_DERECHA;
			System.out.println(currentState);
			return;
		} else if (tienesMuroArriba() && !tienesMuroAbajo() && !tienesMuroIzquierda() && tienesMuroDerecha()) {
			this.lastState = this.currentState;
			this.currentState = STATES.ORIENTE_PARED_DERECHA_ARRIBA;
			System.out.println(currentState);
			return;
		} else if (tienesMuroArriba() && !tienesMuroAbajo() && !tienesMuroIzquierda() && !tienesMuroDerecha()) {
			this.lastState = this.currentState;
			this.currentState = STATES.ORIENTE_PARED_ARRIBA;
			System.out.println(currentState);
			return;
		} else if (tienesMuroArriba() && !tienesMuroAbajo() && tienesMuroIzquierda() && !tienesMuroDerecha()) {
			this.lastState = this.currentState;
			this.currentState = STATES.ORIENTE_PARED_ARRIBA_IZQUIERDA;
			System.out.println(currentState);
			return;
		}else if (tienesMuroArriba() && tienesMuroAbajo() && !tienesMuroIzquierda() && !tienesMuroDerecha()) {
			this.lastState = this.currentState;
			this.currentState = STATES.ORIENTE_PARED_ARRIBA_ABAJO;
			System.out.println(currentState);
			return;
		}

	}

	/**
	 * Recorre el arbol de decision y devuelve una accion a realizar.
	 * 
	 * @return Accion a realizar tras recorrer los nodos el arbol.
	 */
	public ACTIONS pensar(StateObservation percepcion) {
		this.lastAction = this.qlearning.nextOnlyOneBestAction(currentState);

//		System.out.println("\nEstado: " + this.currentState);

		return lastAction;
	}

	public ACTIONS entrenar(StateObservation percepcion) {
		double reward = getReward(lastState, lastAction, currentState);
		this.qlearning.update(lastState, lastAction, currentState, reward);
		this.lastAction = this.qlearning.nextAction(currentState);

//		System.out.println("\nEstado: " + this.currentState);
//		System.out.println("\nRECOMPENSA: " + this.globalReward);

		return lastAction;
	}

	public double getGR() {
		return this.globalReward;
	}

	// =============================================================================
	// AUXILIARES
	// =============================================================================

	private double getReward(STATES lastState, ACTIONS lastAction, STATES currentState) {
		this.reward = -50;
//
////		Casilla ahora = mapa.getAvatar();
////		Casilla antes = mapa.getLastAvatar();
////
////		int columna = mapa.getColumnaPortal();
////
////		double distanciaAhora = Math.abs(ahora.getX() - columna);
////		double distanciaAntes = Math.abs(antes.getX() - columna);
////
////		if (distanciaAhora < distanciaAntes) {
////			return 50;
////		} else if (distanciaAhora == distanciaAntes) {
////
////			return -20;
////		} else {
////			return -50;
////		}
//
//		if (lastState == STATES.ORIENTE_PARED_IZQUIERDA && lastAction == ACTIONS.ACTION_DOWN
//				&& currentState == STATES.ORIENTE_PARED_IZQUIERDA) {
//			this.reward = 50;
//		}
//		if (lastState == STATES.ORIENTE_PARED_IZQUIERDA && lastAction == ACTIONS.ACTION_DOWN
//				&& currentState == STATES.ORIENTE_PARED_IZQUIERDA_ABAJO) {
//			this.reward = 50;
//		}
//
//		if (lastState == STATES.ORIENTE_PARED_IZQUIERDA_ABAJO && lastAction == ACTIONS.ACTION_DOWN
//				&& currentState == STATES.ORIENTE_PARED_ABAJO) {
//			this.reward = 50;
//		}
//
//		if (lastState == STATES.ORIENTE_PARED_ABAJO && lastAction == ACTIONS.ACTION_RIGHT
//				&& currentState == STATES.ORIENTE_PARED_ABAJO) {
//			this.reward = 50;
//		}
//		if (lastState == STATES.ORIENTE_PARED_ABAJO && lastAction == ACTIONS.ACTION_RIGHT
//				&& currentState == STATES.ORIENTE_PARED_ABAJO_DERECHA) {
//			this.reward = 50;
//		}
//
//		if (lastState == STATES.ORIENTE_PARED_ABAJO_DERECHA && lastAction == ACTIONS.ACTION_UP
//				&& currentState == STATES.ORIENTE_PARED_DERECHA) {
//			this.reward = 50;
//		}
//
//		if (lastState == STATES.ORIENTE_PARED_DERECHA && lastAction == ACTIONS.ACTION_UP
//				&& currentState == STATES.ORIENTE_PARED_DERECHA) {
//			this.reward = 50;
//		}
//
//		if (lastState == STATES.ORIENTE_PARED_DERECHA && lastAction == ACTIONS.ACTION_UP
//				&& currentState == STATES.ORIENTE_PARED_DERECHA_ARRIBA) {
//			this.reward = 50;
//		}
//
//		if (lastState == STATES.ORIENTE_PARED_DERECHA_ARRIBA && lastAction == ACTIONS.ACTION_LEFT
//				&& currentState == STATES.ORIENTE_PARED_ARRIBA) {
//			this.reward = 50;
//		}
//
//		if (lastState == STATES.ORIENTE_PARED_ARRIBA && lastAction == ACTIONS.ACTION_LEFT
//				&& currentState == STATES.ORIENTE_PARED_ARRIBA) {
//			this.reward = 50;
//		}
//
//		if (lastState == STATES.ORIENTE_PARED_ARRIBA && lastAction == ACTIONS.ACTION_LEFT
//				&& currentState == STATES.ORIENTE_SIN_PARED_ABAJO) {
//			this.reward = 50;
//		}
//
//		if (lastState == STATES.ORIENTE_SIN_PARED_ABAJO && lastAction == ACTIONS.ACTION_UP
//				&& currentState == STATES.ORIENTE_PARED_DERECHA) {
//			this.reward = 50;
//		}
//
//		if (lastState == STATES.ORIENTE_PARED_DERECHA && lastAction == ACTIONS.ACTION_UP
//				&& currentState == STATES.ORIENTE_SIN_PARED_ABAJO) {
//			this.reward = 50;
//		}
//
//		if (lastState == STATES.ORIENTE_SIN_PARED_ABAJO && lastAction == ACTIONS.ACTION_RIGHT
//				&& currentState == STATES.ORIENTE_PARED_ABAJO) {
//			this.reward = 50;
//		}
//
//		if (lastState == STATES.ORIENTE_PARED_ABAJO && lastAction == ACTIONS.ACTION_DOWN
//				&& currentState == STATES.ORIENTE_SIN_PARED_ABAJO) {
//			this.reward = 50;
//		}

		return reward;

	}

	private ArrayList<STATES> getStates() {
		// CAMINODERECHA, CAMINOABAJO, CAMINOARRIBA, CAMINOATRAS
		return new ArrayList<STATES>(Arrays.asList(STATES.ORIENTE_PARED_IZQUIERDA, STATES.ORIENTE_PARED_IZQUIERDA_ABAJO,
				STATES.ORIENTE_PARED_ABAJO, STATES.ORIENTE_PARED_ABAJO_DERECHA, STATES.ORIENTE_PARED_DERECHA,
				STATES.ORIENTE_PARED_DERECHA_ARRIBA, STATES.ORIENTE_PARED_ARRIBA, STATES.ORIENTE_PARED_ARRIBA_IZQUIERDA,
				STATES.ORIENTE_PARED_ARRIBA_ABAJO, STATES.ORIENTE_PARED_DIAGONAL_ARRIBA_IZQUIERDA,
				STATES.ORIENTE_PARED_DIAGONAL_ARRIBA_DERECHA, STATES.ORIENTE_PARED_DIAGONAL_ABAJO_IZQUIERDA,
				STATES.ORIENTE_PARED_DIAGONAL_ABAJO_DERECHA));
	}

	private ArrayList<ACTIONS> getActions() {
		return new ArrayList<ACTIONS>(
				Arrays.asList(ACTIONS.ACTION_UP, ACTIONS.ACTION_DOWN, ACTIONS.ACTION_LEFT, ACTIONS.ACTION_RIGHT));
	}

//	public void generaArbol() {
//
//		// Nodos de decision - Preguntas
//		// Nodos de decision - Preguntas
//		TIENES_MURO = new TIENES_MURO();
//		TIENES_MURO_ARRIBA = new TIENES_MURO_ARRIBA();
//		TIENES_MURO_ABAJO = new TIENES_MURO_ABAJO();
//		TIENES_MURO_IZQUIERDA = new TIENES_MURO_IZQUIERDA();
//		TIENES_MURO_DERECHA = new TIENES_MURO_DERECHA();
//
//		// Nodos hojas - Estados
//		ORIENTE_CON_PARED_IZQUIERDA = new ORIENTE_CON_PARED_IZQUIERDA(STATES.ORIENTE_CON_PARED_IZQUIERDA);
//		ORIENTE_CON_PARED_IZQUIERDA_ABAJO = new ORIENTE_CON_PARED_IZQUIERDA_ABAJO(
//				STATES.ORIENTE_CON_PARED_IZQUIERDA_ABAJO);
//		ORIENTE_CON_PARED_ABAJO = new ORIENTE_CON_PARED_ABAJO(STATES.ORIENTE_CON_PARED_ABAJO);
//		ORIENTE_CON_PARED_ABAJO_DERECHA = new ORIENTE_CON_PARED_ABAJO_DERECHA(STATES.ORIENTE_CON_PARED_ABAJO_DERECHA);
//		ORIENTE_CON_PARED_DERECHA = new ORIENTE_CON_PARED_DERECHA(STATES.ORIENTE_CON_PARED_DERECHA);
//		ORIENTE_CON_PARED_DERECHA_ARRIBA = new ORIENTE_CON_PARED_DERECHA_ARRIBA(
//				STATES.ORIENTE_CON_PARED_DERECHA_ARRIBA);
//		ORIENTE_CON_PARED_ARRIBA = new ORIENTE_CON_PARED_ARRIBA(STATES.ORIENTE_CON_PARED_ARRIBA);
//		ORIENTE_SIN_PARED = new ORIENTE_SIN_PARED(STATES.ORIENTE_SIN_PARED);
//
//		// --- CREAMOS EL ARBOL ---
//
//		// Asignamos la raiz
//		this.raiz = TIENES_MURO;
//
//		// Tienes muro alrededor?
//		TIENES_MURO.setYes(TIENES_MURO_IZQUIERDA);
//		
//		// Tienes muro a la izquierda?
//		TIENES_MURO_IZQUIERDA.setYes(TIENES_MURO_ABAJO);
//		
//		// Tienes muro abajo?
//		TIENES_MURO_ABAJO.setYes(TIENES_MURO_DERECHA);
//		
//	}

	private boolean tienesMuro() {
		Casilla avatar = this.mapa.getAvatar();

		if (this.mapa.getNodo(avatar.getX(), avatar.getY() + 1).getEstado().equals(MURO)
				|| this.mapa.getNodo(avatar.getX(), avatar.getY() - 1).getEstado().equals(MURO)
				|| this.mapa.getNodo(avatar.getX() - 1, avatar.getY()).getEstado().equals(MURO)
				|| this.mapa.getNodo(avatar.getX() + 1, avatar.getY()).getEstado().equals(MURO)) {
			return true;
		}
		return false;
	}

	private boolean tienesMuroArriba() {
		Casilla avatar = this.mapa.getAvatar();

		if (this.mapa.getNodo(avatar.getX(), avatar.getY() - 1).getEstado().equals(MURO)) {
			return true;
		}
		return false;
	}

	private boolean tienesMuroAbajo() {
		Casilla avatar = this.mapa.getAvatar();

		if (this.mapa.getNodo(avatar.getX(), avatar.getY() + 1).getEstado().equals(MURO)) {
			return true;
		}

		return false;
	}

	private boolean tienesMuroIzquierda() {
		Casilla avatar = this.mapa.getAvatar();

		if (this.mapa.getNodo(avatar.getX() - 1, avatar.getY()).getEstado().equals(MURO)) {
			return true;
		}
		return false;
	}

	private boolean tienesMuroDerecha() {
		Casilla avatar = this.mapa.getAvatar();

		if (this.mapa.getNodo(avatar.getX() + 1, avatar.getY()).getEstado().equals(MURO)) {
			return true;
		}
		return false;
	}

	private boolean tienesDiagonalArribaDerecha() {
		Casilla avatar = this.mapa.getAvatar();

		if (this.mapa.getNodo(avatar.getX() + 1, avatar.getY() - 1).getEstado().equals(MURO)) {
			return true;
		}
		return false;
	}

	private boolean tienesDiagonalArribaIzquierda() {
		Casilla avatar = this.mapa.getAvatar();

		if (this.mapa.getNodo(avatar.getX() - 1, avatar.getY() - 1).getEstado().equals(MURO)) {
			return true;
		}
		return false;
	}

	private boolean tienesDiagonalAbajoDerecha() {
		Casilla avatar = this.mapa.getAvatar();

		if (this.mapa.getNodo(avatar.getX() + 1, avatar.getY() + 1).getEstado().equals(MURO)) {
			return true;
		}
		return false;
	}

	private boolean tienesDiagonalAbajoIzquierda() {
		Casilla avatar = this.mapa.getAvatar();

		if (this.mapa.getNodo(avatar.getX() - 1, avatar.getY() + 1).getEstado().equals(MURO)) {
			return true;
		}
		return false;
	}

	public void writeTable(String path) {
		qlearning.writeTable(path);
	}

	public void readTable(String path) {
		qlearning.readTable(path);
	}

}
