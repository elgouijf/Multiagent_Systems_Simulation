package main.main_Boids.Boids.Species;

import java.awt.Color;
import java.util.ArrayList;
import main.main_Boids.Behaviors.*;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.*;

public class Wolf extends Boid {
    private double killRadius;                       // capture radius (distance)
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

        // sensible default: relative to graphics size of wolf (so it's not tiny)
        this.killRadius = Math.max(8.0, boidSize * 1.5); // <- important fix

        this.prey = "Deer";
        initializeBehaviors(windowWidth, windowHeight);
    }

    public void initializeBehaviors(int width, int height) {
        this.behaviors.clear();
        this.behaviors.add(new Separation(0.15, width, height, this.closeDistance * 1.5));
        this.behaviors.add(new Alignment(1.2, width, height, this.neighborDistance));
        this.behaviors.add(new Cohesion(0.8, width, height, this.neighborDistance * 1.2));
        this.behaviors.add(new FollowLeaderChase(3));
    }

    public void updateLeader(Grid grid){
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < pack.size(); i++){
            Wolf wolf = pack.get(i);
            ArrayList<Boid> neighbors = grid.getNeighbors(wolf);
            for (Boid other : neighbors){
                if (other == null) continue;
                if (other.getSpecies().equals(prey)){
                    double distance = wolf.distanceTo(other);
                    if (distance < minDistance ){
                        minDistance = distance;
                        Wolf.preyLeader = other;
                        Wolf.indexLeader = i;
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
            double distanceToPrey = wolf.distanceTo(preyLeader);
            if (distanceToPrey > this.killRadius){
                return false;
            }
        }
        return true;
    }

    /**
     * Hunt: returns the list of prey (Boid) that are considered captured.
     * Rule: a prey is caught when at least threshold wolves are within killRadius.
     */
    public ArrayList<Boid> hunt(ArrayList<Boid> potentialPrey) {
        ArrayList<Boid> caught = new ArrayList<>();

        if (pack.isEmpty()) return caught;

        // threshold number of wolves required

        for (Boid prey : potentialPrey) {
            if (!prey.getSpecies().equals("Deer")) continue;

            int wolvesAround = 0;
            for (Wolf wolf : pack) {
                double d_square = wolf.distanceToOptimized(prey); // squared distance
                if (d_square < (killRadius * killRadius)) {
                    wolvesAround++;
                }
            }

            // capture if enough wolves surround the prey
            if (wolvesAround >= 5) {
                caught.add(prey);
            }
        }

        return caught;
    }

    // getters
    public int getindexLeader(){
        return this.indexLeader;
    }
    public Boid getPreyLeader(){
        return this.preyLeader;
    }

    public void setPreyLeader(Boid preyLeader){
        this.preyLeader = preyLeader;
    }

    // return static pack (non-static previously caused confusion when called statically)
    public static ArrayList<Wolf> getPack(){
        return pack;
    }
}
