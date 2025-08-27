package moveManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gameData.ClientGameState;
import mapData.Field;
import mapData.Position;
import moveManager.strategy.MovementStrategy;

public class MoveManager {
	private static final Logger logger = LoggerFactory.getLogger(MoveManager.class);
	private List<Map.Entry<Position, Field>> pathOfFields = new ArrayList<>();
	private final StrategyDefiner strategyDefiner;
	private MovementStrategy strategy;

	public MoveManager() {
		this.strategyDefiner = new StrategyDefiner();
	}
 
	public Movement createMove(ClientGameState gameState) {
		strategy = strategyDefiner.determineStrategy(gameState);

		this.pathOfFields = strategy.calculatePath(gameState.getPlayerPosition().get());
		Position currentPos = gameState.getPlayerPosition().get();
		logger.info("Current Position {}", currentPos.toString());
		Position nextPos =pathOfFields.get(0).getKey();
		logger.info("Next Position {}", nextPos.toString());
		Movement toSendMove = PathToMovesConverter.getDirection(currentPos, nextPos);
		
		return toSendMove;
	}

	public static void printMovements(List<Movement> movements) {
		System.out.println("Movements:");
		for (int i = 0; i < movements.size(); i++) {
			System.out.print((i + 1) + ". " + movements.get(i) + "    ");
			if ((i + 1) % 10 == 0)
				System.out.println();
		}

	}

} 