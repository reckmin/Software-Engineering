package moveManager;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mapData.ClientMap;
import mapData.Field;
import mapData.Position;
import mapEnums.Terrain;
import mapGeneration.HalfMapConfiguration;

public class PlayerHalfMapsDefiner {
	private static final Logger logger = LoggerFactory.getLogger(PlayerHalfMapsDefiner.class);
	private HashMap<Position, Field> myHalfMap;
	private HashMap<Position, Field> enemyHalfMap;
	private boolean areHalfMapsDefined = false;

	private int HORIZONTAL_MAP = 20;
	private int SQUARE_MAP = 10;

	public PlayerHalfMapsDefiner() {
		this.enemyHalfMap = new HashMap<>();
		this.myHalfMap = new HashMap<>();
	}
  
	public boolean getAreHalfMapsDefined() {
		return areHalfMapsDefined;
	}

	public HashMap<Position, Field> getMyHalfMap() {
		return myHalfMap;
	}

	public HashMap<Position, Field> getEnemyHalfMap() {
		return enemyHalfMap;
	}

	public static Position getRandomGrassPosition(HashMap<Position, Field> map) {
		Position randomPosition = map.entrySet().stream()
				.filter(entry -> entry.getValue().getTerrain().equals(Terrain.Grass)).map(Map.Entry::getKey).findFirst()
				.get();
		return randomPosition;
	}

	public void definePlayersHalfMaps(ClientMap map) {
		Position fortPosition = map.getFortPosition().get();
 
		if (map.getWidth() == HORIZONTAL_MAP) {
			logger.debug("HORIZONTAL MAP");
			splitHorizontalMap(map, fortPosition);
		} else if (map.getHeight() == SQUARE_MAP) {
			logger.debug("SQUARE MAP");
			splitVerticalMap(map, fortPosition);
		}
		areHalfMapsDefined = true;
	}

	private void splitHorizontalMap(ClientMap map, Position fortPosition) {
		boolean isFortOnRightSide = fortPosition.getX() >= HalfMapConfiguration.X_HALFMAP_SIZE;

		logger.debug("Is my Map is on right side? -> {}", isFortOnRightSide);

		for (Map.Entry<Position, Field> entry : map.getFields().entrySet()) {
			Position position = entry.getKey();
			Field field = entry.getValue();

			if ((position.getX() >= HalfMapConfiguration.X_HALFMAP_SIZE) == isFortOnRightSide) {
				this.myHalfMap.put(position, field);
			} else {
				this.enemyHalfMap.put(position, field);
			}
		}

	}

	private void splitVerticalMap(ClientMap map, Position fortPosition) {
		boolean isFortOnTopSide = fortPosition.getY() >= HalfMapConfiguration.Y_HALFMAP_SIZE;

		logger.debug("Is my Map is on top side? -> {}", isFortOnTopSide);

		for (Map.Entry<Position, Field> entry : map.getFields().entrySet()) {
			Position position = entry.getKey();
			Field field = entry.getValue();

			if ((position.getY() >= HalfMapConfiguration.Y_HALFMAP_SIZE) == isFortOnTopSide) {
				this.myHalfMap.put(position, field);
			} else {
				this.enemyHalfMap.put(position, field);
			}
		}
	}
}
