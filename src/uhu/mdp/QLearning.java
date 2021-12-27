package uhu.mdp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.event.IIOReadWarningListener;

import ontology.Types.ACTIONS;
import static uhu.Constantes.*;

public class QLearning {

	private double alpha;
	private double gamma;
	private double epsilon;
	private int timer;

	private double reward;

	private ArrayList<STATES> states;
	private ArrayList<ACTIONS> actions;
	private double[][] qTable;

	public QLearning(ArrayList<STATES> states, ArrayList<ACTIONS> actions, String path, int timer) {
		this.states = states;
		this.actions = actions;
		this.timer = timer;

		this.qTable = new double[states.size()][actions.size()];

		File file = new File(path);
		if (!file.exists()) {
			initTable();
		} else {
			readTable(path);
		}

		this.alpha = 0.5;
		this.gamma = 0.5;
		this.epsilon = 0.1;

	}
	
	public int getTimer() {
		return this.timer;
	}

	public ACTIONS update(STATES lastState, ACTIONS lastAction, STATES currentState, double reward) {
		this.reward = reward;
		double sample = reward + gamma * getMaxQValue(currentState);
		double newQValue = (1 - alpha) * getQValue(lastState, lastAction) + alpha * sample;

//		double lastQValue = getQValue(lastState, lastAction);
//		double maxCurrentQValue = getMaxQValue(currentState);
//
//		double newQValue = lastQValue + alpha * (reward + gamma * maxCurrentQValue - lastQValue);

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

		double maxVal = -Double.MAX_VALUE;
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

//		if (candidatos.size() == 0) {
//			printTable();
//		}

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
		this.alpha = (0.9 * 1000 / (1000 + timer));
		this.epsilon = (0.9 * 1000 / (1000 + timer));

		timer++;
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

	public ACTIONS nextOnlyOneBestAction(STATES currentState) {
		int i = states.indexOf(currentState);

		double maxVal = -Double.MAX_VALUE;
		int indexAction = 0;

		for (int j = 0; j < actions.size(); j++) {
			if (qTable[i][j] > maxVal) {
				maxVal = qTable[i][j];
				indexAction = j;
			}
		}
		return actions.get(indexAction);
	}

	private double getReward(STATES lastState, ACTIONS lastAction, STATES currentState) {

		return 0;
	}

	public void writeTable(String path) {
		try {
			FileWriter fichero;
//        fichero = new FileWriter(nombre + ".tsp");
			fichero = new FileWriter(path);
			String fila = "";
			for (int i = 0; i < states.size(); i++) {
				fila = "";
				for (int j = 0; j < actions.size(); j++) {
					if (j != 0)
						fila += "," + qTable[i][j];
					else
						fila += qTable[i][j];
				}
				fichero.write(fila + "\n");
			}
			fichero.close();
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void readTable(String path) {
		try {
			FileReader fichero = new FileReader(path); // FileReader sierve para leer ficheros
			BufferedReader b = new BufferedReader(fichero); // BufferReader sirve para leer texto de una entrada de
															// caracteres
			String aux; // Variable donde guardar las lecturas de fichero de forma momentanea
			ArrayList<String> stringFichero = new ArrayList<>(); // Almacena cada linea del fichero
			String[] parts; // Para dividir Strings

			// Mientras pueda leer la siguiente linea, sigue leyendo, hace la asignación
			// dentro del if
			while ((aux = b.readLine()) != null) {
				stringFichero.add(aux);
			}

			fichero.close();
			for (int i = 0; i < stringFichero.size(); i++) {
				aux = stringFichero.get(i);
				parts = aux.split(",");

				for (int j = 0; j < parts.length; j++) {
					qTable[i][j] = Double.parseDouble(parts[j]);
				}
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

//		printTable();

	}

	public void printTable() {
		for (int i = 0; i < states.size(); i++) {
			System.out.println();
			for (int j = 0; j < actions.size(); j++) {
				System.out.print(qTable[i][j] + " , ");
			}
		}
		System.out.println();
	}

}
