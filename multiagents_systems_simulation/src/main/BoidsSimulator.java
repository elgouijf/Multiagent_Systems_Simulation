package main;
import gui.Simulable;
import gui.GUISimulator;
import java.awt.Color;
import java.util.ArrayList;

import gui.Oval;
public class BoidsSimulator implements Simulable {

    private GUISimulator gui;
    private Boids boids;
    private int width;
    private int height;
    private Vector_2D target;

    public BoidsSimulator(GUISimulator gui, Boids boids,Vector_2D target) {
        this.gui = gui;
        this.boids = boids;

        this.width = gui.getWidth();
        this.height = gui.getHeight();
        this.target = target;
        
        this.reDisplay();
    }

    @Override
    public void next() {
        this.width = gui.getWidth();
        this.height = gui.getHeight();
        ArrayList<Boid> listeBoids = boids.getlisteBoids();
        // Apply separation force for each boid
        for (Boid b : listeBoids) {
            
            /* b.wander(target,1); */
            b.submittoGroupBehavior(this.boids);
        }

        // Update all boids
        for (Boid b : boids.getlisteBoids()) {
            b.updatestate();
            handleBorderBounce(b);
        }

        this.reDisplay();
    }

    @Override
    public void restart() {
        // Reinit each boid
        for (Boid b : boids.getlisteBoids()) {
            b.reInit();
        }
        this.reDisplay();
    }

    /** Bounce on window borders */
    private void handleBorderBounce(Boid boid) {
        int r = boid.getSize();
        double x = boid.getPosition().getX();
        double y = boid.getPosition().getY();

        if (x < 0 || x + 2*r > width) {
            Vector_2D v = new Vector_2D(-2*boid.getVelocity().getX(),0);
            boid.getVelocity().add(v);
            Vector_2D X = new Vector_2D(Math.max(0, Math.min(x, width - 2*r))-x,0);
            boid.getPosition().add(X); // Faicil must write the comments 
        }

        if (y < 0 || y + 2*r > height) {
            Vector_2D v = new Vector_2D(0,-2*boid.getVelocity().getY());
            boid.getVelocity().add(v);
            Vector_2D Y = new Vector_2D(0,Math.max(0, Math.min(y, height - 2*r)));
            boid.getPosition().add(Y);
        }
    }

    
    private void reDisplay() {
        gui.reset();

        for (Boid b : boids.getlisteBoids()) {
            double x = b.getPosition().getX();
            double y = b.getPosition().getY();
            /* double vx = b.getVelocity().getX();
            double vy = b.getVelocity().getY(); */
            int size = b.getSize();

            // Draw boid 

            // Get the velocity direction angle
            double orientation = b.getVelocity().heading();
            // Create a triangle shape for the boid relative to the center
            Vector_2D triangle_tip = new Vector_2D(2*size, 0);
            Vector_2D left_wing = new Vector_2D(-size, size);
            Vector_2D right_wing = new Vector_2D(-size, -size);

            // Rotate the triangle according to the orientation
            triangle_tip.rotate(orientation);
            left_wing.rotate(orientation);
            right_wing.rotate(orientation);
 
            // Translate the triangle to the boid's position
            triangle_tip.add(new Vector_2D(x , y));
            left_wing.add(new Vector_2D(x , y));
            right_wing.add(new Vector_2D(x , y));

            // Draw the triangle
            int[] triangle_x = { (int)Math.round(triangle_tip.getX()), 
             (int)Math.round(left_wing.getX()), 
             (int)Math.round(right_wing.getX()) };

            int[] triangle_y = { (int)Math.round(triangle_tip.getY()), 
             (int)Math.round(left_wing.getY()), 
             (int)Math.round(right_wing.getY()) };
            PolygonGraphics triangle_boid = new PolygonGraphics(triangle_x, triangle_y, 3, b.getColor());

           // Add to GUI
            gui.addGraphicalElement(triangle_boid); 
            Oval oval = new Oval((int) target.getX(),(int) target.getY(),Color.GREEN,Color.GREEN,4,4);
            gui.addGraphicalElement(oval);
        }
    }
}
