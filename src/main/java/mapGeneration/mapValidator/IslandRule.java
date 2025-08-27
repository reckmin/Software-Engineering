package mapGeneration.mapValidator;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mapData.Field;
import mapData.Position;
import mapEnums.Terrain;
import mapGeneration.HalfMapConfiguration;

// Algorithm FloodMill is used as described on Wikipedia :  https://en.wikipedia.org/wiki/Flood_fill 

public class IslandRule implements IRule {
	private static final Logger logger = LoggerFactory.getLogger(IslandRule.class);
	private final int xMax;
	private final int yMax;

	public IslandRule() {
		this.xMax = HalfMapConfiguration.X_HALFMAP_SIZE;
		this.yMax = HalfMapConfiguration.Y_HALFMAP_SIZE;
	}

	@Override
	public void validate(HashMap<Position, Field> mapOfFields, Notification notification) {
		if (!checkForIslands(mapOfFields)) {
			notification.addError("Island detected!");
		}
	}

	public boolean checkForIslands(HashMap<Position, Field> mapOfFields) {
		boolean[][] visitedFields = new boolean[yMax][xMax];
		boolean foundNonWater = false;

		for (int y = 0; y < yMax && !foundNonWater; ++y) {
			for (int x = 0; x < xMax && !foundNonWater; ++x) {
				if (isNonWaterField(mapOfFields, x, y)) {
					floodFill(mapOfFields, x, y, visitedFields);
					foundNonWater = true;
				}
			}
		}

		if (!foundNonWater)
			return true;

		for (int i = 0; i < yMax; i++) {
			for (int j = 0; j < xMax; j++) {
				if (isNonWaterField(mapOfFields, j, i) && !visitedFields[i][j]) {
					logger.debug("Island detected!");
					return false;
				}
			}
		}
		return true;
	}

	private void floodFill(HashMap<Position, Field> mapOfFields, int x, int y, boolean[][] visitedFields) {
		Field field = mapOfFields.get(new Position(x, y));
		if (x < 0 || x >= xMax || y < 0 || y >= yMax || visitedFields[y][x] || field == null
				|| field.getTerrain() == Terrain.Water) {
			return;
		}

		visitedFields[y][x] = true;

		floodFill(mapOfFields, x + 1, y, visitedFields);
		floodFill(mapOfFields, x - 1, y, visitedFields);
		floodFill(mapOfFields, x, y + 1, visitedFields);
		floodFill(mapOfFields, x, y - 1, visitedFields);
	}

	private boolean isNonWaterField(HashMap<Position, Field> mapOfFields, int x, int y) {
		Field field = mapOfFields.get(new Position(x, y));
		return field != null && field.getTerrain() != Terrain.Water;
	}

}