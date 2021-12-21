package uhu.juego15.preguntas;

import static uhu.Constantes.*;

import ontology.Types.ACTIONS;
import uhu.arbol.NodoLogico;
import uhu.Cerebro;
import uhu.Constantes;
import uhu.grid.Casilla;
import uhu.grid.Mapa;

public class EstoyRetrocediendo extends NodoLogico {
	@Override
	public STATES decidir(Cerebro c) {

		Casilla ahora = c.getMapa().getAvatar();
		Casilla antes = c.getMapa().getLastAvatar();

		int columna = c.getMapa().getColumnaPortal();

		double distanciaAhora = Math.abs(ahora.getX() - columna);
		double distanciaAntes = Math.abs(antes.getX() - columna);

		if (distanciaAhora <= distanciaAntes) {
			this.setValor(false);
//			System.out.println("No estoy retrocediendo");
		} else {
			this.setValor(true);
//			System.out.println("Estoy retrocediendo");
		}
		return super.decidir(c);
	}
}
