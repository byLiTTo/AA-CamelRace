package uhu.juego15.preguntas;

import uhu.Cerebro;
import uhu.Constantes.STATES;
import uhu.arbol.NodoLogico;
import uhu.grid.Casilla;

import static uhu.Constantes.*;

public class TIENES_MURO_IZQUIERDA extends NodoLogico {
	@Override
	public STATES decidir(Cerebro c) {

		Casilla avatar = c.getMapa().getAvatar();
		
		Casilla arriba = c.getMapa().getNodo(avatar.getX(), avatar.getY()+1);
		Casilla abajo = c.getMapa().getNodo(avatar.getX(), avatar.getY()-1);
		
		if (arriba.getEstado().equals(MURO) && abajo.getEstado().equals(MURO)) {
			this.setValor(true);
		} else {
			this.setValor(false);
		}
		return super.decidir(c);
	}
}
