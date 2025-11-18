package main.EventManaging;

import main.main_Boids.Boids.*;
import main.main_Boids.BoidsSimulations.MultipleBoidsSimulator;

public class EventBoidsMultiple extends Event {
    private MultipleBoidsSimulator simulator;
    private EventManager manager;

    public EventBoidsMultiple(long date, MultipleBoidsSimulator simulator, EventManager manager) {
        super(date);
        this.simulator = simulator;
        this.manager = manager;
    }

    @Override
    public void execute() {
        simulator.moveBoids();
        simulator.reDisplay();
        manager.addEvent(new EventBoidsMultiple(getDate()+1, simulator, manager));
    }
}
