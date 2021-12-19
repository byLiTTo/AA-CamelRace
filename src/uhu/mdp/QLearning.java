package uhu.mdp;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import ontology.Types.ACTIONS;
import static uhu.Constantes.*;

public class QLearning {

	private double alpha;
	private double gamma;
	private double epsilon;
	private int time = 0;

	private double reward;

	private ArrayList<STATES> states;
	private ArrayList<ACTIONS> actions;
	private double[][] qTable;

	public QLearning(ArrayList<STATES> states, ArrayList<ACTIONS> actions, String path) {
		this.states = states;
		this.actions = actions;

		this.qTable = new double[states.size()][actions.size()];

		File file = new File(path);
		if (!file.exists()) {
			initTable();
		} else {
			readTable(path);
		}

		this.alpha = 0.9;
		this.gamma = 0.5;
		this.epsilon = 0.9;

	}

	public ACTIONS update(STATES lastState, ACTIONS lastAction, STATES currentState, double reward) {
		this.reward = reward;
		double sample = reward + gamma * getMaxQValue(currentState);
		double newQValue = (1 - alpha) * getQValue(lastState, lastAction) + alpha * sample;

		setQValue(lastState, lastAction, newQValue);

		updateVar();

		return nextAction(currentState);
	}

	private void initTable() {
		for (int i = 0; i < states.size(); i++) {
			for (int j = 0; j < actions.size(); j++) {
				this.qTable[i][j] = 0;
			}
		}
	}

	public double getQValue(STATES s, ACTIONS a) {
		int i = states.indexOf(s);
		int j = actions.indexOf(a);

		return this.qTable[i][j];
	}

	public void setQValue(STATES s, ACTIONS a, double qValue) {
		int i = states.indexOf(s);
		int j = actions.indexOf(a);

		this.qTable[i][j] = qValue;
	}

	public double getMaxQValue(STATES state) {
		int i = states.indexOf(state);

		double maxVal = Double.MIN_VALUE;

		for (int j = 0; j < actions.size(); j++) {
			if (qTable[i][j] > maxVal) {
				maxVal = qTable[i][j];
			}
		}

		return maxVal;
	}

	public ACTIONS getBestAction(STATES state) {
		int i = states.indexOf(state);

		ArrayList<Integer> candidatos = new ArrayList<Integer>();

		double maxVal = Double.MIN_VALUE;
		int indexAction = 0;

		for (int j = 0; j < actions.size(); j++) {
			if (qTable[i][j] > maxVal) {
				maxVal = qTable[i][j];
				indexAction = j;
				candidatos.clear();
				candidatos.add(indexAction);
			} else if (qTable[i][j] == maxVal) {
				indexAction = j;
				candidatos.add(indexAction);
			}
		}

		Random rd = new Random(System.currentTimeMillis());
		int seleccion = rd.nextInt(candidatos.size());

		return actions.get(candidatos.get(seleccion));
	}

	public ACTIONS getRandomAction() {
		Random rd = new Random(System.currentTimeMillis());
		int action = rd.nextInt(actions.size());

		return actions.get(action);
	}

	private void updateVar() {
		this.alpha = (0.9 * 100000 / (100000 + time));
		this.epsilon = (0.9 * 100000 / (100000 + time));

		time++;
	}

	public ACTIONS nextAction(STATES currentState) {
		Random rd = new Random(System.currentTimeMillis());
		double randomNumber = Math.abs(rd.nextDouble());

		if (randomNumber < epsilon) {
			return getRandomAction();
		} else {
			return getBestAction(currentState);
		}
	}

	private double getReward(STATES lastState, ACTIONS lastAction, STATES currentState) {

		return 0;
	}

	private void writeTable(String path) {
		// TODO
	}

	private void readTable(String path) {
		// TODO
	}

//	public void update(Estado lastState, ACTIONS lastAction, Estado currentState) {
//		double qValue = 0;
//
//		// Aprendemos de la experiencia pero no sobreescribimos
//		qValue += getQValue(lastState, lastAction) * (1 - alpha);
//
//		// Y le sumamos el maximo qValue del estado que nos encontramos
//		qValue += alpha * (reward + discount * getValue(currentState));
//
//		setValues(currentState, lastAction, qValue);
//	}
//
//	public void getQValue(Estado state, ACTIONS action) {
//		// De vuelve el valor q de la posicion estado accion
////		return qTable[state][action];
//	}
//
//	public ACTIONS getAction(Estado state) {
//		// Cogemos las acciones disponibles
//		ACTIONS action = getActionFromQValues(state);
//		return action;
//	}
//
//	public double getMaxValueFromQValues(Estado state) {
//		// Si no hay acciones disponibles
//		if (getAviableActions(state) == 0) {
//			return 0.0;
//		}
//		double maxEval = Double.MIN_VALUE;
//		
//		for action in getAviableActions(state){
//			qValAcc = getQValue(state, action);
//			if(qValAcc > maxEval) {
//				maxEval = qValAcc;
//			}
//		}
//		return maxEval;
//	}
//	
//	public ACTIONS getActionFromQValues(Estado state) {
//		// Por defecto no hacemos nada
//		ACTIONS bestAction = null;
//		
//		double maxEval = Double.MIN_VALUE;
//		for action in getAviableActions(state) {
//			if(getQValue(state, action) > maxEval) {
//				maxEval = getQValue(state, action);
//				// Añadir accion a array de acciones
//			}else /* Si son iguales */ {
//				// Añadir tambien al array de acciones
//			}
//		}
//		// Devuelve una accion aleatoria dentro de las mejores
//		return null;
//	}
// 
//
//	public ACTIONS getBestAction(Estado state) {
//		
//	}

}
