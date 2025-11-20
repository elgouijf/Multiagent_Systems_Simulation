package main.main_Boids.BoidsSimulations;
import main.main_Boids.Boidutils.*;
import main.EventManaging.EventManager;
import main.EventManaging.EventBoids;
import main.main_Boids.Behaviors.*;
import main.main_Boids.Boids.*;

import gui.Simulable;
import gui.GUISimulator;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import gui.Oval;
public class BoidsSimulator implements Simulable {
    protected FlowField windField;
    protected GUISimulator gui;
    protected Boids boids;
    protected int width;
    protected int height;
    private EventManager manager;

    public void setWindField(FlowField field) {
    this.windField = field;
    }

    public void removeWindField() {
        this.windField = null;
    }

    public FlowField getWindField() {
        // In case we want to add wind effects
        return windField;
    }
    public BoidsSimulator(GUISimulator gui, Boids boids) {
        this.gui = gui;
        this.boids = boids;

        this.width = gui.getWidth();
        this.height = gui.getHeight();
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

        for (Boid b : listeBoids) {
            
            /* b.wander(target,1); */
            b.submittoGroupBehavior(grids, windField);

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
        Vector2D pos = boid.getPosition();
        Vector2D vel = boid.getVelocity();

        // Bord gauche / droite
        if (pos.getX() < 0) {
            pos.add(new Vector2D(-pos.getX(), 0)); // recaler à X=0
            vel.setX(Math.abs(vel.getX())); // rebond vers la droite
        } else if (pos.getX() + 2*r > width) {
            pos.add(new Vector2D(width - 2*r - pos.getX(), 0)); // recaler au bord droit
            vel.setX(-Math.abs(vel.getX())); // rebond vers la gauche
        }

        // Bord haut / bas
        if (pos.getY() < 0) {
            pos.add(new Vector2D(0, -pos.getY())); // recaler à Y=0
            vel.setY(Math.abs(vel.getY())); // rebond vers le bas
        } else if (pos.getY() + 2*r > height) {
            pos.add(new Vector2D(0, height - 2*r - pos.getY())); // recaler au bord bas
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
            Vector2D triangleTip = new Vector2D(2*size, 0);
            Vector2D leftWing = new Vector2D(-size, size);
            Vector2D rightWing = new Vector2D(-size, -size);

            // Rotate the triangle according to the orientation
            triangleTip.rotate(orientation);
            leftWing.rotate(orientation);
            rightWing.rotate(orientation);
 
            // Translate the triangle to the boid's position
            triangleTip.add(new Vector2D(x , y));
            leftWing.add(new Vector2D(x , y));
            rightWing.add(new Vector2D(x , y));

            // Draw the triangle
            int[] triangleX = { (int)Math.round(triangleTip.getX()), 
             (int)Math.round(leftWing.getX()), 
             (int)Math.round(rightWing.getX()) };

            int[] triangleY = { (int)Math.round(triangleTip.getY()), 
             (int)Math.round(leftWing.getY()), 
             (int)Math.round(rightWing.getY()) };
            PolygonGraphics triangleBoid = new PolygonGraphics(triangleX, triangleY, 3, b.getColor());

           // Add to GUI
            gui.addGraphicalElement(triangleBoid); 
            
        }
    }

    public GUISimulator getGui() { 
        return this.gui; }
    public int getWidth()        { 
        return this.width; }
    public int getHeight()       { 
        return this.height; }

}
