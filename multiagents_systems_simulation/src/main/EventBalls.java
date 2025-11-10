package main;


public class EventBalls extends Event{
    Balls balls;
    BallsSimulator simulator;
    private EventManager manager;
    public EventBalls(long date, Balls balls, BallsSimulator ballsS, EventManager manager){
        super(date);
        this.balls = balls;
        this.simulator = ballsS;
        this.manager = manager;
    }
    @Override
    public void execute(){
        simulator.moveBalls();
        simulator.reDisplay();
        manager.addEvent(new EventBalls(getDate()+1, balls, simulator,manager));
    }
    
}
