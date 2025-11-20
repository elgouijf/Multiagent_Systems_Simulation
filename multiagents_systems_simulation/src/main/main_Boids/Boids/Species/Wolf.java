package main.main_Boids.Boids.Species;

import java.awt.Color;
import java.util.ArrayList;
import main.main_Boids.Behaviors.*;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.*;


public class Wolf extends Boid{
    /* I am a father, a son, a serial killer (please don't take it seriously it just a reference i had the urge to put in a comment),
    But more importantly Wolf is not a solo predator, it seeks its prey in circles by following a given leader (which is the closest to the prey) */
    private double killRadius;
    private static ArrayList<Wolf> pack = new ArrayList<>();
    private static int indexLeader;
    private String prey;
    private static Boid preyLeader;
    
    public Wolf(Vector2D position, Vector2D velocity, Vector2D acceleration,
                 double speedLimit, double forceLimit, double wanderRadius, double pathRadius,
                 int boidSize, Color color, Color compassColor, double angleDistance,
                 int windowWidth, int windowHeight){
        super(position, velocity, acceleration, speedLimit, forceLimit,
                wanderRadius, pathRadius, boidSize,
                color, compassColor, angleDistance, windowWidth, windowHeight);
        
        pack.add(this);
        this.species = "Wolf";
        this.killRadius = 1.0 ;
        this.prey = "Deer";
        initializeBehaviors(windowWidth, windowHeight);
                 }
    

    public void initializeBehaviors(int width, int height) {
        
        this.behaviors.clear();
        // they tolerate being close in a pack
        this.behaviors.add(new Separation(0.15, width, height, this.closeDistance * 1.5));

        // they run in a common direction during chase
        this.behaviors.add(new Alignment(1.2,width, height, this.neighborDistance));

        // they stay together but not too tight
        this.behaviors.add(new Cohesion(0.8, width, height, this.neighborDistance * 1.2));

        // Predation → strongest
        this.behaviors.add(new FollowLeaderChase(3));
    }
    public void updateLeader(Grid grid){

        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i< pack.size();i++){
            Wolf wolf = pack.get(i);
           ArrayList<Boid> neighbors = grid.getNeighbors(wolf);
           for (Boid other : neighbors){
            if (other == null) continue; // Sometimes neighbors cells can be empty
             if (other.getSpecies().equals(prey)){ // only consider prey 
                double distance = wolf.distanceTo(other);
                if (distance < minDistance ){
                    minDistance = distance;
                    this.preyLeader = other;
                    this.indexLeader = i;
                }
             }
           }
        } 
    }
    public boolean changePrey(){
        if (preyLeader == null){
            return true;
        }
        for (Wolf wolf : pack){
            double distancetoPrey = wolf.distanceTo(preyLeader);
            if (distancetoPrey > this.killRadius){
                return false;
            }
        }
        return true;
    }

    public int getindexLeader(){
        return this.indexLeader;
    }
    public Boid getPreyLeader(){
        return this.preyLeader;
    }
    public ArrayList<Wolf> getPack(){
        return this.pack;
    }


}