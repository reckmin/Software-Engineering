package gameData.models;

import java.util.ArrayList;
import java.util.List;

import gameData.views.Observer;

public interface Observable {
	List<Observer> observers = new ArrayList<>();
	
	default void attach(Observer observer) {
		observers.add(observer);
	}
	
	default void detach(Observer observer) {
		observers.remove(observer);
	}
	
	default void notifyObservers(AModel model) {
		for(Observer observer : observers) {
			observer.update(model);
		}
	}
}
