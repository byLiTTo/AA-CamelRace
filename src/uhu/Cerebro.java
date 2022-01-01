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
 * Clase que se encarga de analizar el entorno en el que se encuentra el agente y de seleccionar que acción tiene que realizar el mismo
 * @author Carlos Garcia Silva
 * @author Daniel Perez Rodriguez
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
	private TIENES_MURO_ARRIBA tienesMuroArriba_Norte;
	
	// Oeste
	private TIENES_MURO_ARRIBA tienesMuroArriba_Oeste;
	private TIENES_MURO_IZQUIERDA tienesMuroIzquierda_Oeste;
	
	// MUROS ALREDEDOR
	private TIENES_MURO_ALREDEDOR tienesMuroAlrededor;
	
	// Nodos hojas - Estados
	private ESTADO_NORTE estadoNorte;
	private ESTADO_SUR estadoSur;
	private ESTADO_ESTE estadoEste;
	private ESTADO_OESTE estadoOeste;

	private ACTIONS lastAction;
	private ORIENTACION orientacion;

	private double reward;
	private double globalReward;

	// =============================================================================
	// CONSTRUCTORES
	// =============================================================================

	/**
	 * Constructor de la clase cerebro que crea un mapa y genera el arbol de
	 * decision.
	 * @param percepcion observacion del estado actual.
	 * @param timer tiempo actual
	 */
	public Cerebro(StateObservation percepcion) {
		Dimension dim = percepcion.getWorldDimension();
		int bloque = percepcion.getBlockSize();

		this.mapa = new Mapa(dim.width / bloque, dim.height / bloque, bloque, percepcion);

		if (this.mapa.getAvatar().getX() - this.mapa.getColumnaPortal() < 0) {
			this.lastAction = ACTIONS.ACTION_DOWN;
			this.currentState = STATES.HACIA_SUR;
			this.lastState = STATES.HACIA_SUR;
			this.orientacion = ORIENTACION.SUR;

		} else {
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
	 * @return Mapa generado.
	 */
	public Mapa getMapa() {
		return this.mapa;
	}
	
	/**
	 * Devuelve la última acción realizada
	 * @return Devuelve una acción
	 */
	public ACTIONS getLastAction() {
		return this.lastAction;
	}
	
	/**
	 * Devuelve la orientación del agente
	 * @return Devuelve una orientación
	 */
	public ORIENTACION getOrientacion() {
		return this.orientacion;
	}
	
	/**
	 * Devuelve el timer actual
	 * @return Devuelve el número de ticks jugados por el agente
	 */
	public int getTimer() {
		return this.qlearning.getTimer();
	}

	// =============================================================================
	// METODOS
	// =============================================================================

	/**
	 * Analiza el mapa y actualiza el estado
	 * @param percepcion Percepción del juego
	 */
	public void percibe(StateObservation percepcion) {
		analizarMapa(percepcion);
		actualizaState(percepcion);
	}

	/**
	 * Analiza el mapa del juego
	 * @param percepcion Observacion del estado actual.
	 */
	private void analizarMapa(StateObservation percepcion) {
		this.mapa.actualiza(percepcion, Visualizaciones.NADA);
	}

	/**
	 * Actualiza el estado
	 * @param percepcion Percepción del juego
	 */
	private void actualizaState(StateObservation percepcion) {
		this.lastState = this.currentState;
		this.currentState = this.raiz.decidir(this);
	}

	/**
	 * Recorre el arbol de decision y devuelve una accion a realizar.
	 * @return Accion a realizar tras recorrer los nodos el arbol.
	 */
	public ACTIONS pensar(StateObservation percepcion) {
		this.lastAction = this.qlearning.nextOnlyOneBestAction(currentState);
		
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

	/**
	 * Entrena el agente para que aprenda a jugar a través del algoritmo Q-learning
	 * @param percepcion Percepción del juego
	 * @return Devuelve una acción
	 */
	public ACTIONS entrenar(StateObservation percepcion) {
		double reward = getReward(lastState, lastAction, currentState);
		this.qlearning.update(lastState, lastAction, currentState, reward);
		this.lastAction = this.qlearning.nextAction(currentState);

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

	/**
	 * Devuelve la recomensa total obtenida por el agente
	 * @return Devuelve la recompensa total
	 */
	public double getGR() {
		return this.globalReward;
	}

	// =============================================================================
	// AUXILIARES
	// =============================================================================

	/**
	 * Calcula la recompensa para la última acción realizada
	 * @param lastState Estado anterior
	 * @param lastAction Última acción realizada
	 * @param currentState Estado actual
	 * @return Devuelve la recompensa calculada
	 */
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
		
		return reward;

	}
	
	/**
	 * Comprueba si el agente se ha movido hacia el norte
	 * @param a Posición Y de la última casilla
	 * @param b Posición Y de la casilla actual
	 * @return Devuelve true si el agente se mueve hacia el norte. False en caso contrario
	 */
	private boolean checkNorthMovement(int a, int b) {
		if((a-b) > 0)
			return true;
		else
			return false;
	}
	
	/**
	 * Comprueba si el agente se ha movido hacia el sur
	 * @param a Posición Y de la última casilla
	 * @param b Posición Y de la casilla actual
	 * @return Devuelve true si el agente se mueve hacia el sur. False en caso contrario
	 */
	private boolean checkSouthMovement(int a, int b) {
		if((a-b) < 0)
			return true;
		else
			return false;
	}
	
	/**
	 * Comprueba si el agente se ha movido hacia el este
	 * @param a Posición X de la última casilla
	 * @param b Posición X de la casilla actual
	 * @return Devuelve true si el agente se mueve hacia el este. False en caso contrario
	 */
	private boolean checkEastMovement(int a, int b) {
		if((b-a) > 0)
			return true;
		else
			return false;
	}
	
	/**
	 * Comprueba si el agente se ha movido hacia el oeste
	 * @param a Posición X de la última casilla
	 * @param b Posición X de la casilla actual
	 * @return Devuelve true si el agente se mueve hacia el oeste. False en caso contrario
	 */
	private boolean checkWestMovement(int a, int b) {
		if((b-a) < 0)
			return true;
		else
			return false;
	}

	/**
	 * Devuelve los estados en los que se puede encontrar el agente
	 * @return Devuelve un ArrayList con los estados
	 */
	private ArrayList<STATES> getStates() {
		// CAMINODERECHA, CAMINOABAJO, CAMINOARRIBA, CAMINOATRAS
		return new ArrayList<STATES>(Arrays.asList(STATES.HACIA_NORTE, STATES.HACIA_SUR, STATES.HACIA_ESTE, STATES.HACIA_OESTE));
	}

	/**
	 * Devuelve las acciones que puede realizar al agente
	 * @return Devuelve un ArrayList con las acciones
	 */
	private ArrayList<ACTIONS> getActions() {
		return new ArrayList<ACTIONS>(
				Arrays.asList(ACTIONS.ACTION_UP, ACTIONS.ACTION_DOWN, ACTIONS.ACTION_LEFT, ACTIONS.ACTION_RIGHT));
	}
	
	/**
	 * Crea el arbol de decisión del agente 
	 */
	private void generaArbol() {
		
		// CREAMOS LOS NODOS
		// DECISION
		
		this.tienesMuroAlrededor = new TIENES_MURO_ALREDEDOR();
		
		this.orientacion_Sur = new ORIENTACION_SUR();
			this.tienesMuroIzquierda_Sur = new TIENES_MURO_IZQUIERDA();
			this.tienesMuroAbajo_Sur = new TIENES_MURO_ABAJO();
		
		this.orientacion_Este = new ORIENTACION_ESTE();
			this.tienesMuroAbajo_Este = new TIENES_MURO_ABAJO();
			this.tienesMuroDerecha_Este = new TIENES_MURO_DERECHA();
		
		this.orientacion_Norte = new ORIENTACION_NORTE();
			this.tienesMuroDerecha_Norte = new TIENES_MURO_DERECHA();
			this.tienesMuroArriba_Norte = new TIENES_MURO_ARRIBA();
		
			this.tienesMuroArriba_Oeste = new TIENES_MURO_ARRIBA();
			this.tienesMuroIzquierda_Oeste = new TIENES_MURO_IZQUIERDA();
		
		// HOJAS
		this.estadoNorte = new ESTADO_NORTE(STATES.HACIA_NORTE);
		this.estadoSur = new ESTADO_SUR(STATES.HACIA_SUR);
		this.estadoEste = new ESTADO_ESTE(STATES.HACIA_ESTE);
		this.estadoOeste = new ESTADO_OESTE(STATES.HACIA_OESTE);

		// PREGUNTAS - ASIGNAMOS EL VALOR DE CADA NODO
		// Alrededor
		this.tienesMuroAlrededor.setYes(this.orientacion_Sur);
		this.tienesMuroAlrededor.setNo(this.estadoSur);
		
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
			this.tienesMuroDerecha_Norte.setYes(this.tienesMuroArriba_Norte);
			this.tienesMuroDerecha_Norte.setNo(this.estadoEste);
				this.tienesMuroArriba_Norte.setYes(this.estadoOeste);
				this.tienesMuroArriba_Norte.setNo(this.estadoNorte);
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
		this.raiz = this.tienesMuroAlrededor;
		
	}

	/**
	 * Guarda la tabla-Q del agente en un fichero
	 * @param path Ruta donde guardar la tabla
	 */
	public void writeTable(String path) {
		qlearning.writeTable(path);
	}

	/**
	 * Lee la tabla-Q guardada en un fichero
	 * @param path Ruta donde leer la tabla
	 */
	public void readTable(String path) {
		qlearning.readTable(path);
	}
	
	/**
	 * Guarda el valor del timer en un fichero
	 */
	public void saveTimer() {
		this.qlearning.saveTimer();
	}
}
