
package uhu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import core.game.StateObservation;
import core.player.AbstractPlayer;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

/**
 * @author Carlos Garcia Silva
 * @author Daniel Perez Rodriguez
 *
 */
public class Agent extends AbstractPlayer {

	private Cerebro c;

	// =============================================================================
	// CONSTRUCTORES
	// =============================================================================

	/**
	 * Constructor publico con observacion del estado y con tiempo.
	 * 
	 * @param percepcion   Observacion del estado actual.
	 * @param elapsedTimer Temporizador para la creacion del controlador.
	 */
	public Agent(StateObservation percepcion, ElapsedCpuTimer elapsedTimer) {
		this.c = new Cerebro(percepcion);
	}
	
	// =============================================================================
	// METODOS
	// =============================================================================

	/**
	 * Devuelve una accion. Esta funcion es llamada en cada paso del juego para que
	 * devuelva una accion que debe realizar el jugador.
	 * 
	 * @param percepcion   Observacion del estado actual.
	 * @param elapsedTimer Temporizador cuando vence la accion devuelta.
	 * @return Una accion para el estado actual.
	 */
	public ACTIONS act(StateObservation percepcion, ElapsedCpuTimer elapsedTimer) {

		c.percibe(percepcion);
		ACTIONS accion = c.entrenar(percepcion);
//		ACTIONS accion = c.pensar(percepcion);

		return accion;
	}

	@Override
	public void result(StateObservation stateObs, ElapsedCpuTimer elapsedCpuTimer) {
		c.writeTable("QTABLE.txt");
		c.saveTimer();
//		System.out.println("PUNTUACION: "+ c.getGR());
	}
	

}
