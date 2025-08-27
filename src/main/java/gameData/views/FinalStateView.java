package gameData.views;

import gameData.ClientGameState;
import gameData.PlayerGameState;
import gameData.models.AModel;

public class FinalStateView implements Observer  {

	@Override
	public void update(AModel model) {
		if (!(model instanceof ClientGameState))
			return;
		ClientGameState state = (ClientGameState) model; 
		
		if(state.getPlayerGameState().equals(PlayerGameState.Won)) System.out.print(ColoredEmojis.WON);
		
		if(state.getPlayerGameState().equals(PlayerGameState.Lost)) System.out.print(ColoredEmojis.LOST);
 	} 

} 
 