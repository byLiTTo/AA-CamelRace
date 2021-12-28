/**
 * 
 */
package uhu;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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
	// Sur
	private ORIENTACION_SUR orientacion_Sur;
	private TIENES_MURO_IZQUIERDA tienesMuroIzquierda_Sur;
	private TIENES_MURO_ABAJO tienesMuroAbajo_Sur;

	// Este
	private ORIENTACION_ESTE orientacion_Este;
	private TIENES_MURO_ABAJO tienesMuroAbajo_Este;
	private TIENES_MURO_DERECHA tienesMuroDerecha_Este;

	// Norte
	private ORIENTACION_NORTE orientacion_Norte;
	private TIENES_MURO_DERECHA tienesMuroDerecha_Norte;
	private TIENES_MURO_ARRIBA tienesMuroArriba1_Norte;
	private TIENES_MURO_ARRIBA tienesMuroArriba2_Norte;
	
	// Oeste
	private TIENES_MURO_ARRIBA tienesMuroArriba_Oeste;
	private TIENES_MURO_IZQUIERDA tienesMuroIzquierda_Oeste;	
	
	// Nodos hojas - Estados
	private ESTADO_NORTE estadoNorte;
	private ESTADO_SUR estadoSur;
	private ESTADO_ESTE estadoEste;
	private ESTADO_OESTE estadoOeste;

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
	 * @param timer tiempo actual
	 */
	public Cerebro(StateObservation percepcion) {
		Dimension dim = percepcion.getWorldDimension();
		int bloque = percepcion.getBlockSize();

		this.mapa = new Mapa(dim.width / bloque, dim.height / bloque, bloque, percepcion);

		if (this.mapa.getAvatar().getX() - this.mapa.getColumnaPortal() < 0) {
//			this.sentido = SENTIDO.ORIENTE;
			this.lastAction = ACTIONS.ACTION_DOWN;
			this.currentState = STATES.HACIA_SUR;
			this.lastState = STATES.HACIA_SUR;
			this.orientacion = ORIENTACION.SUR;

		} else {
//			this.sentido = SENTIDO.OCCIDENTE;
			this.lastAction = ACTIONS.ACTION_UP;
			this.currentState = STATES.HACIA_NORTE;
			this.lastState = STATES.HACIA_NORTE;
			this.orientacion = ORIENTACION.NORTE;
		}

		this.qlearning = new QLearning(getStates(), getActions(), new String("QTABLE.txt"));

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
	
	public ORIENTACION getOrientacion() {
		return this.orientacion;
	}
	
	public int getTimer() {
		return this.qlearning.getTimer();
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
//		Casilla a = new Casilla(mapa.getAvatar().getX(), mapa.getAvatar().getY()+1, "$");
//		this.mapa.setNodo(a);
		
		switch(this.lastAction) {
		case ACTION_UP:
			this.orientacion = ORIENTACION.NORTE;
			break;
		case ACTION_DOWN:
			this.orientacion = ORIENTACION.SUR;
			break;		
		case ACTION_RIGHT:
			this.orientacion = ORIENTACION.ESTE;
			break;		
		case ACTION_LEFT:
			this.orientacion = ORIENTACION.OESTE;
			break;		
		}
		
		System.out.println("Orientacion: " + this.orientacion);
		
		this.mapa.visualiza();
		System.out.println("\nEstado: " + this.currentState);

		return lastAction;
	}

	public ACTIONS entrenar(StateObservation percepcion) {
		double reward = getReward(lastState, lastAction, currentState);
		this.qlearning.update(lastState, lastAction, currentState, reward);
		this.lastAction = this.qlearning.nextAction(currentState);

//		System.out.println("\nEstado: " + this.currentState);
//		System.out.println("\nRECOMPENSA: " + this.globalReward);
		
//		System.out.println("Orientacion: " + this.orientacion);
//		
//		this.mapa.visualiza();
//		System.out.println("\nEstado: " + this.currentState);

		switch(this.lastAction) {
		case ACTION_UP:
			this.orientacion = ORIENTACION.NORTE;
			break;
		case ACTION_DOWN:
			this.orientacion = ORIENTACION.SUR;
			break;		
		case ACTION_RIGHT:
			this.orientacion = ORIENTACION.ESTE;
			break;		
		case ACTION_LEFT:
			this.orientacion = ORIENTACION.OESTE;
			break;		
		}

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
		Casilla currentCasilla = this.mapa.getAvatar();
		Casilla lastCasilla = this.mapa.getLastAvatar();
		
		switch(lastState) {
		case HACIA_NORTE:
			if(this.checkNorthMovement(lastCasilla.getY(), currentCasilla.getY()))
				this.reward = 50; 
			break;
		case HACIA_SUR:
			if(this.checkSouthMovement(lastCasilla.getY(), currentCasilla.getY()))
				this.reward = 50;
			break;
		case HACIA_ESTE:
			if(this.checkEastMovement(lastCasilla.getX(), currentCasilla.getX()))
				this.reward = 50;
			break;
		case HACIA_OESTE:
			if(this.checkWestMovement(lastCasilla.getX(), currentCasilla.getX()))
				this.reward = 50;
			break;
		}
		
//		System.out.println("Orientacion: " + this.orientacion);
//		System.out.println("Estado: " + currentState);
//		System.out.println("Recompensa: " + this.reward);
//		this.mapa.visualiza();
		
		return reward;

	}
	
	private boolean checkNorthMovement(int a, int b) {
		if((a-b) > 0)
			return true;
		else
			return false;
	}
	
	private boolean checkSouthMovement(int a, int b) {
		if((a-b) < 0)
			return true;
		else
			return false;
	}
	
	private boolean checkEastMovement(int a, int b) {
		if((b-a) > 0)
			return true;
		else
			return false;
	}
	
	private boolean checkWestMovement(int a, int b) {
		if((b-a) < 0)
			return true;
		else
			return false;
	}

	private ArrayList<STATES> getStates() {
		// CAMINODERECHA, CAMINOABAJO, CAMINOARRIBA, CAMINOATRAS
		return new ArrayList<STATES>(Arrays.asList(STATES.HACIA_NORTE, STATES.HACIA_SUR, STATES.HACIA_ESTE, STATES.HACIA_OESTE));
	}

	private ArrayList<ACTIONS> getActions() {
		return new ArrayList<ACTIONS>(
				Arrays.asList(ACTIONS.ACTION_UP, ACTIONS.ACTION_DOWN, ACTIONS.ACTION_LEFT, ACTIONS.ACTION_RIGHT));
	}

	public void generaArbol() {
		
		// CREAMOS LOS NODOS
		// DECISION
		
		this.orientacion_Sur = new ORIENTACION_SUR();
			this.tienesMuroIzquierda_Sur = new TIENES_MURO_IZQUIERDA();
			this.tienesMuroAbajo_Sur = new TIENES_MURO_ABAJO();
		
		this.orientacion_Este = new ORIENTACION_ESTE();
			this.tienesMuroAbajo_Este = new TIENES_MURO_ABAJO();
			this.tienesMuroDerecha_Este = new TIENES_MURO_DERECHA();
		
		this.orientacion_Norte = new ORIENTACION_NORTE();
			this.tienesMuroDerecha_Norte = new TIENES_MURO_DERECHA();
			this.tienesMuroArriba1_Norte = new TIENES_MURO_ARRIBA();
			this.tienesMuroArriba2_Norte = new TIENES_MURO_ARRIBA();
		
			this.tienesMuroArriba_Oeste = new TIENES_MURO_ARRIBA();
			this.tienesMuroIzquierda_Oeste = new TIENES_MURO_IZQUIERDA();
		
		// HOJAS
		this.estadoNorte = new ESTADO_NORTE(STATES.HACIA_NORTE);
		this.estadoSur = new ESTADO_SUR(STATES.HACIA_SUR);
		this.estadoEste = new ESTADO_ESTE(STATES.HACIA_ESTE);
		this.estadoOeste = new ESTADO_OESTE(STATES.HACIA_OESTE);

		// PREGUNTAS - ASIGNAMOS EL VALOR DE CADA NODO
		// Sur
		this.orientacion_Sur.setYes(this.tienesMuroIzquierda_Sur);
		this.orientacion_Sur.setNo(this.orientacion_Este);
			this.tienesMuroIzquierda_Sur.setYes(this.tienesMuroAbajo_Sur);
			this.tienesMuroIzquierda_Sur.setNo(this.estadoOeste);
				this.tienesMuroAbajo_Sur.setYes(this.estadoEste);
				this.tienesMuroAbajo_Sur.setNo(this.estadoSur);
		// Este
		this.orientacion_Este.setYes(this.tienesMuroAbajo_Este);
		this.orientacion_Este.setNo(this.orientacion_Norte);
			this.tienesMuroAbajo_Este.setYes(this.tienesMuroDerecha_Este);
			this.tienesMuroAbajo_Este.setNo(this.estadoSur);
				this.tienesMuroDerecha_Este.setYes(this.estadoNorte);
				this.tienesMuroDerecha_Este.setNo(this.estadoEste);
		// Norte
		this.orientacion_Norte.setYes(this.tienesMuroDerecha_Norte);
		this.orientacion_Norte.setNo(this.tienesMuroArriba_Oeste);
			this.tienesMuroDerecha_Norte.setYes(this.tienesMuroArriba1_Norte);
			this.tienesMuroDerecha_Norte.setNo(this.estadoEste);
				this.tienesMuroArriba1_Norte.setYes(this.estadoOeste);
				this.tienesMuroArriba1_Norte.setNo(this.estadoNorte);
//					this.tienesMuroArriba2_Norte.setYes(this.estadoOeste);
//					this.tienesMuroArriba2_Norte.setNo(this.estadoEste);
		// Oeste
		this.tienesMuroArriba_Oeste.setYes(this.tienesMuroIzquierda_Oeste);
		this.tienesMuroArriba_Oeste.setNo(this.estadoNorte);
			this.tienesMuroIzquierda_Oeste.setYes(this.estadoSur);
			this.tienesMuroIzquierda_Oeste.setNo(this.estadoOeste);
				
		// HOJAS - ASIGNAMOS EL VALOR DE CADA NODO
		this.estadoNorte.setState(STATES.HACIA_NORTE);
		this.estadoSur.setState(STATES.HACIA_SUR);
		this.estadoEste.setState(STATES.HACIA_ESTE);
		this.estadoOeste.setState(STATES.HACIA_OESTE);

		// --- CREAMOS EL ARBOL ---

		// Asignamos la raiz
		this.raiz = this.orientacion_Sur;
		
	}

	public void writeTable(String path) {
		qlearning.writeTable(path);
	}

	public void readTable(String path) {
		qlearning.readTable(path);
	}
	
	public void saveTimer() {
		this.qlearning.saveTimer();
	}
}
