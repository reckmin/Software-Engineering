package mapGeneration.mapValidator;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mapData.Field;
import mapData.Position;
import mapGeneration.HalfMapConfiguration;

public class TerrainRule implements IRule {
	private static final Logger logger = LoggerFactory.getLogger(TerrainRule.class);
	private String message ="";
	
	public TerrainRule() {
	}

	@Override
	public void validate(HashMap<Position, Field> mapOfFields, Notification notification) {
		if(!checkMinimumTerrains(mapOfFields)) {
			notification.addError(message);
		}
	}

	public boolean checkMinimumTerrains(HashMap<Position, Field> mapOfFields) {
		int waterCount = 0;
		int mountainCount = 0;
		int grassCount = 0;

		for (int y = 0; y < HalfMapConfiguration.Y_HALFMAP_SIZE; y++) {
			for (int x = 0; x < HalfMapConfiguration.X_HALFMAP_SIZE; x++) {

				Position position = new Position(x, y);
				if (mapOfFields.get(position) != null) {
					switch (mapOfFields.get(position).getTerrain()) {
					case Water:
						waterCount++;
						break;
					case Mountain:
						mountainCount++;
						break;
					case Grass:
						grassCount++;
						break;
					}
				}
			}
		}

		boolean waterAmount = waterCount >= HalfMapConfiguration.MIN_WATER;
		boolean mountainAmount = mountainCount >= HalfMapConfiguration.MIN_MOUNTAIN;
		boolean grassAmount = grassCount >= HalfMapConfiguration.MIN_GRASS;

		if (!waterAmount) {
			message += "Water Terrain Amount is lacking: " + waterCount;
			logger.debug("Water Terrain Amount is lacking: {}", waterCount);
		}

		if (!mountainAmount) {
			message += "Mountain Terrain Amount is lacking: " + mountainCount;
			logger.debug("Mountain Terrain Amount is lacking: {}", mountainCount);
		}

		if (!grassAmount) {
			message += "Grass Terrain Amount is lacking: " + grassCount;
			logger.debug("Grass Terrain Amount is lacking: {}", grassCount);
		}

		return waterAmount && mountainAmount && grassAmount;
	}

}
