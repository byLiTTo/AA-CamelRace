
package uhu;

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

		Cerebro c = new Cerebro(percepcion);
		c.analizarMapa(percepcion);
		ACTIONS accion = c.pensar();

		return accion;
	}
}
