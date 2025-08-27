package gameData.models;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gameData.ClientGameState;
import mapData.Position;

public class MapModel implements Observable{
	private static final Logger logger = LoggerFactory.getLogger(MapModel.class);
	
	private ClientGameState clientState = new ClientGameState();
	private Optional<Position> treasurePosition = Optional.empty();
	private int round= 0;
 
	
	public void setClientGameState(final ClientGameState newClientState) {
		this.clientState = newClientState;
		handleTreasurePosition(); 
		notifyObservers(clientState);
	}
	
	public ClientGameState getClientGameState() {
		return this.clientState;
	}
	
	public void setTreasurePosition(Position position) {
		this.treasurePosition=Optional.of(position);
	}
	
	public Optional<Position> getTreasurePosition(){
		return this.treasurePosition;
	}
	
	public void handleTreasurePosition() {
		// SAVE THE TREASURE POSITION
		if (treasurePosition.isEmpty()) {
			if (clientState.isTreasureVisible()) {
				treasurePosition = clientState.getTreasurePosition();
				logger.trace("Discovered treasure at {} by going on Mountain", treasurePosition);
			} else if (clientState.isCollectedTreasure()) {
				treasurePosition = clientState.getPlayerPosition();
				logger.trace("Discovered new treasure at {} by passing Grass", treasurePosition);
			}
		}

		// SET THE TREASURE POSITION
		if (treasurePosition.isPresent() && clientState.getTreasurePosition().isEmpty()) {
			clientState.setTreasurePosition(treasurePosition.get());
			logger.info("Setting position for current state: {}", clientState.getTreasurePosition());
		}
	}
	
	public void addRound() {
		round++;
		logger.info("Turn {} ", round);
	}
}
