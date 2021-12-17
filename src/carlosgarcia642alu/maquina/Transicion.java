/**
 * 
 */
package uhu.carlosgarcia642alu.maquina;

import uhu.carlosgarcia642alu.Cerebro;

/**
 * @author LiTTo
 *
 */
public abstract class Transicion implements ITransicion {

	// =============================================================================
	// VARIABLES
	// =============================================================================

	private Estado destino;

	// =============================================================================
	// GETs y SETs
	// =============================================================================

	public void setEstadoDestino(Estado d) {
		this.destino = d;
	}

	// =============================================================================
	// SOBRECARGAS
	// =============================================================================

	@Override
	public boolean seActiva(Cerebro c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Estado getEstadoDestino() {
		return this.destino;
	}

}
