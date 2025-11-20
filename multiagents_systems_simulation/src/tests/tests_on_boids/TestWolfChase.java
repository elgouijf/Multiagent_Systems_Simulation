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

public class TestWolfChase {
    public static void main(String[] args) {

        int width  = (args.length > 0) ? Integer.parseInt(args[0]) : 1200;
        int height = (args.length > 1) ? Integer.parseInt(args[1]) : 800;

        GUISimulator gui = new GUISimulator(width, height, Color.BLACK);
        Random rand = new Random();

        // ==========================================================
        // DEER HERD
        // ==========================================================
        ArrayList<Deer> deerList = new ArrayList<>();
        int deerN = 40;
        double deerSpeedLimit  = 5.5;
        double deerForceLimit  = 0.2;
        double deerWanderRad   = 3.0;
        double deerPathRad     = 15;
        int deerSize = 6;
        double deerAngle       = Math.PI/8;

        for (int i = 0; i < deerN; i++) {
            Vector2D pos = new Vector2D(
                width * 0.2 + rand.nextGaussian() * 100,
                height * 0.5 + rand.nextGaussian() * 100
            );
            Vector2D vel = new Vector2D(rand.nextDouble()*2 - 1, rand.nextDouble()*2 - 1);
            Vector2D acc = new Vector2D(0, 0);

            deerList.add(new Deer(
                pos, vel, acc,
                deerSpeedLimit, deerForceLimit,
                deerWanderRad, deerPathRad,
                deerSize,
                new Color(180,140,80), // body color
                Color.WHITE,           // compass color
                deerAngle,
                width, height
            ));
        }

        // ==========================================================
        // WOLF PACK
        // ==========================================================
        ArrayList<Wolf> wolfList = new ArrayList<>();
        int wolfN = 40; // requirmet n_wolves > 5
        double wolfSpeedLimit = 7.5;
        double wolfForceLimit = 0.4;
        double wolfWanderRad  = 4.5;
        double wolfPathRad    = 18;
        int wolfSize = 10;
        double wolfAngle      = Math.PI/6;

        for (int i = 0; i < wolfN; i++) {
            Vector2D pos = new Vector2D(
                width * 0.7 + rand.nextGaussian() * 120,
                height * 0.5 + rand.nextGaussian() * 120
            );
            Vector2D vel = new Vector2D(rand.nextDouble()*3 - 1.5, rand.nextDouble()*3 - 1.5);
            Vector2D acc = new Vector2D(0, 0);

            wolfList.add(new Wolf(
                pos, vel, acc,
                wolfSpeedLimit, wolfForceLimit,
                wolfWanderRad, wolfPathRad,
                wolfSize,
                new Color(100,100,100),  // wolf body
                Color.RED,               // heading color
                wolfAngle,
                width, height
            ));
        }

        // ==========================================================
        // GRIDS
        // ==========================================================
        HashMap<GridType, Grid> deerGrids = new HashMap<>();
        deerGrids.put(GridType.SEPARATION, new Grid(width, height, deerList.get(0).getCloseDistance(), GridType.SEPARATION));
        deerGrids.put(GridType.TOGETHER,   new Grid(width, height, deerList.get(0).getNeighborDistance(), GridType.TOGETHER));

        HashMap<GridType, Grid> wolfGrids = new HashMap<>();
        wolfGrids.put(GridType.SEPARATION, new Grid(width, height, wolfList.get(0).getCloseDistance(), GridType.SEPARATION));
        wolfGrids.put(GridType.TOGETHER,   new Grid(width, height, wolfList.get(0).getNeighborDistance(), GridType.TOGETHER));
        wolfGrids.put(GridType.PREDATOR_DETECTION, new Grid(width, height, wolfList.get(0).getpathRadius(), GridType.PREDATOR_DETECTION));

        // Adding Deer to their grids and to wolf detection
        for (Deer d : deerList) {
            deerGrids.get(GridType.SEPARATION).addBoid(d);
            deerGrids.get(GridType.TOGETHER).addBoid(d);
            wolfGrids.get(GridType.PREDATOR_DETECTION).addBoid(d);
        }

        // Adding Wolves to their grids and to deer TOGETHER
        for (Wolf w : wolfList) {
            wolfGrids.get(GridType.SEPARATION).addBoid(w);
            wolfGrids.get(GridType.TOGETHER).addBoid(w);
            deerGrids.get(GridType.TOGETHER).addBoid(w);
        }

        Boids deerHerd = new Boids(new ArrayList<>(deerList), deerGrids);
        if (wolfN < 5){
            throw new IllegalArgumentException("Number of wolves must be bigger than 5");}
        
        Boids wolfPack = new Boids(new ArrayList<>(wolfList), wolfGrids);

        // ==========================================================
        // SIMULATOR
        // ==========================================================
        ArrayList<Boids> allGroups = new ArrayList<>();
        allGroups.add(deerHerd);
        allGroups.add(wolfPack);

        MultipleBoidsSimulator sim = new MultipleBoidsSimulator(gui, allGroups);
        gui.setSimulable(sim);
    }
}
