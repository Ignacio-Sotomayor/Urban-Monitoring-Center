package com.view;

import com.model.Fines.Fine;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Observer;
import java.util.concurrent.CopyOnWriteArrayList;

public class RegistryNotifier {

    private final List<Observer> observers = new CopyOnWriteArrayList<>();

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void notifyObservers(Fine newFine) {
        for (Observer o : observers) {
            o.update(null,null);
        }
    }
}