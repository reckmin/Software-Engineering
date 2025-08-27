package mapGeneration.mapValidator;

import java.util.HashMap;

import mapData.Field;
import mapData.Position;
import mapEnums.Terrain;
import mapGeneration.HalfMapConfiguration;

public class BorderRule implements IRule {
	private String message = "";

	public BorderRule() {
	}

	@Override
	public void validate(HashMap<Position, Field> mapOfFields, Notification notification) {
		if (!checkAllBorders(mapOfFields)) {
			notification.addError(message);
		}
	}

	private boolean checkAllBorders(HashMap<Position, Field> mapOfFields) {
		boolean topBorder = checkBorderWaterCount(mapOfFields, "top", 0, 0, HalfMapConfiguration.X_HALFMAP_SIZE, 0);
		boolean bottomBorder = checkBorderWaterCount(mapOfFields, "bottom", 0, HalfMapConfiguration.Y_HALFMAP_SIZE - 1,
				HalfMapConfiguration.X_HALFMAP_SIZE, HalfMapConfiguration.Y_HALFMAP_SIZE - 1);
		boolean leftBorder = checkBorderWaterCount(mapOfFields, "left", 0, 0, 0, HalfMapConfiguration.Y_HALFMAP_SIZE);
		boolean rightBorder = checkBorderWaterCount(mapOfFields, "right", HalfMapConfiguration.X_HALFMAP_SIZE - 1, 0,
				HalfMapConfiguration.X_HALFMAP_SIZE - 1, HalfMapConfiguration.Y_HALFMAP_SIZE);

		return topBorder && bottomBorder && leftBorder && rightBorder;
	}

	private boolean checkBorderWaterCount(HashMap<Position, Field> mapOfFields, String borderName, int beginX, int beginY, int endX, int endY) {
		int totalFields = 0;
		int waterFields = 0;

		if (beginX == endX) {
			// Vertical borders
			for (int y = beginY; y < endY; y++) {
				if (isWaterField(mapOfFields, beginX, y)) {
					waterFields++;
				}
				totalFields++;
			}
		} else {
			// Horizontal borders
			for (int x = beginX; x < endX; x++) {
				if (isWaterField(mapOfFields, x, beginY)) {
					waterFields++;
				}
				totalFields++;
			}
		}

		double waterRatio = (double) waterFields / totalFields;
		if (waterRatio >= HalfMapConfiguration.MAX_BORDER_WATER) {
			message = String.format("%d water fields on the %s border exceed the allowed ratio of %.2f!", waterFields,
					borderName, HalfMapConfiguration.MAX_BORDER_WATER);
			return false;
		}
		return true;
	}

	private boolean isWaterField(HashMap<Position, Field> mapOfFields, int x, int y) {
		Field field = mapOfFields.get(new Position(x, y));
		return field != null && field.getTerrain().equals(Terrain.Water);
	}

}
