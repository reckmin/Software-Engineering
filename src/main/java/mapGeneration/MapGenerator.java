package mapGeneration;

import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gameData.models.MapGenerationErrorLogModel;
import mapData.ClientMap;
import mapData.Field;
import mapData.Position;
import mapGeneration.mapValidator.HalfMapValidator;

public class MapGenerator {
	private static final Logger logger = LoggerFactory.getLogger(MapGenerator.class);
	private ClientMap halfMap;
	private HashMap<Position, Field> mapOfFields = new HashMap<>();
	private MapGenerationErrorLogModel errorLogModel;
	
	private FortPlacer fortPlacer = new FortPlacer();
	
	public MapGenerator(MapGenerationErrorLogModel errorLogModel) {
		this.mapOfFields = new HashMap<>();
		this.halfMap = new ClientMap(HalfMapConfiguration.X_HALFMAP_SIZE, HalfMapConfiguration.Y_HALFMAP_SIZE, mapOfFields);
		this.errorLogModel = errorLogModel;
		 
	}
	
	public ClientMap geneteHalfMap(){
		boolean isValid = false;
		int attempts = 0;
		
		do {
			attempts++;
			this.mapOfFields = new HashMap<>();
			
			TerrainGenerator terrainGenerator = new TerrainGenerator(mapOfFields);
			this.mapOfFields = terrainGenerator.generateMapOfFields();
			this.mapOfFields = fortPlacer.placeFort(mapOfFields);
			HalfMapValidator halfMapValidator = new HalfMapValidator();
			
			isValid = halfMapValidator.isHalfMapValid(mapOfFields);
			if(!isValid) {
				errorLogModel.setValidator(halfMapValidator);
			}
			
			logger.debug("MapValidation {}", isValid);
		} while(!isValid && attempts < 30);
		
		if(!isValid) {
			logger.debug("Failed to generate map after {} attempts", attempts);
			throw new IllegalArgumentException("Failed to generate a valid half map!");
		}
		logger.debug("Map was successfully generated after {} attempts", attempts);
		this.halfMap = new ClientMap(mapOfFields);
		
		return halfMap;
	}
	
}
