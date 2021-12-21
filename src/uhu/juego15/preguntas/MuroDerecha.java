package uhu.juego15.preguntas;

import static uhu.Constantes.*;
import uhu.arbol.NodoLogico;
import uhu.Cerebro;
import uhu.Constantes;
import uhu.grid.Casilla;
import uhu.grid.Mapa;

public class MuroDerecha extends NodoLogico {
	@Override
	public STATES decidir(Cerebro c) {
		
		Mapa mapa = c.getMapa();
		int actualX = mapa.getAvatar().getX();
		int actualY = mapa.getAvatar().getY();
		
		int checkX = actualX + 1;
		Casilla casillaCheck = mapa.getNodo(checkX, actualY);
		
		if(casillaCheck.getEstado().equals(Constantes.MURO))
			this.setValor(true);
		else
			this.setValor(false);
		
		return super.decidir(c);
	}
}
