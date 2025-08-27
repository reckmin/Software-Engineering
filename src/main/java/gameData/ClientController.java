package gameData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.FailedPlayerRegistrationException;
import exceptions.NetworkException;
import gameData.models.MapGenerationErrorLogModel;
import gameData.models.MapModel;
import mapData.ClientMap;
import mapGeneration.MapGenerator;
import moveManager.MoveManager;
import moveManager.Movement;
import networkAndConverter.NetworkHandler;

public class ClientController {
	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

	private final MapModel mapModel;
	private final MapGenerationErrorLogModel errorLogModel;

	private final NetworkHandler network;
	private MoveManager moveManager;

	public ClientController(NetworkHandler networkHandler, MapModel mapModel,
			MapGenerationErrorLogModel errorLogModel, MoveManager manager) {
		this.network = networkHandler;
		this.moveManager = manager;
		this.mapModel = mapModel;
		this.errorLogModel = errorLogModel;

	}

	public void start() {
		logger.info("Starting game...");

		initiateRegistration();

		ClientMap map = createHalfMap();
		initiateHalfMapSending(map);

		while (!mapModel.getClientGameState().hasGameEnded()) {
			waitPollingIntervall();
			updateGameState();


			if (mapModel.getClientGameState().isMyTurn()) {
				initiateMoveSending();
			}
		}
		logger.info("Game finished! Final PlayerGameState: {}", mapModel.getClientGameState().getPlayerGameState());
	}

	private void initiateRegistration() {
		try {
			network.registerPlayer();
			mapModel.addRound();
		} catch (FailedPlayerRegistrationException e) {
			logger.error("Failed to register player: {}", e.getMessage());
			throw new IllegalStateException("Player registration failed." + e.getMessage());

		}
	}

	private void initiateHalfMapSending(ClientMap map) {
		waitForTurn();

		try {
			network.sendHalfMap(map);
			mapModel.addRound();
			logger.info("HalfMap sent successfully!");
		} catch (NetworkException e) {
			logger.error("Failed to send HalfMap: {}", e.getMessage());
			throw new IllegalStateException("HalfMap sending failed due to" + e.getMessage()); 
		}
	}

	private ClientMap createHalfMap() {
		MapGenerator generator = new MapGenerator(errorLogModel);
		ClientMap map = generator.geneteHalfMap();

		return map;
	}

	private boolean updateGameState() {
		ClientGameState newState;
		try {
			newState = network.getGameState();
			if (hasGameStateChanged(newState)) {
				mapModel.setClientGameState(newState);
				mapModel.addRound();
				return true;
			}
		} catch (NetworkException e) {
			throw new IllegalStateException("Failed to get GameState!" + e.getMessage());
		}

		return false;
	}

	private boolean hasGameStateChanged(ClientGameState currentState) {
		return !mapModel.getClientGameState().getGameStateId().equals(currentState.getGameStateId());
	}

	private void initiateMoveSending() {
		if (!mapModel.getClientGameState().isMyTurn()) {
			logger.warn("Attempted to send Movement out of turn!");
			return;
		}

		if (mapModel.getClientGameState().hasGameEnded()) {
			logger.info("Game has ended!");
			return;
		}

		Movement move = moveManager.createMove(mapModel.getClientGameState());
		try {
			network.sendMove(move);
			logger.info("Move '{}' sent successfully.", move);
		} catch (Exception e) {
			logger.error("Failed to send move: {}", e.getMessage());
		}

	}

	private void waitForTurn() {
		while (!mapModel.getClientGameState().isMyTurn()) {
			waitPollingIntervall();

			updateGameState();
			if (mapModel.getClientGameState().hasGameEnded()) {
				logger.info("Game ended!");
				return;
			}
		}

	}

	private void waitPollingIntervall() {
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			logger.error("Polling sleep interrupted: {}", e.getMessage());
		}
	}

}