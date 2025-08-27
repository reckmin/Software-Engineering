package networkAndConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import exceptions.FailedPlayerRegistrationException;
import exceptions.NetworkException;
import gameData.ClientGameState;
import mapData.ClientMap;
import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ERequestState;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.GameState;
import moveManager.Movement;
import reactor.core.publisher.Mono;

public class NetworkHandler {
	private static final Logger logger = LoggerFactory.getLogger(NetworkHandler.class);
	private final WebClient baseWebClient;
	private String playerId;
	private final String gameId;

	public NetworkHandler(final String serverBaseUrl, final String gameId) {
		this.gameId = gameId;
		this.baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
	}

	public void registerPlayer() throws FailedPlayerRegistrationException {

		PlayerRegistration playerReg = new PlayerRegistration("Valeriia", "Kalinichenko", "kalinichev01");

		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/players")
				.body(BodyInserters.fromValue(playerReg)).retrieve().bodyToMono(ResponseEnvelope.class);

		@SuppressWarnings("unchecked")
		ResponseEnvelope<UniquePlayerIdentifier> resultReg = webAccess.block();
		if (resultReg.getState() == ERequestState.Error) {
			logger.error("Client error, errormessage: " + resultReg.getExceptionMessage());
			throw new FailedPlayerRegistrationException("Tried to register player but failed.");
		} else {
			this.playerId = resultReg.getData().get().getUniquePlayerID();
			logger.info("Player was registered! My Player ID: {}", playerId);
		}
	}

	public void sendHalfMap(final ClientMap map) throws NetworkException {
		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/halfmaps")
				.body(BodyInserters.fromValue(ClientToNetworkConverter.convertClientHalfMap(playerId, map.getFields())))
				.retrieve().bodyToMono(ResponseEnvelope.class);
	
		ResponseEnvelope<FullMap> requestResult = webAccess.block();

		if (requestResult.getState() == ERequestState.Error) {
			logger.error("Client error, errormessage: " + requestResult.getExceptionMessage());
			throw new NetworkException("Error occured during sending the map:" + requestResult.getExceptionMessage());
		} else {
			logger.debug("HalfMap is succesfully send to Server!", map);

		} 
	}

	public ClientGameState getGameState() throws NetworkException {
		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
				.uri("/" + gameId + "/states/" + playerId).retrieve().bodyToMono(ResponseEnvelope.class);

		@SuppressWarnings("unchecked")
		ResponseEnvelope<GameState> requestResult = webAccess.block();

		if (requestResult.getState() == ERequestState.Error) {
			logger.error("Client error, errormessage: " + requestResult.getExceptionMessage());
			throw new NetworkException("Failed to fetch the gameState due to: " + requestResult.getExceptionMessage());
		} else {
			GameState serverGameState = requestResult.getData().get();
			ClientGameState currentGameState = NetworkToClientConverter.convertGameState(serverGameState, playerId);

			return currentGameState;

		}

	}

	public void sendMove(final Movement move) throws NetworkException {
		logger.info("Sending move {}", move.toString());

		final PlayerMove playerMove = ClientToNetworkConverter.convertToPlayerMove(playerId, move);
		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/moves")
				.body(BodyInserters.fromValue(playerMove)).retrieve().bodyToMono(ResponseEnvelope.class);

		@SuppressWarnings("rawtypes")
		ResponseEnvelope requestResult = webAccess.block();

		if (requestResult.getState() == ERequestState.Error) {
			logger.error("Client error, errormessage: " + requestResult.getExceptionMessage());
			throw new NetworkException("Error occured while sending the move: "+ requestResult.getExceptionMessage());
		} else {
			logger.debug("Move is succesfully send to Server!", move.toString());

		}
	}

}
