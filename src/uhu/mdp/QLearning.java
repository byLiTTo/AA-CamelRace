package uhu.mdp;

import java.util.Random;

import ontology.Types.ACTIONS;

public class QLearning {

	private double alpha;
	private double discount;
	private double reward;

	private int numStates;
	private int numActions;

	public QLearning() {
		// Inicializar la tabla
	}

	public void update(Estado lastState, ACTIONS lastAction, Estado currentState) {
		double qValue = 0;

		// Aprendemos de la experiencia pero no sobreescribimos
		qValue += getQValue(lastState, lastAction) * (1 - alpha);

		// Y le sumamos el maximo qValue del estado que nos encontramos
		qValue += alpha * (reward + discount * getValue(currentState));

		setValues(currentState, lastAction, qValue);
	}

	public void getQValue(Estado state, ACTIONS action) {
		// De vuelve el valor q de la posicion estado accion
//		return qTable[state][action];
	}
	
	public ACTIONS getAction(state)

	public double getMaxValueFromQValues(Estado state) {
		// Si no hay acciones disponibles
		if (getAviableActions(state) == 0) {
			return 0.0;
		}
		double maxEval = Double.MIN_VALUE;
		
		for action in getAviableActions{
			qValAcc = getQValue(state, action);
			if(qValAcc > maxEval) {
				maxEval = qValAcc;
			}
		}
		return maxEval;
	}
	
	public ACTIONS nextAction(Estado currentState()) {
		Random rd = new Random();
		double randomNumber = Math.abs(rd.nextDouble());
		
		if(randomNumber < epsilon) {
			return getRandomAction();
		}else {
			return getBestAction(currentState());
		}
	}
	
	public ACTIONS getBestAction(Estado state) {
		
	}
	
}
