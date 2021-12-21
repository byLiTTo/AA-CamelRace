package uhu.juego15.preguntas;

import static uhu.Constantes.*;
import uhu.arbol.NodoLogico;
import uhu.Cerebro;
import uhu.Constantes.STATES;
import uhu.grid.Casilla;
import uhu.grid.Mapa;

public class PuedeAvanzar extends NodoLogico {
	@Override
	public STATES decidir(Cerebro c) {

		Casilla avatar = c.getMapa().getAvatar();
		int columnaPortal = c.getMapa().getColumnaPortal();

		double orientacion = avatar.getX() - columnaPortal;

		Casilla delante = null;

		if (orientacion < 0) {
			// Derecha
			delante = c.getMapa().getNodo(avatar.getX() + 1, avatar.getY());
		} else {
			// Izquierda
			delante = c.getMapa().getNodo(avatar.getX() - 1, avatar.getY());
		}

		if (delante.getEstado().equals(MURO)) {
			this.setValor(true);
		} else {
			this.setValor(false);
		}
		return super.decidir(c);
	}
}
