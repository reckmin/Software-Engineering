package mapGeneration.mapValidator;

import java.util.HashMap;
import java.util.Map;

import mapData.Field;
import mapData.Position;
import mapEnums.FortState;
import mapEnums.Terrain;

public class FortRule implements IRule {
	private String message = "";

	public FortRule() {
	}

	@Override
	public void validate(HashMap<Position, Field> mapOfFields, Notification notification) {
		if (!hasFortOnGrass(mapOfFields)) {
			message = "No fort is present on any grass field!";
			notification.addError(message);
		}
	}

	private boolean hasFortOnGrass(HashMap<Position, Field> mapOfFields) {
		for (Map.Entry<Position, Field> entry : mapOfFields.entrySet()) {
			Field field = entry.getValue();
			if (field.getFortState().equals(FortState.MyFortPresent) && field.getTerrain().equals(Terrain.Grass)) {
				return true;
			}
		}
		return false;
	}

}