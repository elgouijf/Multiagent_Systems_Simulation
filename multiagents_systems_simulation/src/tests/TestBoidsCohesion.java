package tests;
import main.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import gui.GUISimulator;

public class TestBoidsCohesion {
    public static void main(String[] args) {
        int width  = (args.length > 0) ? Integer.parseInt(args[0]) : 1000;
        int height = (args.length > 1) ? Integer.parseInt(args[1]) : 1000;

        GUISimulator gui = new GUISimulator(width, height, Color.BLACK);
        Random rand = new Random();

        ArrayList<Boid> list = new ArrayList<>();

        // Color par cluster of boids
        Color[] clusterColors = { Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE };

        // number of boids par Cluster
        int cluster_n = 70;

        // maximum distance between elements of a cluster
        double clusterSize = 200; 

        

        // === Création de 4 clusters ===
        Vector_2D[] clusterCenters = {
            new Vector_2D(750, 750), // bottom right
            new Vector_2D(750, 250), // top right
            new Vector_2D(250, 250), // top left
            new Vector_2D(250, 750), // bottom left
        };

        for (int c = 0; c < 4; c++) {
            Vector_2D center = clusterCenters[c];
            Color bodyColor = clusterColors[c];

            for (int i = 0; i < cluster_n; i++) {
                double radius_x = rand.nextDouble() * clusterSize; // distance from center between 0 and clusterSize
                double radius_y = rand.nextDouble() * clusterSize; // distance from center between 0 and clusterSize
                Vector_2D pos = new Vector_2D(
                    center.getX() + radius_x,
                    center.getY() + radius_y
                );

                Vector_2D vel = new Vector_2D(rand.nextDouble()*4 - 2, rand.nextDouble()*4 - 2); // small random velocity

                Vector_2D acc = new Vector_2D(0, 0);

                double speedLimit = 8;
                double forceLimit = 3;
                double wanderRadius = 2;
                double pathRadius = 10;
                int boidRadius = 6;

                Boid b = new Boid(pos, vel, acc, speedLimit, forceLimit, wanderRadius, pathRadius, boidRadius, bodyColor, Color.WHITE, Math.PI / 12);
                list.add(b);
            }
        }

        Boids boids = new Boids(list);
        Vector_2D target = new Vector_2D(width / 2.0, height / 2.0);

        // Création du simulateur
        BoidsSimulator simulator = new BoidsSimulator(gui, boids, target);
        gui.setSimulable(simulator);
    }
}
