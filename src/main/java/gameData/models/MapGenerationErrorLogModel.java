package gameData.models;

import mapGeneration.mapValidator.HalfMapValidator;

public class MapGenerationErrorLogModel implements Observable{
	private HalfMapValidator validator = new HalfMapValidator();

	
	public void setValidator(final HalfMapValidator validator) {
		this.validator = validator;
		notifyObservers(validator);
	}
	
	public HalfMapValidator getClientGameState() {
		return (HalfMapValidator) this.validator;
	}
}
