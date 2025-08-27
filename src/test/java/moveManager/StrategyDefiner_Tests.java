package moveManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gameData.ClientGameState;
import gameData.PlayerGameState;
import mapData.ClientMap;
import mapData.Field;
import mapData.Position;
import mapEnums.FortState;
import mapEnums.Terrain;
import mapEnums.TreasureState;
import moveManager.strategy.MovementStrategy;
import moveManager.strategy.TargetNotLocatedStrategy;

public class StrategyDefiner_Tests {

	private StrategyDefiner strategyDefiner;
	private ClientGameState gameState;
	private ClientMap map;
	private HashMap<Position, Field> mapOfFields;

	@BeforeEach
	public void setUp() {
		this.strategyDefiner = new StrategyDefiner();

		mapOfFields = new HashMap<>();
		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 5; y++) {
				mapOfFields.put(new Position(x, y), new Field(Terrain.Grass));
			}
		}

		mapOfFields.get(new Position(0, 0)).setFortState(FortState.MyFortPresent);

		map = new ClientMap(mapOfFields);
		this.gameState = new ClientGameState("123", PlayerGameState.MustAct, map, false);
	}

	@Test
	public void determineStrategy_withTreasureAbsent_shouldReturnFindTreasureStrategy() {
		MovementStrategy strategy = strategyDefiner.determineStrategy(gameState);

		assertNotNull(strategy, "Strategy should not be null");
		assertTrue(strategy instanceof TargetNotLocatedStrategy,
				"Expected a TargetNotLocatedStrategy if treasure was not collected and is not visible");
	}

	@Test
	public void determineStrategy_withTreasureVisible_shouldReturnTreasureFoundStrategy() {
		Position treasurePos = new Position(2, 2);
		mapOfFields.get(treasurePos).setTreasureState(TreasureState.MyTreasurePresent);
		gameState.setTreasurePosition(treasurePos);

		MovementStrategy strategy = strategyDefiner.determineStrategy(gameState);

		assertNotNull(strategy, "Strategy should not be null");
		assertTrue(strategy.getClass().getSimpleName().contains("TargetLocatedStrategy"),
				"Expected a TargetLocatedStrategy if treasure is visible (but not collected).");
	}


}
