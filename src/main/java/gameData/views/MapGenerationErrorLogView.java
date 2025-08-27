package gameData.views;

import gameData.models.AModel;
import mapGeneration.mapValidator.HalfMapValidator;

public class MapGenerationErrorLogView implements Observer  {

	@Override
	public void update(AModel model) {
		if (!(model instanceof HalfMapValidator))
			return;
		HalfMapValidator validator = (HalfMapValidator) model;
	
		
        if (!validator.getIsMapValid()) {
            System.out.println(String.format("Validation %d failed! Errors:", validator.getMapValidationCount()));
            validator.getErrorMessages().forEach(System.out::println);
        } else {
            System.out.println("Validation successful! No errors found.");
        }
		
	}
}
