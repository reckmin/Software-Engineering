package networkAndConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.InvalidEnumConversionException;
import mapData.Field;
import mapData.Position;
import mapEnums.Terrain;
import messagesbase.messagesfromclient.EMove;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromclient.PlayerMove;
import moveManager.Movement;

public class ClientToNetworkConverter {
	private static final Logger logger = LoggerFactory.getLogger(ClientToNetworkConverter.class);
	
	public static ETerrain convertTerrain (Terrain terrain) {
		switch(terrain) {
		case Grass:
			return ETerrain.Grass;
		case Mountain:
			return ETerrain.Mountain;
		case Water: 
			return ETerrain.Water;
		default:
			throw new InvalidEnumConversionException("Unexpected terrain type! - "+ terrain);
		}	
	}
	
	public static PlayerHalfMap convertClientHalfMap(final String playerId, final HashMap<Position, Field> halfMap) {
		final List<PlayerHalfMapNode> halfMapNode = halfMap.entrySet().stream().map((Map.Entry<Position, Field> entry) -> {
			 final Position position = entry.getKey();
		     final Field field = entry.getValue();
		        
		     final boolean fortPresent = field.isFortPresent();
		     final int x = position.getX();
		     final int y = position.getY();
		     final ETerrain terrain = convertTerrain(field.getTerrain());
		        
		     return new PlayerHalfMapNode(x, y, fortPresent, terrain);
		}).collect(Collectors.toList()); 
		return new PlayerHalfMap(playerId, halfMapNode);
	}
	
	public static PlayerMove convertToPlayerMove (final String playerId, Movement movement) {
		logger.info("Converting movement {}", movement);
		final EMove move = convertMove(movement);
		final PlayerMove playerMove = PlayerMove.of(playerId, move);
		return playerMove;
	}
	
	private static EMove convertMove(final Movement move) {
		logger.debug("Movement {}", move.toString());
		switch(move) {
		case Down:
			return EMove.Down;
		case Up:
			return EMove.Up;
		case Right:
			return EMove.Right;
		case Left:
			return EMove.Left;
		default:
			throw new InvalidEnumConversionException("Unexpected Movement type!"+ move);
		}
	}
	
}
