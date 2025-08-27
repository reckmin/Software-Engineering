package mapGeneration.mapValidator;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import mapData.Field;
import mapData.Position;
import mapEnums.FortState;
import mapEnums.Terrain;
import mapGeneration.HalfMapConfiguration;

public class FortRule_Tests {
	private HashMap<Position, Field> mapOfFields;
	private Notification notification;
	private FortRule fortRule;

	@BeforeEach
	public void setUp() {
		mapOfFields = new HashMap<>();

		for (int x = 0; x < HalfMapConfiguration.X_HALFMAP_SIZE; x++) {
			for (int y = 0; y < HalfMapConfiguration.Y_HALFMAP_SIZE; y++) {
				if(x == 0) {
					mapOfFields.put(new Position(x, y), new Field(Terrain.Water));
				}
				else {
					mapOfFields.put(new Position(x, y), new Field(Terrain.Grass));
				}
			}
		}

		notification = new Notification();
		fortRule = new FortRule();
	}
	
	@Test
	public void validate_fortIsNotPresent_shouldAddNotificationMessage() {
		fortRule.validate(mapOfFields,notification);
		assertTrue(notification.hasErrors(), "Should fail since fort is not present");
	}
	
	@Test
	public void validate_fortIsLocatedNotOnGrass_shouldAddNotificationMessage() {
		mapOfFields.get(new Position(0, 1)).setFortState(FortState.MyFortPresent);
		fortRule.validate(mapOfFields,notification);
		assertTrue(notification.hasErrors(), "Should fail since fort is not located on grass field");
	}
}
