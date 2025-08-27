package mapGeneration.mapValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gameData.models.AModel;
import mapData.Field;
import mapData.Position;

public class HalfMapValidator extends AModel {
	private final List<IRule> rules = new ArrayList<>();
	private Notification notification = new Notification();
	private int mapValidationCount = 0;

	public HalfMapValidator() {
		rules.add(new TerrainRule());
		rules.add(new IslandRule());
		rules.add(new BorderRule());
		rules.add(new FortRule());

	}

	public boolean isHalfMapValid(HashMap<Position, Field> mapOfFields) {
		mapValidationCount++;
		notification = new Notification();
		for (IRule rule : rules) {
			rule.validate(mapOfFields, notification);
		}
		return getIsMapValid();
	}

	public List<String> getErrorMessages() {
		return notification.getErrorList();
	}

	public boolean getIsMapValid() {
		return !notification.hasErrors();
	}

	public int getMapValidationCount() {
		return mapValidationCount;
	}

}
