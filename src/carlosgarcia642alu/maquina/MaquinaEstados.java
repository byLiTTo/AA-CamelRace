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
public class MaquinaEstados {

	// =============================================================================
	// VARIABLES
	// =============================================================================

	private ArrayList<Estado> estados;
	private Estado estadoInicial;
	private Estado estadoActual;

	// =============================================================================
	// CONSTRUCTORES
	// =============================================================================

	/**
	 * Constructor publico sin parametros. Inicializa la lista de estados como vacia
	 * y los estados incial y actual como nulos.
	 */
	public MaquinaEstados() {
		this.estados = new ArrayList<Estado>();
		this.estadoInicial = null;
		this.estadoActual = null;
	}

	/**
	 * Constructor publico con parametros. Inicializa la lista de estados con la
	 * lista pasada por parametro y los estados inicial y actual con el estado
	 * pasado por parametro.
	 * 
	 * @param e   Lista de estados de la maquina.
	 * @param ini Estado inicial de la maquina, en la inicializacion de la maquina,
	 *            tambien sera el estado actual.
	 */
	public MaquinaEstados(ArrayList<Estado> e, Estado ini) {
		this.estados = e;
		this.estadoInicial = ini;
		this.estadoActual = this.estadoInicial;
	}

	// =============================================================================
	// METODOS
	// =============================================================================

	public ACTIONS actualiza(Cerebro c) {
		Transicion transicionDesencadenada = null;

		for (Transicion t : this.estadoActual.getTransiciones()) {
			if (t.seActiva(c)) {
				if (this.estados.contains(t.getEstadoDestino())) {
					transicionDesencadenada = t;
					break;
				}
			}
		}

		if (transicionDesencadenada != null) {
			Estado estadoDestino = transicionDesencadenada.getEstadoDestino();
			ACTIONS accion = estadoDestino.getAccionSalida(c);
			this.estadoActual = estadoDestino;
			return accion;
		} else {
			return this.estadoActual.getAccion(c);
		}
	}

}
