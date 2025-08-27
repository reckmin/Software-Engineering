package moveManager;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.NoSuitableStrategyException;
import gameData.ClientGameState;
import mapData.Position;
import moveManager.strategy.MovementStrategy;
import moveManager.strategy.TargetLocatedStrategy;
import moveManager.strategy.TargetNotLocatedStrategy;

public class StrategyDefiner {
	private static final Logger logger = LoggerFactory.getLogger(StrategyDefiner.class);

	private final PlayerHalfMapsDefiner mapDefiner;

	private TargetNotLocatedStrategy findTreasureStrategy;
	private TargetLocatedStrategy treasureFoundStrategy;
	private TargetLocatedStrategy goToEnemyFieldStrategy;
	private TargetNotLocatedStrategy findCastleStrategy;
	private TargetLocatedStrategy castleFoundStrategy;

  
	boolean areStrategiesDefined = false;

	public StrategyDefiner() {
		this.mapDefiner = new PlayerHalfMapsDefiner();
	}

	public MovementStrategy determineStrategy(ClientGameState gameState) {
		if (!areStrategiesDefined)
			defineStrategies(gameState);

		if (!gameState.isCollectedTreasure()) {
			if (!gameState.isTreasureVisible()) {
				logger.info("findTreasureStrategy");
				return findTreasureStrategy;
			} 

			if (gameState.isTreasureVisible() ) {
				treasureFoundStrategy.setTarget(Optional.of(gameState.getTreasurePosition().get()));
				logger.info("treasureFoundStrategy");
				return treasureFoundStrategy;
			} 

		} else if (!gameState.isEnemyCastleVisible()) {

			if (!isPlayerOnEnemySide(gameState.getPlayerPosition().get())) {
				logger.info("goToEnemyFieldStrategy");
				goToEnemyFieldStrategy.setTarget(Optional.of(PlayerHalfMapsDefiner.getRandomGrassPosition(mapDefiner.getEnemyHalfMap())));
				return goToEnemyFieldStrategy;
			} else {
				logger.info("findCastleStrategy");
				return findCastleStrategy;
			}
		} else {
			logger.info("castleFoundStrategy");
			castleFoundStrategy.setTarget(Optional.of(gameState.getEnemyFortPosition().get()));
			return castleFoundStrategy;
		}
		 
		throw new NoSuitableStrategyException("No suitable strategy was found! gameState :" + gameState);
	}

	
	private void defineStrategies(ClientGameState gameState) {
		if (!mapDefiner.getAreHalfMapsDefined()) {
			mapDefiner.definePlayersHalfMaps(gameState.getMap());
		}

		findTreasureStrategy = new TargetNotLocatedStrategy(mapDefiner.getMyHalfMap());
		treasureFoundStrategy = new TargetLocatedStrategy(mapDefiner.getMyHalfMap());
		goToEnemyFieldStrategy = new TargetLocatedStrategy(gameState.getMap().getFields());
		findCastleStrategy = new TargetNotLocatedStrategy(mapDefiner.getEnemyHalfMap());
		castleFoundStrategy = new TargetLocatedStrategy(gameState.getMap().getFields());

		areStrategiesDefined = true;
	}


	private boolean isPlayerOnEnemySide(Position playerPosition) {
		return mapDefiner.getEnemyHalfMap().containsKey(playerPosition);
	}

}
