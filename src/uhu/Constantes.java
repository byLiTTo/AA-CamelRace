/**
 * 
 */
package uhu;

/**
 * Clase donde se guardaran algunas constantes de uso comun para facilitar su
 * accesibilidad en las diferentes partes del proyecto.
 * 
 * @author LiTTo
 *
 */
public final class Constantes {

	// =============================================================================
	// VISUALIZAR MAPA
	// =============================================================================

	// POR DEFECTO-VACiO
	public static final String VACIO = ".";

	// AVATAR
	public static final String AVATAR = "@";
	public static final int avatar_cate = 0;
	public static final int avatar_tipo = 20;

	// INMOVIBLES
	public static final String MURO = "w";
	public static final int muro_cate = 4;
	public static final int muro_tipo = 0;

	// PORTALS
	public static final String META = "=";
	public static final int meta_cate = 2;
	public static final int meta_tipo = 15;

	// MOVIBLES
	// Camello derecha rapido
	
	public static final String CAMELLO = "C";
	public static final int cdr_cate = 6;
	public static final int cdr_tipo = 6;

	// NPCs
//	public static final String CAMELLO= "C";
//	public static final int cdr_cate = 6;
//	public static final int cdr_tipo = 6;

	// =============================================================================
	// OPCIONES DE METODOS
	// =============================================================================

	public enum Visualizaciones {
		NADA, MAPA, BASICO, CAMELLOS, TODO
	}

	public enum SENTIDO {
		ORIENTE, OCCIDENTE,
	}
	
	public enum ORIENTACION{
		NORTE,
		SUR,
		ESTE,
		OESTE
	}

	public enum STATES {
		HACIA_NORTE, HACIA_SUR, HACIA_ESTE, HACIA_OESTE
	}

	public enum QUESTIONS {
		TIENES_MURO_ARRIBA, TIENES_MURO_ABAJO, TIENES_MURO_IZQUIERDA, TIENES_MURO_DERECHA,
		ORIENTACION_NORTE, ORIENTACION_SUR, ORIENTACION_ESTE, ORIENTACION_OESTE

	}
}
