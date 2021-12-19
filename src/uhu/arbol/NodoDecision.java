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
public abstract class NodoDecision implements Nodo {

	@Override
	public ACTIONS decidir(Cerebro c) {
		return null;
	}

}
