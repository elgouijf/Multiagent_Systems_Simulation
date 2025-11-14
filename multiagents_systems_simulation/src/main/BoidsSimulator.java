package main;
import main.Behaviors.*;
import gui.Simulable;
import gui.GUISimulator;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import gui.Oval;
public class BoidsSimulator implements Simulable {

    private GUISimulator gui;
    private Boids boids;
    private int width;
    private int height;
    private Vector_2D target;
    EventManager manager;


    public BoidsSimulator(GUISimulator gui, Boids boids,Vector_2D target) {
        this.gui = gui;
        this.boids = boids;

        this.width = gui.getWidth();
        this.height = gui.getHeight();
        this.target = target;
        this.manager = new EventManager();
        this.manager.addEvent(new EventBoids(0, this.boids, this, this.manager));
        this.reDisplay();
    }

    @Override
    public void next(){
        manager.next();
    }
    public void moveBoids() {
        this.width = gui.getWidth();
        this.height = gui.getHeight();
        ArrayList<Boid> listeBoids = boids.getlisteBoids();
        HashMap<GridType,Grid> grids = boids.getGrids();


        long start = System.nanoTime();
        for (Boid b : listeBoids) {
            
            /* b.wander(target,1); */
            b.submittoGroupBehavior(grids);

        }

        // Update all boids
        for (Boid b : listeBoids) {
            b.updatestate();
            // Update grid (possible now that b is updated)
            for (Behavior behavior : b.getBehaviors()) {
                Grid grid = grids.get(behavior.getGridType());
                 if (grid != null) {
                    behavior.updateGrid(b, grid);}
            }
            handleBorderBounce(b);
        }
        long end = System.nanoTime();
        double time_per_frame = (end - start) / 1e6; // milliseconds
        System.out.println("Frame time: " + time_per_frame + " ms");
        this.reDisplay();
    }

    @Override
    public void restart() {
        // Reinit each boid
        for (Boid b : boids.getlisteBoids()) {
            b.reInit();
        }
        manager.restart();
        manager.addEvent(new EventBoids(0, this.boids, this, this.manager));
        this.reDisplay();
    }

    /** Bounce on window borders */
    private void handleBorderBounce(Boid boid) {
        int r = boid.getSize();
        Vector_2D pos = boid.getPosition();
        Vector_2D vel = boid.getVelocity();

        // Bord gauche / droite
        if (pos.getX() < 0) {
            pos.add(new Vector_2D(-pos.getX(), 0)); // recaler à X=0
            vel.setX(Math.abs(vel.getX())); // rebond vers la droite
        } else if (pos.getX() + 2*r > width) {
            pos.add(new Vector_2D(width - 2*r - pos.getX(), 0)); // recaler au bord droit
            vel.setX(-Math.abs(vel.getX())); // rebond vers la gauche
        }

        // Bord haut / bas
        if (pos.getY() < 0) {
            pos.add(new Vector_2D(0, -pos.getY())); // recaler à Y=0
            vel.setY(Math.abs(vel.getY())); // rebond vers le bas
        } else if (pos.getY() + 2*r > height) {
            pos.add(new Vector_2D(0, height - 2*r - pos.getY())); // recaler au bord bas
            vel.setY(-Math.abs(vel.getY())); // rebond vers le haut
        }
    }


    
    public void reDisplay() {
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
            
        }
        // Draw target
        Oval target_oval = new Oval((int) target.getX(),(int) target.getY(),Color.GREEN,Color.GREEN,4,4);
        gui.addGraphicalElement(target_oval);
    }
}
