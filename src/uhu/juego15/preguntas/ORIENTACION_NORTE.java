package uhu.juego15.preguntas;

import uhu.Cerebro;
import uhu.Constantes.STATES;
import uhu.arbol.NodoLogico;
import uhu.grid.Casilla;

import static uhu.Constantes.*;

public class ORIENTACION_NORTE extends NodoLogico {
	@Override
	public STATES decidir(Cerebro c) {

		if (c.getOrientacion().equals(ORIENTACION.NORTE)) {
			this.setValor(true);
		} else {
			this.setValor(false);
		}
		return super.decidir(c);
	}
}
