package exceptions;

public class FailedPlayerRegistrationException extends Exception{
	private static final long serialVersionUID = 1L;

	public FailedPlayerRegistrationException(String message) {
		super(message);
	}
}
