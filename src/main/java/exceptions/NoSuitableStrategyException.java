package exceptions;

public class NoSuitableStrategyException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public NoSuitableStrategyException(String message) {
		super(message);
	}
}
