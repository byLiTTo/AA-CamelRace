/**
 * 
 */
package uhu.carlosgarcia642alu.maquina;

import uhu.carlosgarcia642alu.Cerebro;

/**
 * @author LiTTo
 *
 */
public interface ITransicion {
	public boolean seActiva(Cerebro c);

	public Estado getEstadoDestino();
}
