package main;

import java.awt.Color;
import gui.Simulable;
import gui.GUISimulator;
import gui.Oval;

public class BoidsSimulator implements Simulable {

    private GUISimulator gui;
    private Boids boids;
    private int width;
    private int height;

    public BoidsSimulator(GUISimulator gui, Boids boids) {
        this.gui = gui;
        this.boids = boids;

        this.width = gui.getWidth();
        this.height = gui.getHeight();

        this.reDisplay();
    }

    @Override
    public void next() {
        this.width = gui.getWidth();
        this.height = gui.getHeight();

        // Apply separation force for each boid
        for (Boid b : boids.getlisteBoids()) {
            boids.separation(b);
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
            boid.getVelocity().setX(-boid.getVelocity().getX());
            boid.getPosition().setX(
                Math.max(0, Math.min(x, width - 2*r))
            );
        }

        if (y < 0 || y + 2*r > height) {
            boid.getVelocity().setY(-boid.getVelocity().getY());
            boid.getPosition().setY(
                Math.max(0, Math.min(y, height - 2*r))
            );
        }
    }

    
    private void reDisplay() {
        gui.reset();

        for (Boid b : boids.getlisteBoids()) {
            double x = b.getPosition().getX();
            double y = b.getPosition().getY();
            double vx = b.getVelocity().getX();
            double vy = b.getVelocity().getY();
            int r = b.getSize();

            // Draw boid 
            gui.addGraphicalElement(
                new Oval((int)x, (int)y, b.getColor(), b.getColor(), r)
            );

            // Draw a small "compass" indicating direction
            gui.addGraphicalElement(
                new Oval((int)(x + vx), (int)(y + vy), b.getCompassColor(), b.getCompassColor(), Math.max(2, r/3))
            );
        }
    }
}
