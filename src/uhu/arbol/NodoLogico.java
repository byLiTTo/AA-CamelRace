/**
 * 
 */
package uhu.arbol;

import ontology.Types.ACTIONS;
import uhu.Cerebro;

/**
 * @author LiTTo
 *
 */
public abstract class NodoLogico extends NodoInterno {
	
	// =============================================================================
	// VARIABLES
	// =============================================================================

	private NodoDecision yes;
	private NodoDecision no;
	private boolean valor;

	public void setYes(NodoDecision y) {
		this.yes = y;
	}

	public void setNo(NodoDecision n) {
		this.no = n;
	}

	public void setValor(boolean b) {
		this.valor = b;
	}

	@Override
	public ACTIONS decidir(Cerebro c) {
		if (valor) {
			return this.yes.decidir(c);
		} else {
			return this.no.decidir(c);
		}
	}
}
