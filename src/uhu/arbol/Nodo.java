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
public abstract interface Nodo {

	public ACTIONS decidir(Cerebro c);

}
