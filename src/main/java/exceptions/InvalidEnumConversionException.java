package exceptions;

public class InvalidEnumConversionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidEnumConversionException(String message) {
		super(message);
	}

}
