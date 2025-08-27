package moveManager.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mapData.Field;
import mapData.Position;
import mapEnums.Terrain;
import mapGeneration.HalfMapConfiguration;

public class TargetNotLocatedStrategy_Tests {

	private HashMap<Position, Field> mapOfFields;

	@BeforeEach
	public void setUp() {
		mapOfFields = new HashMap<>();

		mapOfFields = new HashMap<>();
		for (int x = 0; x < HalfMapConfiguration.X_HALFMAP_SIZE; x++) {
			for (int y = 0; y < HalfMapConfiguration.Y_HALFMAP_SIZE; y++) {
				if ((y == 0 && x == 3) || (y == 1 && x == 2)) {
					mapOfFields.put(new Position(x, y), new Field(Terrain.Water));
				} else if ((y == 2 && x == 2) || (y == 3 && x == 0) || (y == 3 && x == 1) || (y == 3 && x == 2)) {
					mapOfFields.put(new Position(x, y), new Field(Terrain.Mountain));
				} else {
					mapOfFields.put(new Position(x, y), new Field(Terrain.Grass));
				}
			}
		}

	}

	@Test
	public void calculatePath_shouldContainAllGrassFields() {
		TargetNotLocatedStrategy strategy = new TargetNotLocatedStrategy(mapOfFields);
		List<Map.Entry<Position, Field>> path = strategy.calculatePath(new Position(2, 0));
		HashMap<Position, Field> allGrassFields = new HashMap<>();
		for (Map.Entry<Position, Field> entry : mapOfFields.entrySet()) {
			if (entry.getValue().getTerrain().equals(Terrain.Grass)) {
				allGrassFields.put(entry.getKey(), entry.getValue());
			}
		}

		for (Map.Entry<Position, Field> entry : path) {
			Position posInPath = entry.getKey();
			Field fieldInPath = entry.getValue();
			
			if (allGrassFields.containsKey(posInPath)) {
				Field fieldAllGrass = allGrassFields.get(posInPath);
				
				if (fieldAllGrass.equals(fieldInPath)) {
					allGrassFields.remove(posInPath);
				}
			}
		}
		System.out.println(allGrassFields.toString());
		allGrassFields.remove(new Position(2, 0));
		System.out.println("Remaining grass fields: " + allGrassFields);
		assertEquals(0, allGrassFields.size(), "Not all grass fields are in path!");

	}
}
