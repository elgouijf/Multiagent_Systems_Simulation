package tests.tests_on_boids;
import main.main_Boids.Boids.*;
import main.main_Boids.Boids.Species.*;
import main.main_Boids.BoidsSimulations.*;
import main.main_Boids.Boidutils.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import gui.GUISimulator;
public class TestBirdsCohesionWithWind {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask the user if they want to display the wind
        System.out.print("Display wind? (true/false): ");
        boolean showWind = scanner.nextBoolean();

        int width  = (args.length > 0) ? Integer.parseInt(args[0]) : 1000;
        int height = (args.length > 1) ? Integer.parseInt(args[1]) : 1000;

        GUISimulator gui = new GUISimulator(width, height, Color.BLACK);
        Random rand = new Random();

        ArrayList<Boid> list = new ArrayList<>();

        // Colors for clusters of birds
        Color[] clusterColors = { Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE };
        int clusterN = 50; // number of birds per cluster
        double clusterSize = 200; // max distance from cluster center

        Vector2D[] clusterCenters = {
            new Vector2D(750, 750), // bottom right
            new Vector2D(750, 250), // top right
            new Vector2D(250, 250), // top left
            new Vector2D(250, 750)  // bottom left
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
            Vector2D center = clusterCenters[c];
            Color bodyColor = clusterColors[c];

            for (int i = 0; i < clusterN; i++) {
                double radiusX = rand.nextDouble() * clusterSize;
                double radiusY = rand.nextDouble() * clusterSize;
                Vector2D pos = new Vector2D(center.getX() + radiusX, center.getY() + radiusY);

                Vector2D vel = new Vector2D(rand.nextDouble()*4 - 2, rand.nextDouble()*4 - 2);
                Vector2D acc = new Vector2D(0, 0);

                Boid b = new Bird(pos, vel, acc, speedLimit, forceLimit, wanderRadius, pathRadius, birdRadius, bodyColor, Color.WHITE, angleWander, width, height);
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
        Boids birds = new Boids(list, grids); // grids are no longer needed

        for (Boid b : list) {
            grids.get(GridType.SEPARATION).addBoid(b);
            grids.get(GridType.TOGETHER).addBoid(b);
            grids.get(GridType.TOGETHER).addBoid(b);
        }

       
      

        // Create the simulator
        BoidsSimulatorWithWind simulator = new BoidsSimulatorWithWind(gui, birds);

        simulator.setShowWind(showWind); // apply user choice

        gui.setSimulable(simulator);
    }
}
    
