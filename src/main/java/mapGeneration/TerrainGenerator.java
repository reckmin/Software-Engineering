package mapGeneration;

import java.util.HashMap;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mapData.Field;
import mapData.Position;
import mapEnums.Terrain;

public class TerrainGenerator {
	private static final Logger logger = LoggerFactory.getLogger(TerrainGenerator.class);
	private Random random = new Random();
	
	private Terrain[] terrains = Terrain.values();
	private HashMap<Position, Field> mapOfFields;
	
	TerrainGenerator(HashMap<Position, Field> mapOfFields){
		this.mapOfFields = new HashMap<>(mapOfFields);
	}
	
	
	public HashMap<Position, Field> generateMapOfFields(){
		generateFields(Terrain.Water, HalfMapConfiguration.MIN_WATER);
		generateFields(Terrain.Mountain, HalfMapConfiguration.MIN_MOUNTAIN);
		generateFields(Terrain.Grass, HalfMapConfiguration.MIN_GRASS);
		
		generateRemainingFields();  
		
		return this.mapOfFields;
	}
	
	private void generateFields(Terrain terrain, int amountOfFields) {
		for(int count = 0; count < amountOfFields;) {
			Position newPosition = getRandomPosition();
			
			if(!this.mapOfFields.containsKey(newPosition)) {
				this.mapOfFields.put(newPosition, new Field(terrain));
				count++;
				
				logger.debug("New Field of {} Terrain at {} was added", terrain, newPosition);
			}
		}
	}
	
	private void generateRemainingFields() {
		for(int y = 0; y < HalfMapConfiguration.Y_HALFMAP_SIZE; ++y) {
			for(int x = 0; x < HalfMapConfiguration.X_HALFMAP_SIZE; ++x) {
				
				Position newPosition = new Position(x, y);
				
				if(this.mapOfFields.get(newPosition) == null) {
					Terrain randomTerrain = terrains[random.nextInt(terrains.length)];
					this.mapOfFields.put(newPosition, new Field(randomTerrain));
					logger.debug("New Field of {} Terrain at {} was added", randomTerrain, newPosition);
				}
			}
		}
	}
	
	
	private Position getRandomPosition() {
		int xPosition = random.nextInt(HalfMapConfiguration.X_HALFMAP_SIZE);
		int yPosition = random.nextInt(HalfMapConfiguration.Y_HALFMAP_SIZE);
		
		return new Position(xPosition, yPosition);
	}
}
