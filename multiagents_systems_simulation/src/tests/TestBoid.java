package tests;
import main.*;

import java.awt.Color;
import gui.GUISimulator;

public class TestBoid {
    public static void main(String[] args) {
        int width  = (args.length > 0) ? Integer.parseInt(args[0]) : 500;
        int height = (args.length > 1) ? Integer.parseInt(args[1]) : 500;

        GUISimulator gui = new GUISimulator(width, height, Color.BLACK);

        // Position initiale
        Vector_2D position = new Vector_2D(width / 2.0, height / 2.0);

        // Vitesse initiale = 0
        Vector_2D velocity = new Vector_2D(0, 0);

        // Accélération initiale = 0
        Vector_2D acceleration = new Vector_2D(0, 0);

        // Limites et masse
        double speedLimit = 5.0;
        double forceLimit = 0.2;
        double mass = 1.0;

        Boid boid = new Boid(position, velocity, acceleration, speedLimit, forceLimit, mass);

        // Direction vers laquelle il veut aller (target)
        Vector_2D target = new Vector_2D(width - 50, height - 50);

        // Création du simulateur pour un seul boid
        BoidSimulator simulator = new BoidSimulator(gui, boid, Color.RED, target);

        gui.setSimulable(simulator);
    }
}
