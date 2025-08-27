package exceptions;

public class TargetNotSetException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public TargetNotSetException (String message) {
		super(message);
	}

}
