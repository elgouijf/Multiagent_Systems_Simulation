package main.main_Boids.BoidsSimulations;

import main.main_Boids.Boidutils.*;
import main.main_Boids.Boids.Boid;
import gui.GUISimulator;
import gui.Simulable;

import java.awt.Color;

public class BoidSimulatorWithWind implements Simulable {

    private GUISimulator gui;
    private Boid boid;
    private Vector_2D target;
    private int resolution = 30;        // taille d'une cellule du flowfield
    private double strength = 1;      // force du vent
    private double scale = 0.1;         // "zoom" du Perlin
    private FlowField windField;
    private boolean showWind = true; // whether to display the wind field 

    public BoidSimulatorWithWind(GUISimulator gui, Boid boid, Vector_2D target) {
        this.gui = gui;
        this.boid = boid;
        this.target = target;
    }

    @Override
    public void next() {
        int width = gui.getWidth();
        int height = gui.getHeight();

        // Générer un FlowField dynamique
        double time = System.currentTimeMillis() * 0.0001;
        windField = FlowFieldGenerator.generateNoiseField(resolution, width, height, time, strength, scale);

        // Récupérer la force du vent à la position du boid
        Vector_2D wind = windField.getVector(boid.getPosition());
        boid.applyForce(wind);

        // Autres comportements
        boid.applyForce(boid.wander());
        boid.applyForce(boid.seek(target));

        // Mettre à jour l'état
        boid.updatestate();

        // Rebonds sur les bords
        handleBorderBounce();

        // Affichage
        reDisplay();
    }

    public void setShowWind(boolean show) {
    this.showWind = show;
}

    @Override
    public void restart() {
        boid.reInit();
        reDisplay();
    }

    private void handleBorderBounce() {
        int width = gui.getWidth();
        int height = gui.getHeight();
        int r = boid.getSize();
        Vector_2D pos = boid.getPosition();
        Vector_2D vel = boid.getVelocity();

        if (pos.getX() < 0) { pos.setX(0); vel.setX(Math.abs(vel.getX())); }
        if (pos.getX() + 2*r > width) { pos.setX(width - 2*r); vel.setX(-Math.abs(vel.getX())); }
        if (pos.getY() < 0) { pos.setY(0); vel.setY(Math.abs(vel.getY())); }
        if (pos.getY() + 2*r > height) { pos.setY(height - 2*r); vel.setY(-Math.abs(vel.getY())); }
    }

    private void reDisplay() {
        gui.reset();

        // Afficher le FlowField; Comment it if you don't want to see it
        // Display the FlowField only if the user wants it
        if (showWind && windField != null) {
            FlowFieldDraw.draw(gui, windField);
    }


        // Afficher le boid
        Vector_2D pos = boid.getPosition();
        gui.addGraphicalElement(new gui.Oval((int)pos.getX(), (int)pos.getY(), boid.getColor(), boid.getColor(), boid.getSize()));

        // Afficher le target
        gui.addGraphicalElement(new gui.Oval((int)target.getX(), (int)target.getY(), Color.GREEN, Color.GREEN, 4, 4));
    }
}
