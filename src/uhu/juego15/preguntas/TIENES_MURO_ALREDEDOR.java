package uhu.juego15.preguntas;

import uhu.Cerebro;
import uhu.Constantes.STATES;
import uhu.arbol.NodoLogico;
import uhu.grid.Casilla;

import static uhu.Constantes.*;

public class TIENES_MURO_ALREDEDOR extends NodoLogico {
	@Override
	public STATES decidir(Cerebro c) {

		Casilla avatar = c.getMapa().getAvatar();
		Casilla arriba = c.getMapa().getNodo(avatar.getX(), avatar.getY() - 1);
		boolean hayMuro = false;

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				Casilla aux = c.getMapa().getNodo(avatar.getX() + i, avatar.getY() + j);
				if (aux.getEstado().equals(MURO)) {
					hayMuro = true;
				}
			}
		}
		if (hayMuro) {
			this.setValor(true);
		} else {
			this.setValor(false);
		}
		return super.decidir(c);
	}
}
