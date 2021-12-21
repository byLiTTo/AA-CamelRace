package uhu.juego15.preguntas;

import static uhu.Constantes.*;

import ontology.Types.ACTIONS;
import uhu.arbol.NodoLogico;
import uhu.Cerebro;
import uhu.Constantes;
import uhu.grid.Casilla;
import uhu.grid.Mapa;

public class Retrocediendo extends NodoLogico {
	@Override
	public STATES decidir(Cerebro c) {
		if(c.getLastAction() == ACTIONS.ACTION_LEFT)
			this.setValor(true);
		else
			this.setValor(false);
		
		return super.decidir(c);
	}
}
