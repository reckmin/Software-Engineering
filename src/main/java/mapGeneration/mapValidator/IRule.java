package mapGeneration.mapValidator;

import java.util.HashMap;

import mapData.Field;
import mapData.Position;

public interface IRule {
	
	void validate(HashMap<Position, Field> mapOfFields, Notification notification);
}
