/**
 * 
 */
package uhu;

import java.awt.Dimension;
import java.util.ArrayList;

import core.game.StateObservation;
import ontology.Types.ACTIONS;
import uhu.grid.*;
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

		this.mapa = new Mapa(dim.width / bloque, dim.height / bloque, bloque,
				percepcion);
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

	/**
	 * @param percepcion Observacion del estado actual.
	 */
	public void analizarMapa(StateObservation percepcion) {
		this.mapa.actualiza(percepcion,Visualizaciones.BASICO);
		
	}

	/**
	 * Recorre el arbol de decision y devuelve una accion a realizar.
	 * 
	 * @return Accion a realizar tras recorrer los nodos el arbol.
	 */
	public ACTIONS pensar(Integer lastState) {

		return null;
	}
	
	public ACTIONS entrenar() {
		
		return null;
	}

}
