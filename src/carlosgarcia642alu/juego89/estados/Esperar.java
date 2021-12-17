/**
 * 
 */
package uhu.carlosgarcia642alu.juego89.estados;

import ontology.Types.ACTIONS;
import uhu.carlosgarcia642alu.Cerebro;
import uhu.carlosgarcia642alu.aestrella.AEstrella;
import uhu.carlosgarcia642alu.aestrella.Heuristica;
import uhu.carlosgarcia642alu.grid.Casilla;
import uhu.carlosgarcia642alu.grid.Mapa;
import uhu.carlosgarcia642alu.maquina.Estado;

/**
 * @author LiTTo
 *
 */
public class Esperar extends Estado {

	// =============================================================================
	// METODOS
	// =============================================================================

	private ACTIONS realizaAccion(Cerebro c) {
		Mapa m = c.getMapa();
		Casilla avatar = m.getAvatar();
		Casilla spawn_izquierda = m.getSpawnIzquierda();
		Casilla spawn_derecha = m.getSpawnDerecha();

		AEstrella busqueda = new AEstrella(m);

		float distancia_izquierda = Heuristica.calcularDistancia(avatar,
				spawn_izquierda);
		float distancia_derecha = Heuristica.calcularDistancia(avatar,
				spawn_derecha);

		if (distancia_izquierda <= distancia_derecha) {
			return busqueda.calculaCamino(m, avatar, spawn_izquierda);
		} else {
			return busqueda.calculaCamino(m, avatar, spawn_derecha);
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
