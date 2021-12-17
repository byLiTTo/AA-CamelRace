/**
 * 
 */
package uhu.carlosgarcia642alu.juego89.transiciones;

import java.util.ArrayList;

import uhu.carlosgarcia642alu.Cerebro;
import uhu.carlosgarcia642alu.Constantes;
import uhu.carlosgarcia642alu.grid.Casilla;
import uhu.carlosgarcia642alu.maquina.Transicion;

/**
 * Transicion, se activa si el civil mas cercano que se encuentra en caida,
 * tiene alguna nube debajo o esta cayendo al vacio.
 * 
 * @author LiTTo
 *
 */
public class NubeDebajo extends Transicion {

	// =============================================================================
	// SOBRECARGAS
	// =============================================================================

	@Override
	public boolean seActiva(Cerebro c) {
		ArrayList<Casilla> cayendo = c.getMapa().getCayendo();
		if (cayendo.size() > 0) {
			int x = cayendo.get(0).getX();
			int y = cayendo.get(0).getY();
			int alto = c.getMapa().getAlto();
			boolean existe = false;
			int pos = y + 1;

			while (pos < alto - 1 && existe == false) {
				if (c.getMapa().getNodo(x, pos).getEstado() == Constantes.NUBE) {
					existe = true;
				} else {
					pos++;
				}
			}

			if (existe) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
