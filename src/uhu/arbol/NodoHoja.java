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
public abstract class NodoHoja extends NodoDecision {

	@Override
	public ACTIONS decidir(Cerebro c) {
		return null;
	}

}
