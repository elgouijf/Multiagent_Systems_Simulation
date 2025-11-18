package tests.tests_on_boids;

import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.Vector_2D;
import main.main_Boids.BoidsSimulations.BoidSimulatorWithWind;
import gui.GUISimulator;

import java.awt.Color;
import java.util.Scanner;

public class TestBoidWithWind {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask the user if they want to display the wind
        System.out.print("Display wind? (true/false): ");
        boolean showWind = scanner.nextBoolean();

        // Create GUI
        GUISimulator gui = new GUISimulator(1000, 1000, Color.BLACK);

        // Create boid
        Vector_2D position = new Vector_2D(500, 500);
        Vector_2D velocity = new Vector_2D(0, 0);
        Vector_2D acceleration = new Vector_2D(0, 0);

        Boid boid = new Boid(position, velocity, acceleration, 30.0, 2.0, 2.0, 10.0, 9, Color.YELLOW, Color.BLACK, 0);

        // Target
        Vector_2D target = new Vector_2D(450, 450);

        // Create simulator
        BoidSimulatorWithWind simulator = new BoidSimulatorWithWind(gui, boid, target);
        simulator.setShowWind(showWind); // apply user choice

        gui.setSimulable(simulator);
    }
}
