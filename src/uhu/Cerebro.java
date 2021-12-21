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
	private PuedeAvanzar puedeAvanzar;
	private MuroArriba muroArriba;
	private EstoyRetrocediendo estoyRetrocediendo;
	private Bloqueandose bloqueado;

	// Nodos hojas - Estados
	private Avanzando avanzando;
	private Retrocediendo retrocediendo;
	private Subiendo subiendo;
	private Bajando bajando;

	private ACTIONS lastAction = ACTIONS.ACTION_RIGHT;

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
		this.qlearning = new QLearning(getStates(), getActions(), new String("QTABLE.txt"));

		this.currentState = STATES.AVANZANDO;
		this.lastState = STATES.AVANZANDO;

		this.reward = 0;
		this.globalReward = 0;

		generaArbol();
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

		this.lastState = this.currentState;
		this.currentState = this.raiz.decidir(this);

//		System.out.println("\nDespues de actualizar");
//		System.out.println("lastState: " + lastState);
//		System.out.println("currentState: " + currentState);

	}

	/**
	 * Recorre el arbol de decision y devuelve una accion a realizar.
	 * 
	 * @return Accion a realizar tras recorrer los nodos el arbol.
	 */
	public ACTIONS pensar(StateObservation percepcion) {
		this.lastAction = this.qlearning.nextOnlyOneBestAction(currentState);

		System.out.println("\nEstado: " + this.currentState);

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
		this.reward = 0;
//		switch (lastState) {
//		case CAMINODERECHA:
//			switch (currentState) {
//			case CAMINODERECHA:
//				reward = 10;
//				break;
//			case CAMINOABAJO:
//				reward = -15;
//				break;
//			case CAMINOARRIBA:
//				reward = -15;
//				break;
//			case CAMINOATRAS:
//				reward = -15;
//				break;
//			}
//			break;
//		case CAMINOABAJO:
//			switch (currentState) {
//			case CAMINODERECHA:
//				reward = -15;
//				break;
//			case CAMINOABAJO:
//				reward = 10;
//				break;
//			case CAMINOARRIBA:
//				reward = -15;
//				break;
//			case CAMINOATRAS:
//				reward = -15;
//				break;
//			}
//		case CAMINOARRIBA:
//			switch (currentState) {
//			case CAMINODERECHA:
//				reward = -15;
//				break;
//			case CAMINOABAJO:
//				reward = -15;
//				break;
//			case CAMINOARRIBA:
//				reward = 10;
//				break;
//			case CAMINOATRAS:
//				reward = -15;
//				break;
//			}
//		case CAMINOATRAS:
//			switch (currentState) {
//			case CAMINODERECHA:
//				reward = -15;
//				break;
//			case CAMINOABAJO:
//				reward = -15;
//				break;
//			case CAMINOARRIBA:
//				reward = -15;
//				break;
//			case CAMINOATRAS:
//				reward = 10;
//				break;
//			}
//		}
//		this.globalReward += reward;
////		return reward;
//		return globalReward;

		Casilla ahora = mapa.getAvatar();
		Casilla antes = mapa.getLastAvatar();

		int columna = mapa.getColumnaPortal();

		double distanciaAhora = Math.abs(ahora.getX() - columna);
		double distanciaAntes = Math.abs(antes.getX() - columna);

		if (distanciaAhora < distanciaAntes) {
			return 50;
		} else if (distanciaAhora == distanciaAntes) {

			return -20;
		} else {
			return -50;
		}

	}

	private ArrayList<STATES> getStates() {
		// CAMINODERECHA, CAMINOABAJO, CAMINOARRIBA, CAMINOATRAS
		return new ArrayList<STATES>(Arrays.asList(STATES.AVANZANDO, STATES.RETROCEDIENDO, STATES.BLOQUEADO,
				STATES.SUBIENDO, STATES.BAJANDO));
	}

	private ArrayList<ACTIONS> getActions() {
		return new ArrayList<ACTIONS>(
				Arrays.asList(ACTIONS.ACTION_UP, ACTIONS.ACTION_DOWN, ACTIONS.ACTION_LEFT, ACTIONS.ACTION_RIGHT));
	}

	public void generaArbol() {

		// Nodos de decision - Preguntas
		puedeAvanzar = new PuedeAvanzar();
		muroArriba = new MuroArriba();
		estoyRetrocediendo = new EstoyRetrocediendo();
		bloqueado = new Bloqueandose();

		// Nodos hojas - Estados
		avanzando = new Avanzando(STATES.AVANZANDO);
		retrocediendo = new Retrocediendo(STATES.RETROCEDIENDO);
		subiendo = new Subiendo(STATES.SUBIENDO);
		bajando = new Bajando(STATES.BAJANDO);

		// --- CREAMOS EL ARBOL ---

		// Asignamos la raiz
		this.raiz = this.retrocediendo;

		// Retrocediendo?
		this.estoyRetrocediendo.setYes(bloqueado);
		this.estoyRetrocediendo.setNo(puedeAvanzar);

		// Puedo avanzar?
		this.puedeAvanzar.setYes(avanzando);
		this.puedeAvanzar.setNo(bloqueado);

		// Estoy bloqueado?
		this.bloqueado.setYes(retrocediendo);
		this.bloqueado.setNo(muroArriba);

		// Tengo muro arriba?
		this.muroArriba.setYes(bajando);
		this.muroArriba.setNo(subiendo);
	}

	public void writeTable(String path) {
		qlearning.writeTable(path);
	}

	public void readTable(String path) {
		qlearning.readTable(path);
	}

}
