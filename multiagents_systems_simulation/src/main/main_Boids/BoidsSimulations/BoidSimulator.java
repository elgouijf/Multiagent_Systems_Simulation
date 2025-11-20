package main.main_Boids.BoidsSimulations;
import main.main_Boids.Boidutils.*;
import main.main_Boids.Boids.*;
import java.awt.Color;
import gui.Simulable;
import gui.GUISimulator;
import gui.Oval;



public class BoidSimulator implements Simulable {
    private GUISimulator guis;
    private Boid boid;
    private Color color;
    private Color compassColor;
    private int width;
    private int height;
    private Vector2D target;
    //private double target_radius;
    int radius = 7;


    // Constructor
    public BoidSimulator(GUISimulator guis, Boid boid, Color color, Color compassColor, Vector2D target){ 
        this.guis = guis;
        this.boid = boid;
        this.width = guis.getWidth();
        this.height = guis.getHeight();
        this.color = color;
        this.compassColor = compassColor;
        this.target = target;
        //this.target_radius = target_radius;
        this.reDisplay();
    }
    
    
    @Override
    public void next() {
        // Update dimensions in case of window resize
        this.width = guis.getWidth();
        this.height = guis.getHeight();

        Vector2D wanderForce = this.boid.wander();
        Vector2D steer = this.boid.seek(target);
        this.boid.applyForce(wanderForce);
        this.boid.applyForce(steer);
        this.boid.updatestate();
        // Bounce horizontally
        if (boid.getPosition().getX() < 0 || boid.getPosition().getX() + 2*radius > width) {
            Vector2D v = new Vector2D(-2*boid.getVelocity().getX(),0);
            boid.getVelocity().add(v);
            Vector2D pos = boid.getPosition();
            double x = Math.max(0, Math.min(pos.getX(), width - 2*radius)) - pos.getX();
            Vector2D X = new Vector2D(x,0);
            pos.add(X);  // Faical must write the comments 
        }
        // Bounce vertically
        if (boid.getPosition().getY() < 0 || boid.getPosition().getY() + 2*radius > height) {
            Vector2D v = new Vector2D(0,-2*boid.getVelocity().getY());
            boid.getVelocity().add(v);
            Vector2D pos = boid.getPosition();
            double y = Math.max(0, Math.min(pos.getY(), height - 2*radius)) - pos.getY();
            Vector2D Y = new Vector2D(0,y);
            pos.add(Y);  // Faical must write the comments 
        }
        this.reDisplay();
    }

    @Override
    public void restart() {
        boid.reInit();
        this.reDisplay();
    }

    private void reDisplay() {
        guis.reset();
        double x = boid.getPosition().getX();
        double y = boid.getPosition().getY();
        double vx = boid.getVelocity().getX();
        double vy = boid.getVelocity().getY();
        int boidRadius = boid.getSize();
        double xOff = x + vx;
        double yOff = y + vy;
        guis.addGraphicalElement(new gui.Oval((int)x, (int)y, color, color, boidRadius));
        guis.addGraphicalElement(new gui.Oval((int)xOff, (int)yOff, compassColor, compassColor, (int)(boidRadius/3)));
        Oval oval = new Oval((int) target.getX(),(int) target.getY(),Color.GREEN,Color.GREEN,4,4);
        guis.addGraphicalElement(oval);
    }
}

