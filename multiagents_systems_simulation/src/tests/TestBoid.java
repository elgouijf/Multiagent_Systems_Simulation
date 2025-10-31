package tests;
import main.*;

import java.awt.Color;
import gui.GUISimulator;

public class TestBoid {
    public static void main(String[] args) {
        int width  = (args.length > 0) ? Integer.parseInt(args[0]) : 1000;
        int height = (args.length > 1) ? Integer.parseInt(args[1]) : 1000;

        GUISimulator gui = new GUISimulator(width, height, Color.BLACK);

        // Position initiale
        Vector_2D position = new Vector_2D(width / 2.0, height / 2.0);

        // Vitesse initiale = 0
        Vector_2D velocity = new Vector_2D(0, 0);

        // Accélération initiale = 0
        Vector_2D acceleration = new Vector_2D(0, 0);

        // Limites et masse
        double speedLimit = 30.0;
        double forceLimit = 2.0;
        double wander_radius = 2;
        //double target_radius = 10;
        double path_radius = 10;
        int boid_radius = 9;

        Boid boid = new Boid(position, velocity, acceleration, speedLimit, forceLimit,wander_radius,path_radius, boid_radius, Color.YELLOW, Color.BLACK);

        // Direction vers laquelle il veut aller (target)
        Vector_2D target = new Vector_2D(45, 45);

        // Création du simulateur pour un seul boid
        BoidSimulator simulator = new BoidSimulator(gui, boid, boid.getColor(), boid.getCompassColor(), target);
        

        gui.setSimulable(simulator);
    }
}
