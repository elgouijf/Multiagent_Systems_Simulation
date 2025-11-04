package tests;
import main.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import gui.GUISimulator;

public class TestBoids {
    public static void main(String[] args) {
        int width  = (args.length > 0) ? Integer.parseInt(args[0]) : 1000;
        int height = (args.length > 1) ? Integer.parseInt(args[1]) : 1000;

        GUISimulator gui = new GUISimulator(width, height, Color.BLACK);
        Random rand = new Random();

        int n = 30; // number of boids
        ArrayList<Boid> list = new ArrayList<>();

        // Colors to cycle through
        Color[] bodyColors = { Color.YELLOW, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE };
        Color[] compassColors = { Color.BLACK, Color.RED, Color.BLUE, Color.WHITE, Color.GRAY };

        for (int i = 0; i < n; i++) {
            Vector_2D pos = new Vector_2D(Math.random()*width, Math.random()*height);
            Vector_2D vel = new Vector_2D(Math.random()*4 - 2, Math.random()*4 - 2); // small random velocity
            Vector_2D acc = new Vector_2D(0,0);

            // Randomized parameters for each boid
            double speedLimit   = 6 + rand.nextDouble()*4;      // 6 to 10
            double forceLimit   = 2 + rand.nextDouble()*4;      // 2 to 6
            double wanderRadius = 1 + rand.nextDouble()*3;      // 1 to 4
            double pathRadius   = 5 + rand.nextDouble()*10;     // 5 to 15
            int boidRadius      = 5 + rand.nextInt(6);          // 5 to 10

            Color color = bodyColors[i % bodyColors.length];
            Color compass_color = compassColors[i % compassColors.length];

            Boid b = new Boid(pos, vel, acc, speedLimit, forceLimit, wanderRadius, pathRadius, boidRadius, color, compass_color);
            list.add(b);
        }

        Boids boids = new Boids(list);
        
        Vector_2D target = new Vector_2D(50,50);
        // Simulator for multiple boids
        BoidsSimulator simulator = new BoidsSimulator(gui, boids,target);

        gui.setSimulable(simulator);
    }
}
