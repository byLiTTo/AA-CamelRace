package uhu.juego15.preguntas;

import uhu.Cerebro;
import uhu.Constantes.STATES;
import uhu.arbol.NodoLogico;
import uhu.grid.Casilla;

import static uhu.Constantes.*;

public class TIENES_MURO_DERECHA extends NodoLogico {
	@Override
	public STATES decidir(Cerebro c) {

		Casilla avatar = c.getMapa().getAvatar();
		Casilla derecha = c.getMapa().getNodo(avatar.getX()+1, avatar.getY());
		
		if (derecha.getEstado().equals(MURO)) {
			this.setValor(true);
		} else {
			this.setValor(false);
		}
		return super.decidir(c);
	}
}
