package moveManager;

import mapData.Position;

public enum Movement {
	Up(0, 1), Down(0, -1), Right(1, 0), Left(-1, 0);

	private final int x;
	private final int y;

	Movement(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x; 
	}

	public int getY() {
		return y;
	}

	public Position apply(Position position) {
		return new Position(position.getX() + x, position.getY() + y);
	}

	public boolean isEqual(Movement move) {
		if (this == move)
			return true;
		if (move == null)
			return false;

		return (this.x == move.x && this.y == move.y);
	}
}
