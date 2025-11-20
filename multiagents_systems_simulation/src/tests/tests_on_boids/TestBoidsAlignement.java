package tests.tests_on_boids;
import main.main_Boids.Boids.*;
import main.main_Boids.BoidsSimulations.*;
import main.main_Boids.Boidutils.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import gui.GUISimulator;

public class TestBoidsAlignement {
    public static void main(String[] args) {
        int width  = (args.length > 0) ? Integer.parseInt(args[0]) : 1000;
        int height = (args.length > 1) ? Integer.parseInt(args[1]) : 1000;

        GUISimulator gui = new GUISimulator(width, height, Color.BLACK);
        Random rand = new Random();

        ArrayList<Boid> list = new ArrayList<>();

        // Color par cluster of boids
        Color[] clusterColors = { Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE };

        // number of boids par Cluster
        int clusterN = 70;

        // maximum distance between elements of a cluster
        double clusterSize = 40; 

        double speedLimit = 8;
        double forceLimit = 3;
        double wanderRadius = 2;
        double pathRadius = 10;
        int boidRadius = 6;

        // Creating 4 clusters
        Vector2D[] clusterCenters = {
            new Vector2D(750, 750), // bottom right
            new Vector2D(750, 250), // top right
            new Vector2D(250, 250), // top left
            new Vector2D(250, 750), // bottom left
        };

        for (int c = 0; c < 4; c++) {
            Vector2D center = clusterCenters[c];
            Color bodyColor = clusterColors[c];

            for (int i = 0; i < clusterN; i++) {
                double radiusX = rand.nextDouble() * clusterSize; // distance from center between 0 and clusterSize
                double radiusY = rand.nextDouble() * clusterSize; // distance from center between 0 and clusterSize
                Vector2D pos = new Vector2D(
                    center.getX() + radiusX,
                    center.getY() + radiusY
                );

                Vector2D vel = new Vector2D(rand.nextDouble()*4 - 2, rand.nextDouble()*4 - 2); // small random velocity

                Vector2D acc = new Vector2D(0, 0);

               

                Boid b = new Boid(pos, vel, acc, speedLimit, forceLimit, wanderRadius, pathRadius, boidRadius, bodyColor, Color.WHITE, Math.PI / 12);
                list.add(b);
            }
        }

        // Create grids for behaviors (one grid per interaction distance) 
        HashMap<GridType, Grid> grids = new HashMap<>();
        double separationDistance = list.get(0).getCloseDistance();
        double neighborDistance   = list.get(0).getNeighborDistance();
        Grid gridSeparation = new Grid(width, height, separationDistance, GridType.SEPARATION);
        Grid gridTogether   = new Grid(width, height, neighborDistance, GridType.TOGETHER);
        grids.put(GridType.SEPARATION, gridSeparation);
        grids.put(GridType.TOGETHER, gridTogether);
        // Create Boids container
        Boids boids = new Boids(list, grids); // grids are no longer needed

        for (Boid b : list) {
            grids.get(GridType.SEPARATION).addBoid(b);
            grids.get(GridType.TOGETHER).addBoid(b);
            grids.get(GridType.TOGETHER).addBoid(b);
        }


        // Création du simulateur
        BoidsSimulator simulator = new BoidsSimulator(gui, boids);
        gui.setSimulable(simulator);
    }
}
