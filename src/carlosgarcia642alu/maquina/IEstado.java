/**
 * 
 */
package uhu.carlosgarcia642alu.maquina;

import java.util.ArrayList;

import ontology.Types.ACTIONS;
import uhu.carlosgarcia642alu.Cerebro;

/**
 * @author LiTTo
 *
 */
public interface IEstado {
	public ACTIONS getAccion(Cerebro c);

	public ACTIONS getAccionEntrada(Cerebro c);

	public ACTIONS getAccionSalida(Cerebro c);

	public ArrayList<Transicion> getTransiciones();
}
