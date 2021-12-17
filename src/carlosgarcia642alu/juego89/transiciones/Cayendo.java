/**
 * 
 */
package uhu.carlosgarcia642alu.juego89.transiciones;

import uhu.carlosgarcia642alu.Cerebro;
import uhu.carlosgarcia642alu.maquina.Transicion;

/**
 * Transicion, se activa si hay civiles con estado "cayendo".
 * 
 * @author LiTTo
 *
 */
public class Cayendo extends Transicion {

	// =============================================================================
	// SOBRECARGAS
	// =============================================================================

	@Override
	public boolean seActiva(Cerebro c) {
		if (c.getMapa().getCayendo().size() > 0) {
			return true;
		} else {
			return false;
		}
	}

}
