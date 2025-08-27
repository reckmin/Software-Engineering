package mapGeneration.mapValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Notification {
	private final List<String> errors = new ArrayList<>();

	public void addError(String message) {
		errors.add(message);
	}

	public boolean hasErrors() {
		return !errors.isEmpty();
	}


    public String getErrors() {
        return errors.stream().collect(Collectors.joining(", "));
    }

    public List<String> getErrorList() {
        return new ArrayList<>(errors); 
    }
}
