package moveManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import exceptions.InvalidMoveException;
import mapData.Position;

public class PathToMovesConverter_Tests {
	
	@Test
	public void getDirection_withUnreachablePosition_shouldThrowException() {
		
		Executable testCode = () -> PathToMovesConverter.getDirection(new Position(0,0), new Position(2,5));

		Assertions.assertThrows(InvalidMoveException.class, testCode,
				"We expected a exception because the move was invalid, but it was not thrown");
	}

}
 