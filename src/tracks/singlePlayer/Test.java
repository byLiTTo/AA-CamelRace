package tracks.singlePlayer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import core.logging.Logger;
import tools.Utils;
import tracks.ArcadeMachine;

/**
 * Created with IntelliJ IDEA. User: Diego Date: 04/10/13 Time: 16:29 This is a
 * Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class Test {

	public static void main(String[] args) {

		// Available tracks:
		String sampleRandomController = "tracks.singlePlayer.simple.sampleRandom.Agent";
		String doNothingController = "tracks.singlePlayer.simple.doNothing.Agent";
		String sampleOneStepController = "tracks.singlePlayer.simple.sampleonesteplookahead.Agent";
		String sampleFlatMCTSController = "tracks.singlePlayer.simple.greedyTreeSearch.Agent";

		String sampleMCTSController = "tracks.singlePlayer.advanced.sampleMCTS.Agent";
		String sampleRSController = "tracks.singlePlayer.advanced.sampleRS.Agent";
		String sampleRHEAController = "tracks.singlePlayer.advanced.sampleRHEA.Agent";
		String sampleOLETSController = "tracks.singlePlayer.advanced.olets.Agent";

		String sampleMDPController = "uhu.AgentPlayer";
		String sampleMDPControllerTrainer = "uhu.AgentTrainer";

		// Load available games
		String spGamesCollection = "examples/all_games_sp.csv";
		String[][] games = Utils.readGames(spGamesCollection);

		// Game settings
		boolean visuals = true;
		int seed = new Random().nextInt();

		// Game and level to play
		int gameIdx = 15;
		int levelIdx = 6; // level names from 0 to 4 (game_lvlN.txt).
		String gameName = games[gameIdx][1];
		String game = games[gameIdx][0];
		String level1 = game.replace(gameName, gameName + "_lvl" + levelIdx);

		String recordActionsFile = null;// "actions_" + games[gameIdx] + "_lvl"
		// + levelIdx + "_" + seed + ".txt";
		// where to record the actions
		// executed. null if not to save.

		for (int i = 0; i < 18; i++) {
			levelIdx = i;
			for (int j = 0; j < 2; j++) {
				level1 = game.replace(gameName, gameName + "_lvl" + levelIdx);
				ArcadeMachine.runOneGame(game, level1, visuals, sampleMDPController, recordActionsFile, seed, 0);
			}
		}

		// ==============================================================================================================
		// NUESTRO ENTRENAMIENTO
		// ==============================================================================================================

		// Entrenamiento ----------------------------------------

//		String path = "resultados.csv";
//		int M = 100; // Número de partidas
//		String[] arrayResult = new String[M + 1];
//		arrayResult[0] = "Partida,Ticks\n";
//		Double[] arrayTicks = new Double[M];
//		for (int i = 0; i < M + 1; i++) {
//			if (i > -1)
//				visuals = true;
//
//			levelIdx = getRandomNumber(6, 9);
//			System.out.println("\nPartida actual: " + (i + 1) + " - Nivel: " + levelIdx);
//			level1 = game.replace(gameName, gameName + "_lvl" + levelIdx);
//
//			double[] resultado = ArcadeMachine.runOneGame(game, level1, visuals, sampleMDPController, recordActionsFile,
//					seed, 0);
//			// resultado[0] -> indica la victoria(1 o 0) - resultado[1] -> puntos -
//			// resultado[2] -> ticks
//			arrayResult[i] = i + "," + resultado[2] + "\n";
//		}
//
//		// Creamos fichero
//		try {
//			FileWriter myWriter = new FileWriter(path);
//			for (int i = 0; i < M + 1; i++)
//				myWriter.write(arrayResult[i]);
//
//			myWriter.close();
//		} catch (IOException e) {
//			System.out.println("An error occurred.");
//			e.printStackTrace();
//		}

		// Practica ----------------------------------------

//		double totalPuntos = 0;
//        double puntos[] = new double[10];
//        double victorias[] = new double[10];
//        int numVictorias = 0;
//
//        for(int i=0;i<10;i++) {
//            puntos[i] = 0;
//            victorias[i] = 0;
//        }
//
//        for(int i=0;i<8;i++) {
//            levelIdx = i;
//            level1 = game.replace(gameName, gameName + "_lvl" + levelIdx);
//            for(int j=0;j<1;j++) {
//                double[] resultado = ArcadeMachine.runOneGame(game, level1, visuals, sampleMDPController, recordActionsFile, seed, 0);
//                //resultado[0] -> indica la victoria(1 o 0) - resultado[1] -> puntos - resultado[2] -> ticks
//                totalPuntos = totalPuntos + resultado[1];
//                puntos[i] = puntos[i] + resultado[1];
//
//                if(resultado[0] == 1) {
//                    numVictorias++;
//                    victorias[i]++;
//                }
//            }
//        }

		// ==============================================================================================================

		// 1. This starts a game, in a level, played by a human.
//		ArcadeMachine.playOneGame(game, level1, recordActionsFile, seed);

		// 2. This plays a game in a level by the controller.
//		ArcadeMachine.runOneGame(game, level1, visuals, sampleMDPController, recordActionsFile, seed, 0);

		// 3. This replays a game from an action file previously recorded
		// String readActionsFile = recordActionsFile;
		// ArcadeMachine.replayGame(game, level1, visuals, readActionsFile);

		// 4. This plays a single game, in N levels, M times :
//		String level2 = new String(game).replace(gameName, gameName + "_lvl" + 1);
//		int M = 10;
//		for(int i=0; i<games.length; i++){
//			game = games[i][0];
//			gameName = games[i][1];
//			level1 = game.replace(gameName, gameName + "_lvl" + levelIdx);
//			ArcadeMachine.runGames(game, new String[]{level1}, M, sampleMDPController, null);
//		}

		// 5. This plays N games, in the first L levels, M times each. Actions to file
		// optional (set saveActions to true).
//		int N = games.length, L = 2, M = 1;
//		boolean saveActions = false;
//		String[] levels = new String[L];
//		String[] actionFiles = new String[L*M];
//		for(int i = 0; i < N; ++i)
//		{
//			int actionIdx = 0;
//			game = games[i][0];
//			gameName = games[i][1];
//			for(int j = 0; j < L; ++j){
//				levels[j] = game.replace(gameName, gameName + "_lvl" + j);
//				if(saveActions) for(int k = 0; k < M; ++k)
//				actionFiles[actionIdx++] = "actions_game_" + i + "_level_" + j + "_" + k + ".txt";
//			}
//			ArcadeMachine.runGames(game, levels, M, sampleRHEAController, saveActions? actionFiles:null);
//		}

		// 6. This plays a single game, in N levels, M times by the controller without
		// visuals:
//		int M = 100;
//		for (int i = 0; i < M; i++) {
//			ArcadeMachine.runOneGame(game, level1, false, sampleMDPController, recordActionsFile, seed, 0);
//		}

//		//resultado[0] -> indica la victoria(1 o 0) - resultado[1] -> puntos - resultado[2] -> ticks
//				double[] resultado = 
//				
////				Para guardar los resultados:
//				for(int i=0;i<numeroPartidas;i++){
//				            write(i + "," + resultado[2])
//				        }

		// 7. This plays a single game, in N levels, M times by the controller with
		// visuals:
//		int M = 15;
//		for (int i = 0; i < M; i++) {
//			ArcadeMachine.runOneGame(game, level1, true, sampleMDPController, recordActionsFile, seed, 0);
//		}

	}

	private static int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
}
