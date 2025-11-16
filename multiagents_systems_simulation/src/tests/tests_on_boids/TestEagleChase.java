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
        Vector_2D[] clusterCenters = {
            new Vector_2D(750, 750), new Vector_2D(750, 250),
            new Vector_2D(250, 250), new Vector_2D(250, 750)
        };

        int cluster_n = 50;
        double clusterSize = 200;
        double birdSpeedLimit = 8, birdForceLimit = 3, birdWanderRadius = 2, birdPathRadius = 10;
        int birdRadius = 6;
        double birdAngleWander = Math.PI / 12;

        for (int c = 0; c < 4; c++) {
            Vector_2D center = clusterCenters[c];
            Color bodyColor = clusterColors[c];
            for (int i = 0; i < cluster_n; i++) {
                double radius_x = rand.nextDouble() * clusterSize;
                double radius_y = rand.nextDouble() * clusterSize;
                Vector_2D pos = new Vector_2D(center.getX() + radius_x, center.getY() + radius_y);
                Vector_2D vel = new Vector_2D(rand.nextDouble()*4 - 2, rand.nextDouble()*4 - 2);
                Vector_2D acc = new Vector_2D(0, 0);
                birdsList.add(new Bird(pos, vel, acc, birdSpeedLimit, birdForceLimit, birdWanderRadius,
                        birdPathRadius, birdRadius, bodyColor, Color.WHITE, birdAngleWander, width, height));
            }
        }

        // === Eagles ===
        ArrayList<Boid> eaglesList = new ArrayList<>();
        int eagle_n = 3;
        double eagleSpeedLimit = 12, eagleForceLimit = 4, eagleWanderRadius = 4, eaglePathRadius = 15;
        int eagleRadius = 12;
        double eagleAngleWander = Math.PI / 8;

        for (int i = 0; i < eagle_n; i++) {
            Vector_2D pos = new Vector_2D(rand.nextDouble()*width, rand.nextDouble()*height);
            Vector_2D vel = new Vector_2D(rand.nextDouble()*6 - 3, rand.nextDouble()*6 - 3);
            Vector_2D acc = new Vector_2D(0, 0);
            eaglesList.add(new Eagle(pos, vel, acc, eagleSpeedLimit, eagleForceLimit, eagleWanderRadius,
                    eaglePathRadius, eagleRadius, Color.RED, Color.WHITE, eagleAngleWander, width, height));
        }

        // === Grids ===
        HashMap<GridType, Grid> birdGrids = new HashMap<>();
        birdGrids.put(GridType.SEPARATION, new Grid(width, height, birdsList.get(0).getClose_distance(), GridType.SEPARATION));
        birdGrids.put(GridType.TOGETHER, new Grid(width, height, birdsList.get(0).getNeighbor_distance(), GridType.TOGETHER));

        HashMap<GridType, Grid> eagleGrids = new HashMap<>();
        eagleGrids.put(GridType.SEPARATION, new Grid(width, height, eaglesList.get(0).getClose_distance(), GridType.SEPARATION));
        eagleGrids.put(GridType.TOGETHER, new Grid(width, height, eaglesList.get(0).getNeighbor_distance(), GridType.TOGETHER));
        eagleGrids.put(GridType.PREDATOR_DETECTION, new Grid(width, height, eaglesList.get(0).getPath_radius(), GridType.PREDATOR_DETECTION));

        // Ajout aux grids
        for (Boid b : birdsList) {
            birdGrids.get(GridType.SEPARATION).addBoid(b);
            birdGrids.get(GridType.TOGETHER).addBoid(b);
        }
        for (Boid e : eaglesList) {
            eagleGrids.get(GridType.SEPARATION).addBoid(e);
            eagleGrids.get(GridType.TOGETHER).addBoid(e);
            eagleGrids.get(GridType.PREDATOR_DETECTION).addBoid(e);
        }

        // === Boids containers ===
        Boids birds = new Boids(birdsList, birdGrids);
        Boids eagles = new Boids(eaglesList, eagleGrids);

        // === Target ===
        Vector_2D target = new Vector_2D(width / 2.0, height / 2.0);

        // Création d'une liste de conteneurs de Boids
        ArrayList<Boids> boidsContainers = new ArrayList<>();
        boidsContainers.add(birds);
        boidsContainers.add(eagles);
        // === Simulator multiple ===
        MultipleBoidsSimulator simulator = new MultipleBoidsSimulator(gui, boidsContainers, target);
        

        gui.setSimulable(simulator);
    }
}
