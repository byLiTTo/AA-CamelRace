/**
 * 
 */
package uhu.carlosgarcia642alu.juego89.transiciones;

import uhu.carlosgarcia642alu.Cerebro;
import uhu.carlosgarcia642alu.maquina.Transicion;

/**
 * Transicion, se activa si hay ladrones en el mapa.
 * 
 * @author LiTTo
 *
 */
public class HayLadrones extends Transicion {

	// =============================================================================
	// SOBRECARGAS
	// =============================================================================

	@Override
	public boolean seActiva(Cerebro c) {
		if (c.getMapa().getLadrones().size() > 0) {
			return true;
		} else {
			return false;
		}
	}

}
