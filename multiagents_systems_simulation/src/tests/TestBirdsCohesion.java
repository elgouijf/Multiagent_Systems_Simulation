package tests;
import main.*;
import main.Boids.*;
import main.Boids.Species.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import gui.GUISimulator;

public class TestBirdsCohesion {
    public static void main(String[] args) {
        int width  = (args.length > 0) ? Integer.parseInt(args[0]) : 1000;
        int height = (args.length > 1) ? Integer.parseInt(args[1]) : 1000;

        GUISimulator gui = new GUISimulator(width, height, Color.BLACK);
        Random rand = new Random();

        ArrayList<Boid> list = new ArrayList<>();

        // Colors for clusters of birds
        Color[] clusterColors = { Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE };
        int cluster_n = 200; // number of birds per cluster
        double clusterSize = 200; // max distance from cluster center

        Vector_2D[] clusterCenters = {
            new Vector_2D(750, 750), // bottom right
            new Vector_2D(750, 250), // top right
            new Vector_2D(250, 250), // top left
            new Vector_2D(250, 750)  // bottom left
        };

        // Bird parameters
        double speedLimit = 8;
        double forceLimit = 3;
        double wanderRadius = 2;
        double pathRadius = 10;
        int birdRadius = 6;
        double angleWander = Math.PI / 12;

        // Create clusters of birds
        for (int c = 0; c < 4; c++) {
            Vector_2D center = clusterCenters[c];
            Color bodyColor = clusterColors[c];

            for (int i = 0; i < cluster_n; i++) {
                double radius_x = rand.nextDouble() * clusterSize;
                double radius_y = rand.nextDouble() * clusterSize;
                Vector_2D pos = new Vector_2D(center.getX() + radius_x, center.getY() + radius_y);

                Vector_2D vel = new Vector_2D(rand.nextDouble()*4 - 2, rand.nextDouble()*4 - 2);
                Vector_2D acc = new Vector_2D(0, 0);

                Boid b = new Bird(pos, vel, acc, speedLimit, forceLimit, wanderRadius, pathRadius, birdRadius, bodyColor, Color.WHITE, angleWander, width, height);
                list.add(b);
            }
        }

        // Create grids for behaviors (one grid per interaction distance) 
        HashMap<GridType, Grid> grids = new HashMap<>();
        double separation_distance = list.get(0).getClose_distance();
        double neighbor_distance   = list.get(0).getNeighbor_distance();
        Grid grid_separation = new Grid(width, height, separation_distance, GridType.SEPARATION);
        Grid grid_together   = new Grid(width, height, neighbor_distance, GridType.TOGETHER);
        grids.put(GridType.SEPARATION, grid_separation);
        grids.put(GridType.TOGETHER, grid_together);
        // Create Boids container
        Boids birds = new Boids(list, grids); // grids are no longer needed

        for (Boid b : list) {
            grids.get(GridType.SEPARATION).addBoid(b);
            grids.get(GridType.TOGETHER).addBoid(b);
            grids.get(GridType.TOGETHER).addBoid(b);
        }

        // Target (can be used for wandering, seeking, etc.)
        Vector_2D target = new Vector_2D(width / 2.0, height / 2.0);

        // Create the simulator
        BoidsSimulator simulator = new BoidsSimulator(gui, birds, target);
        gui.setSimulable(simulator);
    }
}