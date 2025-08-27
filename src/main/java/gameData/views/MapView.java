package gameData.views;

import java.util.Optional;

import gameData.ClientGameState;
import gameData.models.AModel;
import mapData.ClientMap;
import mapData.Field;
import mapData.Position;
import mapEnums.Terrain;

public class MapView implements Observer {

	@Override
	public void update(AModel model) {
		if (!(model instanceof ClientGameState))
			return;
		ClientGameState clientState = (ClientGameState) model;
		if(clientState.getEnemyPosition().isEmpty() || clientState.getPlayerPosition().isEmpty()) {
			return;
		}
		drawMap(clientState.getMap(), clientState.getPlayerPosition().get(), clientState.getEnemyPosition().get(), 
				clientState.isCollectedTreasure(), clientState.getTreasurePosition());
	}

	private void drawMap(ClientMap map, Position playerPosition, Position enemyPosition, boolean isTreasureCollected,
			Optional<Position> treasurePosition) {
		int width = map.getWidth();
		int height = map.getHeight(); 

		for (int y = 0; y < height; y++) {
			StringBuilder rowBuilder = new StringBuilder();
			for (int x = 0; x < width; x++) {
				Position position = new Position(x, y);
				Field field = map.getFieldByPosition(position);
				String tileEmoji = getTileEmoji(field, position, playerPosition, enemyPosition, isTreasureCollected,
						treasurePosition);
				rowBuilder.append(" ").append(tileEmoji);
			}
			System.out.println(rowBuilder.toString());
		}
		System.out.println("------------------------------------------------------------");

	} 

	private String getTileEmoji(Field field, Position pos, Position playerPos, Position enemyPos,
			boolean isTreasureCollected, Optional<Position> treasurePosition) {

		if (field == null) {
			return ColoredEmojis.UNKNOWN;
		}

		String terrainEmoji = getTerrainEmoji(field.getTerrain());

		if (field.isFortPresent()) {
			terrainEmoji = ColoredEmojis.MY_CASTLE;
		} else if (field.isEnemyFortPresent()) {
			terrainEmoji = ColoredEmojis.ENEMY_CASTLE;
		}

		if (isTreasureCollected && treasurePosition.isPresent() && pos.equals(treasurePosition.get())) {
			terrainEmoji = ColoredEmojis.TREASURE_FOUND;
		} else if (field.isTreasurePresent()) {
			terrainEmoji = ColoredEmojis.TREASURE_NOT_FOUND;
		}

		if (pos.equals(playerPos) && pos.equals(enemyPos)) {
			terrainEmoji = ColoredEmojis.BOTH_PLAYER_POSITION;
		} else if (pos.equals(playerPos)) {
			terrainEmoji = ColoredEmojis.MY_PLAYER_POSITION;
		} else if (pos.equals(enemyPos)) {
			terrainEmoji = ColoredEmojis.ENEMY_PLAYER_POSITION;
		}

		return terrainEmoji;
	}

	private String getTerrainEmoji(Terrain terrain) {
		switch (terrain) {
		case Grass:
			return ColoredEmojis.GRASS;
		case Mountain:
			return ColoredEmojis.MOUNTAIN;
		case Water:
			return ColoredEmojis.WATER;
		default:
			return ColoredEmojis.UNKNOWN;
		}
	}

}
