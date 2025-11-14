package main;
import main.Boids.*;

public class EventBoids extends Event {
    Boids boids;
    BoidsSimulator simulator;
    private EventManager manager;
    public EventBoids(long date, Boids boids, BoidsSimulator bSimulator, EventManager manager){
        super(date);
        this.boids = boids;
        this.simulator = bSimulator;
        this.manager = manager;
    }
    @Override
    public void execute(){
        simulator.moveBoids();
        simulator.reDisplay();
        manager.addEvent(new EventBoids(getDate()+1, boids, simulator,manager));
    }
}
