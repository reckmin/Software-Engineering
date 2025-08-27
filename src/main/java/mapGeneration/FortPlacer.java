package mapGeneration;

import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mapData.Field;
import mapData.Position;
import mapEnums.FortState;
import mapEnums.Terrain;

public class FortPlacer {
	private Random random = new Random();
	private static final Logger logger = LoggerFactory.getLogger(FortPlacer.class);

	public HashMap<Position, Field> placeFort(HashMap<Position, Field> mapOfFields) {
		Position fortPosition;
		
		do {
			fortPosition = getRandomPosition();
		} while(!isTerrainGrass(fortPosition, mapOfFields));
		
		mapOfFields.get(fortPosition).setFortState(FortState.MyFortPresent);
		logger.debug("Castle is placed at position: x = {}, y =  {} ", fortPosition.getX(), fortPosition.getY());
		
		return mapOfFields;
	}
	
	private Position getRandomPosition() {
		int xPosition = random.nextInt(HalfMapConfiguration.X_HALFMAP_SIZE);
		int yPosition = random.nextInt(HalfMapConfiguration.Y_HALFMAP_SIZE);
		
		return new Position(xPosition, yPosition);
	}
	
	private boolean isTerrainGrass(Position position, HashMap<Position, Field> mapOfFields) {
		Field field = mapOfFields.getOrDefault(position, new Field(Terrain.Grass));
		return field.getTerrain() == Terrain.Grass;
	}
	

}

