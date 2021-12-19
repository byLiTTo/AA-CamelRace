package uhu.juego15;

import java.util.ArrayList;

import ontology.Types.ACTIONS;
import uhu.arbol.NodoLogico;
import uhu.Cerebro;
import uhu.Constantes;
import uhu.grid.Casilla;
import uhu.grid.Mapa;

public class CercaMeta extends NodoLogico {
	@Override
	public ACTIONS decidir(Cerebro c) {
		Mapa mapa = c.getMapa();

		int currentColumn = mapa.getAvatar().getX();
		int lastColumn = mapa.getLastAvatar().getX();

		int columnaPortal = mapa.getColumnaPortal();

		boolean cerca = false;

		float lastDist = Math.abs(columnaPortal - lastColumn);
		float currentDist = Math.abs(columnaPortal - currentColumn);

		if (lastDist <= currentDist) {
			cerca = false;
		} else {
			cerca = true;
		}

		if (cerca) {
			this.setValor(true);
		} else {
			this.setValor(false);
		}

		return super.decidir(c);
	}
}
