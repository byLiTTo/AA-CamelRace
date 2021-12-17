/**
 * 
 */
package uhu.carlosgarcia642alu.juego89.estados;

import ontology.Types.ACTIONS;
import uhu.carlosgarcia642alu.Cerebro;
import uhu.carlosgarcia642alu.Constantes;
import uhu.carlosgarcia642alu.aestrella.AEstrella;
import uhu.carlosgarcia642alu.grid.Casilla;
import uhu.carlosgarcia642alu.grid.Mapa;
import uhu.carlosgarcia642alu.maquina.Estado;

/**
 * @author LiTTo
 *
 */
public class Encarcelar extends Estado {

	// =============================================================================
	// METODOS
	// =============================================================================

	private ACTIONS realizaAccion(Cerebro c) {
		Mapa m = c.getMapa();
		AEstrella busqueda = new AEstrella(m);
		m.setNodo(new Casilla(m.getJail().getX(), m.getJail().getY(),
				Constantes.JAIL));
		return busqueda.calculaCamino(m, m.getAvatar(), m.getJail());
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
