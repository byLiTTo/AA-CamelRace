/**
 * 
 */
package uhu.carlosgarcia642alu.juego89.estados;

import java.util.ArrayList;

import ontology.Types.ACTIONS;
import uhu.carlosgarcia642alu.Cerebro;
import uhu.carlosgarcia642alu.aestrella.AEstrella;
import uhu.carlosgarcia642alu.grid.Casilla;
import uhu.carlosgarcia642alu.grid.Mapa;
import uhu.carlosgarcia642alu.maquina.Estado;

/**
 * @author LiTTo
 *
 */
public class Rescatar extends Estado {

	// =============================================================================
	// METODOS
	// =============================================================================

	private ACTIONS realizaAccion(Cerebro c) {
		Mapa m = c.getMapa();
		ArrayList<Casilla> cayendo = m.getCayendo();
		if (cayendo.size() > 0) {
			AEstrella b = new AEstrella(m);
			return b.calculaCamino(m, m.getAvatar(), cayendo.get(0));
		} else {
			return ACTIONS.ACTION_NIL;
		}
	}

	// =============================================================================
	// SOBRECARGAS
	// =============================================================================

	@Override
	public ACTIONS getAccion(Cerebro c) {
		return this.realizaAccion(c);
	}

	@Override
	public ACTIONS getAccionEntrada(Cerebro c) {
		return this.realizaAccion(c);
	}

	@Override
	public ACTIONS getAccionSalida(Cerebro c) {
		return this.realizaAccion(c);
	}

}
