package mapGeneration.mapValidator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mapData.Field;
import mapData.Position;
import mapEnums.Terrain;
import mapGeneration.HalfMapConfiguration;

public class BorderRule_Tests {

	private HashMap<Position, Field> mapOfFields;

	@BeforeEach
	void setUp() {
		mapOfFields = new HashMap<>();
		for (int x = 0; x < HalfMapConfiguration.X_HALFMAP_SIZE; x++) {
			for (int y = 0; y < HalfMapConfiguration.Y_HALFMAP_SIZE; y++) {
				if (y == 0) {
					mapOfFields.put(new Position(x, y), new Field(Terrain.Water));
				} else {
					mapOfFields.put(new Position(x, y), new Field(Terrain.Grass));
				}
			}
		}
	}

	@Test
	public void validate_WaterOnTopBorderExceedsLimit_shouldAddNotification() {
		BorderRule borderRule = new BorderRule();
		Notification notification = new Notification();
		borderRule.validate(mapOfFields,notification);

		assertTrue(notification.hasErrors(), "Should fail since top Border has too much water");
	}

}
