/**
 * 
 */
package uhu.carlosgarcia642alu.maquina;

import java.util.ArrayList;

import ontology.Types.ACTIONS;
import uhu.carlosgarcia642alu.Cerebro;

/**
 * @author LiTTo
 *
 */
public abstract class Estado implements IEstado {

	// =============================================================================
	// VARIABLES
	// =============================================================================

	private ArrayList<Transicion> transiciones;

	// =============================================================================
	// GETs y SETs
	// =============================================================================

	public void setTransiciones(ArrayList<Transicion> t) {
		this.transiciones = t;
	}

	// =============================================================================
	// SOBRECARGAS
	// =============================================================================

	@Override
	public ACTIONS getAccion(Cerebro c) {
		return null;
	}

	@Override
	public ACTIONS getAccionEntrada(Cerebro c) {
		return null;
	}

	@Override
	public ACTIONS getAccionSalida(Cerebro c) {
		return null;
	}

	@Override
	public ArrayList<Transicion> getTransiciones() {
		return this.transiciones;
	}

}
