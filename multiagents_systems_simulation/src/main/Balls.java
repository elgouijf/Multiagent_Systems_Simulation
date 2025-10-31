package main;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Balls{
    /* we're using a list instead of a table for dynamic behavior + it being an abstract class will
    allow us to bounce between objects like ArrayLists and LinkedLists */
    private List<Point> balls;
    // We need to keep track of the initial positions of the balls for reseting
    private List<Point> balls_0;
    // We may also want to keep track of velocities for more complex simulations
    private List<Point> velocities_0;
    private List<Point> velocities;
    // We also need to keep track of the number of balls
    private int n_balls;

    
    

    // Constructor
    public Balls(List<Point> new_balls, List<Point> new_velocities){
        /* 
        This function is the constructor of the Balls class
        */

        this.balls = new ArrayList<>();
        this.balls_0 = new ArrayList<>();
        this.velocities = new ArrayList<>();
        this.velocities_0 = new ArrayList<>();
        this.n_balls = 0;
        this.addBallsInit(new_balls, new_velocities);
    }
    
    public void translate(int dx, int dy){
        /* 
        This function translates all the balls of a Balls instance by (dx, dy)
        */

        for (Point p : this.balls){
            p.translate(dx, dy);
        }
    }

    public void addBallsInit(List<Point> new_balls, List<Point> new_velocities){
        /* 
        This function extends the list of balls of a Balls instance
        by adding new_balls to it
        */

        int n = new_balls.size();
        this.balls.addAll(new_balls);
        this.velocities.addAll(new_velocities);
        for (Point p : new_balls) {
            // We clone the point so balls (velocities) and balls_0 (velocities_0) don't share references
            this.balls_0.add(new Point(p));
            this.velocities_0.add(new Point(velocities.get(this.n_balls)));
        }
        this.n_balls += n;
    }
    

    public void addBalls(List<Point> new_balls, List<Point> new_velocities){
        /* 
        This function extends the list of balls of a Balls instance
        by adding new_balls to it
        */

        int n = new_balls.size();
        this.balls.addAll(new_balls);
        this.velocities.addAll(new_velocities);
        this.n_balls += n;
    }

    public void reInit(){
        /* 
        This function re-initializes the positions of all the balls
        to their initial positions
        */
        for (int i = 0; i < this.n_balls; i++){
            // We clone the point so balls and balls_0 don't share references
            balls.set(i, balls_0.get(i));
        }
    }

    
    public List<Point> getBalls(){
        // since balls is private we need a getter, so we only provide read access
        return this.balls;
    }

    public List<Point> getVelocities(){
        // since balls is private we need a getter, so we only provide read access
        return this.velocities;
    }


    @Override
    public String toString(){
        /* 
        This function returns a string representation of the Balls instance        
        */

        // we use StringBuilder as it is more efficient tha n simple String concatenation  as it is a mutable class
        StringBuilder points = new StringBuilder();
        for (Point p : this.balls) {
            points.append(p.toString()).append(" ");
        }
        return points.toString();
    }
}