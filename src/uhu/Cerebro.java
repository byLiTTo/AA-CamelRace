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

	private AcercandoseMeta acercandose;

	private Caminando caminando;
	private Bloqueado bloqueado;

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

		this.currentState = STATES.CAMINANDO;
		this.lastState = STATES.CAMINANDO;

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
		this.mapa.actualiza(percepcion, Visualizaciones.BASICO);
	}

	private void actualizaState(StateObservation percepcion) {
		this.lastState = this.currentState;
		this.currentState = this.raiz.decidir(this);
		if (currentState == null) {
			System.out.println("Soy mongolito");
		} else {
			System.out.println("Quizas no sea tan mongolito");
		}
	}

	/**
	 * Recorre el arbol de decision y devuelve una accion a realizar.
	 * 
	 * @return Accion a realizar tras recorrer los nodos el arbol.
	 */
	public ACTIONS pensar(StateObservation percepcion) {
		double reward = getReward(lastState, percepcion.getAvatarLastAction(), currentState);
		this.qlearning.update(lastState, percepcion.getAvatarLastAction(), currentState, reward);
		System.out.println("Hola don jose");
		return this.qlearning.nextAction(currentState);
	}

	public ACTIONS entrenar(StateObservation percepcion) {

		return null;
	}

	// =============================================================================
	// AUXILIARES
	// =============================================================================

	private double getReward(STATES lastState, ACTIONS lastAction, STATES currentState) {
		double reward = 0;
		switch (lastState) {
		case CAMINANDO:
			switch (currentState) {
			case CAMINANDO:
				reward += 30;
				break;
			case BLOQUEADO:
				reward -= 20;
			}
			break;
		case BLOQUEADO:
			switch (currentState) {
			case CAMINANDO:
				reward += 10;
				break;
			case BLOQUEADO:
				reward -= 40;
			}
			break;
		}
		return reward;
	}

	private ArrayList<STATES> getStates() {
		return new ArrayList<STATES>(Arrays.asList(STATES.CAMINANDO, STATES.BLOQUEADO));
	}

	private ArrayList<ACTIONS> getActions() {
		return new ArrayList<ACTIONS>(
				Arrays.asList(ACTIONS.ACTION_UP, ACTIONS.ACTION_DOWN, ACTIONS.ACTION_LEFT, ACTIONS.ACTION_RIGHT));
	}

	public void generaArbol() {

		// Inializacion de nodos preguntas
		this.acercandose = new AcercandoseMeta();

		// Inicializacion de nodos hoja
		this.caminando = new Caminando(STATES.CAMINANDO);
		this.bloqueado = new Bloqueado(STATES.BLOQUEADO);

		// Creacion del arbol--------------------------------------
		this.raiz = this.acercandose;

		// ¿Estoy mas cerca?
		this.acercandose.setYes(caminando);
		this.acercandose.setNo(bloqueado);

	}

}
