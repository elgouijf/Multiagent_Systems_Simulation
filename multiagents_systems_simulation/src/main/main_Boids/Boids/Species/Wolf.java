package main.main_Boids.Boids.Species;

import java.awt.Color;
import java.util.ArrayList;
import main.main_Boids.Behaviors.*;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.*;


public class Wolf extends Boid{

    private double killRadius;
    private static ArrayList<Wolf> pack = new ArrayList<>();
    private static int indexLeader;
    private String prey;
    private static Boid preyLeader;
    
    public Wolf(Vector_2D position, Vector_2D velocity, Vector_2D acceleration,
                 double speedLimit, double forceLimit, double wander_radius, double path_radius,
                 int boid_size, Color color, Color compassColor, double angleDistance,
                 int windowWidth, int windowHeight){
        super(position, velocity, acceleration, speedLimit, forceLimit,
                wander_radius, path_radius, boid_size,
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
        this.behaviors.add(new Separation(0.15, width, height, this.close_distance * 1.5));

        // they run in a common direction during chase
        this.behaviors.add(new Alignment(1.2,width, height, this.neighbor_distance));

        // they stay together but not too tight
        this.behaviors.add(new Cohesion(0.8, width, height, this.neighbor_distance * 1.2));

        // Predation → strongest
        this.behaviors.add(new FollowLeaderChase(3));
    }
    public void updateLeader(Grid grid){

        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i< pack.size();i++){
            Wolf wolf = pack.get(i);
           ArrayList<Boid> neighbors = grid.getNeighbors(wolf);
           for (Boid other : neighbors){
            if (other == null) continue; // Sometrimes neighbors cells can be empty
             if (other.getSpecies().equals(prey)){
                double distance = wolf.distance_to(other);
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
            double distancetoPrey = wolf.distance_to(preyLeader);
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