/**
 * 
 */
package uhu.carlosgarcia642alu.juego89.transiciones;

import uhu.carlosgarcia642alu.Cerebro;
import uhu.carlosgarcia642alu.maquina.Transicion;

/**
 * Transicion, se activa si la barra esta llena o no. La barra se llena cuando
 * se captura a 8 ladrones y no se llevan a la carcel.
 * 
 * @author LiTTo
 *
 */
public class BarraLlena extends Transicion {

	// =============================================================================
	// SOBRECARGAS
	// =============================================================================

	@Override
	public boolean seActiva(Cerebro c) {
		if (c.getMapa().getCapturados() >= 8) {
			return true;
		} else {
			return false;
		}
	}

}
