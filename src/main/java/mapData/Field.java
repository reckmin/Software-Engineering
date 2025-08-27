package mapData;

import mapEnums.FortState;
import mapEnums.PlayerPositionState;
import mapEnums.Terrain;
import mapEnums.TreasureState;

public class Field {
	private final Terrain terrain;
	private FortState fortState;
	private TreasureState treasureState;
	private PlayerPositionState playerPosition;

	public Field(Terrain terrain) {
		this.terrain = terrain;
		this.fortState = FortState.NoOrUnknown;
		this.treasureState = TreasureState.NoOrUnknownTreausureState;
		this.playerPosition = PlayerPositionState.NoPlayerPresent;
	}

	public Field(Terrain terrain, FortState fortState, TreasureState treasureState, PlayerPositionState playerPosition) {
		super();
		this.terrain = terrain;
		this.fortState = fortState; 
		this.treasureState = treasureState;
		this.playerPosition = playerPosition;
	}

	public boolean isFortPresent() {
		return this.fortState.equals(FortState.MyFortPresent);
	}

	public boolean isEnemyFortPresent() {
		return this.fortState.equals(FortState.EnemyFortPresent);
	}

	public boolean isTreasurePresent() {
		return this.treasureState.equals(TreasureState.MyTreasurePresent);
	}

	public boolean isPlayerPresent() {
		return this.playerPosition.equals(PlayerPositionState.MyPlayerPosition)
				|| this.playerPosition.equals(PlayerPositionState.BothPlayerPosition);
	}

	public FortState getFortState() {
		return fortState;
	}

	public void setFortState(FortState fortState) {
		this.fortState = fortState;
	}

	public TreasureState getTreasureState() {
		return treasureState;
	}

	public void setTreasureState(TreasureState treasureState) {
		this.treasureState = treasureState;
	}


	public PlayerPositionState getPlayerPosition() {
		return playerPosition;
	}

	public void setPlayerPosition(PlayerPositionState playerPosition) {
		this.playerPosition = playerPosition;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	@Override
	public String toString() {
		return "Field [treasureState=" + treasureState + "]";
	}
	
	
	
}
