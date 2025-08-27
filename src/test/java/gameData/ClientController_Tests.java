package gameData;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import exceptions.NetworkException;
import gameData.models.MapGenerationErrorLogModel;
import gameData.models.MapModel;
import mapData.ClientMap;
import moveManager.MoveManager;
import networkAndConverter.NetworkHandler;

public class ClientController_Tests {

	private NetworkHandler networkMocked;
	private MoveManager moveManagerMocked;
	private MapModel mapModel;
	private MapGenerationErrorLogModel errorLogModel;
	private ClientController controller;

	@BeforeEach
	void setUp() {
		networkMocked = Mockito.mock(NetworkHandler.class);
		moveManagerMocked = Mockito.mock(MoveManager.class);

		mapModel = new MapModel();
		errorLogModel = new MapGenerationErrorLogModel();

		controller = new ClientController(networkMocked, mapModel, errorLogModel, moveManagerMocked);
	}

	 @Test
	    public void start_shouldSendHalfMapExactlyOnce() {
	        try {
	            Mockito.when(networkMocked.getGameState()).thenReturn(
	                    new ClientGameState("1", PlayerGameState.MustWait, new ClientMap(), false),
	                    new ClientGameState("2", PlayerGameState.MustAct, new ClientMap(), false),
	                    new ClientGameState("3", PlayerGameState.Won, new ClientMap(), false));

	        } catch (NetworkException e) {
	            e.printStackTrace();
	            Assertions.fail();
	        }

	        controller.start();

	        try {
	            Mockito.verify(networkMocked, times(1)).sendHalfMap(any());
	        } catch (Exception e) {
	            e.printStackTrace();
	            Assertions.fail();
	        }
	    }

	    @Test
	    public void start_shouldNotSendMovesWhenStateRepeatsAndEndGameOnWin() {
	        ClientGameState sameState = new ClientGameState("1", PlayerGameState.MustAct, new ClientMap(), false);
	        ClientGameState wonState = new ClientGameState("2", PlayerGameState.Won, new ClientMap(), false);

	        try {
				Mockito.when(networkMocked.getGameState()).thenAnswer(invocation -> {
				    Thread.sleep(400);
				    return sameState;
				}).thenAnswer(invocation -> {
				    Thread.sleep(4000);
				    return wonState;
				});
			} catch (NetworkException e) {
				e.printStackTrace();
				 Assertions.fail();
			}

	        controller.start();

	        try {
				Mockito.verify(networkMocked, Mockito.never()).sendMove(any());
			} catch (NetworkException e) {
				e.printStackTrace();
				 Assertions.fail();
			}
	        Assertions.assertTrue(mapModel.getClientGameState().getPlayerGameState().equals(PlayerGameState.Won), "The game state should end with a win.");
	    }

}
