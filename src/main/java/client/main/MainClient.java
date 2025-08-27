package client.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gameData.ClientController;
import gameData.models.MapGenerationErrorLogModel;
import gameData.models.MapModel;
import gameData.views.FinalStateView;
import gameData.views.MapGenerationErrorLogView;
import gameData.views.MapView;
import moveManager.MoveManager;
import networkAndConverter.NetworkHandler;

public class MainClient {
	private static Logger logger = LoggerFactory.getLogger(MainClient.class); 

	public static void main(String[] args) {

		String serverBaseUrl = args[1];
		String gameId = args[2];
		
		MapModel mapModel = new MapModel();
		MapView mapView = new MapView(); 
		mapModel.attach(mapView);
		
		FinalStateView stateView = new FinalStateView();
		mapModel.attach(stateView);
		
		MapGenerationErrorLogModel errorLogModel = new MapGenerationErrorLogModel();
		MapGenerationErrorLogView errorLogView = new MapGenerationErrorLogView();
		errorLogModel.attach(errorLogView);
		
		NetworkHandler network = new NetworkHandler(serverBaseUrl, gameId);
		MoveManager manager = new MoveManager();
		
		try {
		ClientController controller = new ClientController(network, mapModel, errorLogModel, manager);
		logger.info("Start of Client ...");
		controller.start();
		} catch (Exception e) {
			e.printStackTrace();
			 System.exit(1);
		}

	} 

}
