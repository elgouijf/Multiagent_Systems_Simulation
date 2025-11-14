package tests;
import main.*;
import main.Boids.Boid;
import main.Boids.Boids;
import main.BoidsSimulations.*;
import main.Boidutils.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import gui.GUISimulator;

public class TestBoids {
    public static void main(String[] args) {
        int width  = (args.length > 0) ? Integer.parseInt(args[0]) : 1000;
        int height = (args.length > 1) ? Integer.parseInt(args[1]) : 1000;

        GUISimulator gui = new GUISimulator(width, height, Color.BLACK);
        Random rand = new Random();

        int n = 2000; // number of boids
        ArrayList<Boid> list = new ArrayList<>();

        // Colors to cycle through
        Color[] bodyColors = { Color.YELLOW, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE };
        Color[] compassColors = { Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY };

        // parameters for boids
        double speedLimit   = 6 + rand.nextDouble()*4;      // 6 to 10
        double forceLimit   = 2 + rand.nextDouble()*4;      // 2 to 6
        double wanderRadius = 1 + rand.nextDouble()*3;      // 1 to 4
        double pathRadius   = 5 + rand.nextDouble()*10;     // 5 to 15
        int boidRadius      = 3 + rand.nextInt(1)*6;          // 2 to 4


        for (int i = 0; i < n; i++) {
            Vector_2D pos = new Vector_2D(Math.random()*width, Math.random()*height);
            Vector_2D vel = new Vector_2D(Math.random()*4 - 2, Math.random()*4 - 2); // small random velocity 
            Vector_2D acc = new Vector_2D(0,0);

            

            Color color = bodyColors[i % bodyColors.length];
            Color compass_color = compassColors[i % compassColors.length];

            Boid b = new Boid(pos, vel, acc, speedLimit, forceLimit, wanderRadius, pathRadius, boidRadius, color, compass_color,Math.PI/12);
            /* b.setCloseDistance(100); */
            list.add(b);
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
        Boids boids = new Boids(list, grids); // grids are no longer needed

        for (Boid b : list) {
            grids.get(GridType.SEPARATION).addBoid(b);
            grids.get(GridType.TOGETHER).addBoid(b);
            grids.get(GridType.TOGETHER).addBoid(b);
        }
        
        Vector_2D target = new Vector_2D(50,50);
        // Simulator for multiple boids
        BoidsSimulator simulator = new BoidsSimulator(gui, boids,target);

        gui.setSimulable(simulator);
    }
}
