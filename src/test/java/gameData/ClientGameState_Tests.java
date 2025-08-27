package gameData;

import java.util.HashMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import mapData.ClientMap;
import mapData.Field;
import mapData.Position;
import mapEnums.FortState;
import mapEnums.PlayerPositionState;
import mapEnums.Terrain;
import mapEnums.TreasureState;


public class ClientGameState_Tests {
	@ParameterizedTest
	@MethodSource("createClientGameStateForParameterizedIsTreasureVisibleTest")
	public void IsTreasureVisible_withParametervalues_shouldProduceExpectedResult(ClientGameState gameState, boolean expected) {
		
		Assertions.assertEquals(gameState.isTreasureVisible(), expected);
	}
 
	private static Stream<Arguments> createClientGameStateForParameterizedIsTreasureVisibleTest() {
		HashMap<Position, Field> mapOfFields1 = new HashMap<>();
		mapOfFields1.put(new Position (0, 0), new Field(Terrain.Grass));
		mapOfFields1.put(new Position (0, 1), new Field(Terrain.Grass));
		mapOfFields1.put(new Position (1, 1), new Field(Terrain.Grass));
		mapOfFields1.put(new Position (1, 0), new Field(Terrain.Grass));
		ClientMap map1 = new ClientMap(2, 2, mapOfFields1);
		
		HashMap<Position, Field> mapOfFields2 = new HashMap<>();
		mapOfFields2.put(new Position (0, 0), new Field(Terrain.Grass, FortState.EnemyFortPresent, TreasureState.MyTreasurePresent,
				PlayerPositionState.BothPlayerPosition));
		mapOfFields2.put(new Position (0, 1), new Field(Terrain.Grass));
		mapOfFields2.put(new Position (1, 1), new Field(Terrain.Grass));
		mapOfFields2.put(new Position (1, 0), new Field(Terrain.Grass));
		ClientMap map2 = new ClientMap(2, 2, mapOfFields2);
		
		ClientGameState gameState1 = new ClientGameState("", PlayerGameState.MustAct, map1, false);
		
		ClientGameState gameState2 = new ClientGameState("", PlayerGameState.MustAct, map2, false);
		return Stream.of(Arguments.of(gameState1, false), Arguments.of(gameState2, true) );
	}
	
}
