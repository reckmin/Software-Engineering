package moveManager;



import exceptions.InvalidMoveException;
import mapData.Position;

public class PathToMovesConverter {

	public static Movement getDirection(Position from, Position to) {
		int xDifference = to.getX() - from.getX();
		int yDifference = to.getY() - from.getY();
		
		// comparing Movement values to difference in coordinates between fields
		for (Movement movement : Movement.values()) {
			if (movement.getX() == xDifference && movement.getY() == -yDifference) {
				return movement;
			}
		}
		throw new InvalidMoveException("Invalid Movement between positions: " + from + " to " + to);
	}

} 
