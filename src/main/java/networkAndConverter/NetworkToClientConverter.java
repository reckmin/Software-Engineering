package networkAndConverter;

import java.util.HashMap;

import exceptions.InvalidEnumConversionException;
import exceptions.PlayerNotFoundException;
import gameData.ClientGameState;
import gameData.PlayerGameState;
import mapData.ClientMap;
import mapData.Field;
import mapData.Position;
import mapEnums.FortState;
import mapEnums.PlayerPositionState;
import mapEnums.Terrain;
import mapEnums.TreasureState;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;

public class NetworkToClientConverter {
	public static Terrain convertETerrain(ETerrain terrain) {
		switch(terrain) {
		case Grass:
			return Terrain.Grass;
		case Mountain:
			return Terrain.Mountain;
		case Water:
			return Terrain.Water;
		default:
			throw new InvalidEnumConversionException("Unexpected ETerrain Type!" + terrain);
		}
	}

	public static PlayerGameState convertEPlayerGameState(EPlayerGameState playerGameState) {
		switch(playerGameState) {
		case Lost:
			return PlayerGameState.Lost;
		case MustAct:
			return PlayerGameState.MustAct;
		case MustWait: 
			return PlayerGameState.MustWait;
		case Won:
			return PlayerGameState.Won;
		default:
			throw new InvalidEnumConversionException("Unexpected EPlayerGameState Type!" + playerGameState);
		}
	}
		

	public static FortState convertFortState(EFortState fortState) {
	    switch (fortState){
	    case EnemyFortPresent:
	      return FortState.EnemyFortPresent;
	    case MyFortPresent:
	      return FortState.MyFortPresent;
	    case NoOrUnknownFortState:  
	      return FortState.NoOrUnknown;
	    default:
	      throw new InvalidEnumConversionException("Unexpected EFortState TYPE!" + fortState);
	    }  

	  }
		
	public static TreasureState convertETreasureState(ETreasureState treasureState) {
		switch(treasureState) {
		case MyTreasureIsPresent:
			return TreasureState.MyTreasurePresent;
		case NoOrUnknownTreasureState:
			return TreasureState.NoOrUnknownTreausureState;
		default:
			throw new InvalidEnumConversionException("Unexpected TreasureState Type!" + treasureState);
		}
	}
		
	public static PlayerPositionState convertPlayerPositionState(EPlayerPositionState playerPositionState) {
	    switch (playerPositionState){
	    case BothPlayerPosition:
	      return PlayerPositionState.BothPlayerPosition;
	    case EnemyPlayerPosition:
	      return PlayerPositionState.EnemyPlayerPosition;
	    case MyPlayerPosition:
	      return PlayerPositionState.MyPlayerPosition;
	    case NoPlayerPresent:
	      return PlayerPositionState.NoPlayerPresent;
	    default:
	      throw new InvalidEnumConversionException("Unexpected EPlayerPositionState Type" + playerPositionState);
	    }  
	}
	
	public static ClientMap convertServerFullMap(final FullMap fullMap) {
		HashMap<Position, Field> mapOfFields = new HashMap<>();
	    
	    fullMap.getMapNodes().stream().forEach(node -> {
	        Position position = new Position(node.getX(), node.getY());
	        Field field = new Field(
	            convertETerrain(node.getTerrain()), 
	            convertFortState(node.getFortState()), 
	            convertETreasureState(node.getTreasureState()), 
	            convertPlayerPositionState(node.getPlayerPositionState())
	        );
	        mapOfFields.putIfAbsent(position, field); 
	    });
	    return new ClientMap(mapOfFields);
	}	


	public static ClientGameState convertGameState(GameState gameState, String playerId) {
		
		String gameStateId = gameState.getGameStateId();
		ClientMap map = convertServerFullMap(gameState.getMap());

		for(PlayerState playerState : gameState.getPlayers()) {
			if(playerState.getUniquePlayerID().equals(playerId)) {
				PlayerGameState playerGameState = convertEPlayerGameState(playerState.getState());
				boolean treasureState = playerState.hasCollectedTreasure();
				
				return new ClientGameState(gameStateId, playerGameState, map, treasureState);
			}
		}
		throw new PlayerNotFoundException(String.format("No PlayerState with playerId %s found in this GameState", playerId));
	}
	
}
