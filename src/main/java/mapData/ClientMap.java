package mapData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import mapEnums.FortState;
import mapEnums.PlayerPositionState;
import mapEnums.Terrain;

public class ClientMap {
	private Position size;
	private HashMap<Position, Field> fields;

	public ClientMap() {
		this.fields = new HashMap<>();
	}
 
	public ClientMap(int width, int height, HashMap<Position, Field> fields) {
		super();
		this.size = new Position(width, height);
		this.fields = new HashMap<>(fields);
	}

	public ClientMap(HashMap<Position, Field> fields) {
		this.fields = new HashMap<>(fields);
		setMapSizes(fields);
	}

	public void setMapSizes(final HashMap<Position, Field> fields) {
		int maxXCoordinate = fields.keySet().stream().mapToInt(position -> position.getX()).max().orElse(0);
		int maxYCoordinate = fields.keySet().stream().mapToInt(position -> position.getY()).max().orElse(0);

		int trueXSize = maxXCoordinate + 1;
		int trueYSize = maxYCoordinate + 1;

		this.size = new Position(trueXSize, trueYSize);
	}

	public Optional<Position> findPlayerPosition(final PlayerPositionState playerPositionState) {
		for (Map.Entry<Position, Field> entry : fields.entrySet()) {
			Field field = entry.getValue();
			if (field.getPlayerPosition().equals(playerPositionState)
					|| field.getPlayerPosition().equals(PlayerPositionState.BothPlayerPosition)) {
				return Optional.of(entry.getKey());
			}
		}
		return Optional.empty();
	}

	public Field getFieldByPosition(Position position) {
		return fields.get(position);
	}

	public Optional<Position> getFortPosition() {
		return fields.entrySet().stream().filter(entry -> entry.getValue().isFortPresent()).map(Map.Entry::getKey)
				.findFirst();
	}

	public Optional<Position> getEnemyFortPosition() {
		return fields.entrySet().stream().filter(entry -> entry.getValue().isEnemyFortPresent()).map(Map.Entry::getKey)
				.findFirst();
	}

	public Optional<Position> getTreasurePosition() {
		return fields.entrySet().stream().filter(entry -> entry.getValue().isTreasurePresent()).map(Map.Entry::getKey)
				.findFirst();
	}

	public int getWidth() {
		return size.getX();
	}

	public int getHeight() {
		return size.getY();
	}

	public Position getSize() {
		return size;
	}

	public void setSize(Position size) {
		this.size = size;
	}

	public HashMap<Position, Field> getFields() {
		return fields;
	}

	public void setFields(HashMap<Position, Field> fields) {
		this.fields = fields;
	}

}
