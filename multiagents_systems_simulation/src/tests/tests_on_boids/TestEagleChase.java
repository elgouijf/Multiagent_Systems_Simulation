package tests.tests_on_boids;

import main.main_Boids.Boids.*;
import main.main_Boids.Boids.Species.*;
import main.main_Boids.BoidsSimulations.MultipleBoidsSimulator;
import main.main_Boids.Boidutils.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import gui.GUISimulator;

public class TestEagleChase {
    public static void main(String[] args) {
        int width  = (args.length > 0) ? Integer.parseInt(args[0]) : 1000;
        int height = (args.length > 1) ? Integer.parseInt(args[1]) : 1000;

        GUISimulator gui = new GUISimulator(width, height, Color.BLACK);
        Random rand = new Random();

        // === Birds ===
        ArrayList<Boid> birdsList = new ArrayList<>();
        Color[] clusterColors = { Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE };
        Vector2D[] clusterCenters = {
            new Vector2D(750, 750), new Vector2D(750, 250),
            new Vector2D(250, 250), new Vector2D(250, 750)
        };

        int clusterN = 50;
        double clusterSize = 200;
        double birdSpeedLimit = 8, birdForceLimit = 3, birdWanderRadius = 2, birdPathRadius = 10;
        int birdRadius = 6;
        double birdAngleWander = Math.PI / 12;

        for (int c = 0; c < 4; c++) {
            // For each cluster
            Vector2D center = clusterCenters[c];
            Color bodyColor = clusterColors[c];
            for (int i = 0; i < clusterN; i++) {
                double radiusX = rand.nextDouble() * clusterSize;
                double radiusY = rand.nextDouble() * clusterSize;
                Vector2D pos = new Vector2D(center.getX() + radiusX, center.getY() + radiusY);
                Vector2D vel = new Vector2D(rand.nextDouble()*4 - 2, rand.nextDouble()*4 - 2);
                Vector2D acc = new Vector2D(0, 0);
                birdsList.add(new Bird(pos, vel, acc, birdSpeedLimit, birdForceLimit, birdWanderRadius,
                        birdPathRadius, birdRadius, bodyColor, Color.WHITE, birdAngleWander, width, height));
            }
        }

        // === Eagles ===
        ArrayList<Boid> eaglesList = new ArrayList<>();
        int eagleN = 3;
        double eagleSpeedLimit = 12;
        double eagleForceLimit = 5;
        double eagleWanderRadius = 4;
        double eaglePathRadius = 15;
        int eagleRadius = 12;
        double eagleAngleWander = Math.PI / 8;

        for (int i = 0; i < eagleN; i++) {
            Vector2D pos = new Vector2D(rand.nextDouble()*width, rand.nextDouble()*height);
            Vector2D vel = new Vector2D(rand.nextDouble()*6 - 3, rand.nextDouble()*6 - 3);
            Vector2D acc = new Vector2D(0, 0);
            eaglesList.add(new Eagle(pos, vel, acc, eagleSpeedLimit, eagleForceLimit, eagleWanderRadius,
                    eaglePathRadius, eagleRadius, Color.RED, Color.WHITE, eagleAngleWander, width, height));
        }

        // === Grids ===
        HashMap<GridType, Grid> birdGrids = new HashMap<>();
        birdGrids.put(GridType.SEPARATION, new Grid(width, height, birdsList.get(0).getCloseDistance(), GridType.SEPARATION));
        birdGrids.put(GridType.TOGETHER, new Grid(width, height, birdsList.get(0).getNeighborDistance(), GridType.TOGETHER));

        HashMap<GridType, Grid> eagleGrids = new HashMap<>();
        eagleGrids.put(GridType.SEPARATION, new Grid(width, height, eaglesList.get(0).getCloseDistance(), GridType.SEPARATION));
        eagleGrids.put(GridType.TOGETHER, new Grid(width, height, eaglesList.get(0).getNeighborDistance(), GridType.TOGETHER));
        eagleGrids.put(GridType.PREDATOR_DETECTION, new Grid(width, height, eaglesList.get(0).getpathRadius(), GridType.PREDATOR_DETECTION));

        // Adding Boids to their grids for efficient neighbor search
        for (Boid b : birdsList) {
            // Add bird to its grids
            birdGrids.get(GridType.SEPARATION).addBoid(b);
            birdGrids.get(GridType.TOGETHER).addBoid(b);
            // Add bird to eagle detection grid to be detected by eagles
            eagleGrids.get(GridType.PREDATOR_DETECTION).addBoid(b);
        }
        for (Boid e : eaglesList) {
            // Add eagle to its grids
            eagleGrids.get(GridType.SEPARATION).addBoid(e);
            eagleGrids.get(GridType.TOGETHER).addBoid(e);
            // Add eagle to brid neighbor grid to be detected by birds
            birdGrids.get(GridType.TOGETHER).addBoid(e);
        }

        // Boids containers
        Boids birds = new Boids(birdsList, birdGrids);
        Boids eagles = new Boids(eaglesList, eagleGrids);


        // Création d'une liste de conteneurs de Boids
        ArrayList<Boids> boidsContainers = new ArrayList<>();
        boidsContainers.add(birds);
        boidsContainers.add(eagles);
        // === Simulator multiple ===
        MultipleBoidsSimulator simulator = new MultipleBoidsSimulator(gui, boidsContainers);
        

        gui.setSimulable(simulator);
    }
}
