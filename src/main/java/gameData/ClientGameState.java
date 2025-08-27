package gameData;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gameData.models.AModel;
import mapData.ClientMap;
import mapData.Position;
import mapEnums.PlayerPositionState;

public class ClientGameState extends AModel {
	private static final Logger logger = LoggerFactory.getLogger(ClientGameState.class);

	private String gameStateId;
	private PlayerGameState playerGameState;
	private ClientMap map;
	private Optional<Position> playerPosition;
	private Optional<Position> enemyPosition;
	private boolean collectedTreasure;

	private Optional<Position> treasurePosition;
	private Optional<Position> enemyFortPosition;

	public ClientGameState() {
		this.playerGameState = PlayerGameState.MustWait; 
		this.map = new ClientMap();
		this.gameStateId = "";
		this.collectedTreasure = false;
		this.playerPosition = Optional.empty();
		this.enemyPosition = Optional.empty();
		this.treasurePosition = Optional.empty();
		this.enemyFortPosition = Optional.empty();
	}

	public ClientGameState(String gameStateId, PlayerGameState playerGameState, ClientMap map, boolean treasureState) {
		this.gameStateId = gameStateId;
		this.playerGameState = playerGameState;
		this.map = map;
		this.collectedTreasure = treasureState;
		this.playerPosition = Optional.empty();
		this.enemyPosition = Optional.empty();
		this.treasurePosition = Optional.empty();
		this.enemyFortPosition = Optional.empty();
		if (!map.getFields().isEmpty()) {
			setPlayerPosition();
			setEnemyPosition();
		}
		if (map.getTreasurePosition().isPresent()) {
			logger.debug("TreasurePosition was found!");
			treasurePosition = map.getTreasurePosition();
		}
	}

	public boolean isMyTurn() {
		return this.playerGameState.equals(PlayerGameState.MustAct);
	}

	public boolean hasGameEnded() {
		return playerGameState.equals(PlayerGameState.Lost) || playerGameState.equals(PlayerGameState.Won);
	}

	public boolean isCollectedTreasure() {
		return collectedTreasure;
	}
	
	public boolean isTreasureVisible() {
		return treasurePosition.isPresent();
	}

	public boolean isEnemyCastleVisible() {
		if (this.map.getEnemyFortPosition().isPresent()) {
			this.enemyFortPosition = this.map.getEnemyFortPosition();
			return true;
		}
		return false;
	}

	public void setTreasurePosition(Position position) {
		this.treasurePosition = Optional.of(position);
	}

	public Optional<Position> getTreasurePosition() {
		return this.treasurePosition;
	}

	public Optional<Position> getEnemyFortPosition() {
		return this.enemyFortPosition;
	}

	public String getGameStateId() {
		return this.gameStateId;
	}

	public PlayerGameState getPlayerGameState() {
		return playerGameState;
	}

	public ClientMap getMap() {
		return map;
	}

	public Optional<Position> getPlayerPosition() {
		return playerPosition;
	}

	public void setPlayerPosition() {
		this.playerPosition = this.map.findPlayerPosition(PlayerPositionState.MyPlayerPosition);
	}

	public Optional<Position> getEnemyPosition() {
		return enemyPosition;
	}

	public void setEnemyPosition() {
		this.enemyPosition = this.map.findPlayerPosition(PlayerPositionState.EnemyPlayerPosition);
	}

	public void printMyCurrentPosition() {
		logger.info("PlayerPosition :");
		setPlayerPosition();
		if (playerPosition != null)
			logger.info(playerPosition.toString());
		else
			logger.warn("was not determined");
	}

	@Override
	public String toString() {
		return "GameState [gameStateId=" + gameStateId + ", playerGameState=" + playerGameState.toString() + ", map="
				+ map.getSize() + ", collectedTreasure=" + collectedTreasure + "]";
	}

}
