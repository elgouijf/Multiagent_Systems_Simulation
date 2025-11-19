package main.main_Boids.Boids.Species;


import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.Vector_2D;
import main.main_Boids.Boidutils.Path;


public class Deer extends Boid{

    private String predator;
    private Path path = null;
    private double distanceArrival;
    
    public Deer(Vector_2D position, Vector_2D velocity, Vector_2D acceleration, double speedlimit, double forceLimit, 
                double wander_radius, double path_radius, int boid_size, Color color, Color compassColor, 
                double angleDistance, int windowWidth, int windowHeight){
        super(position, velocity, acceleration, speedlimit, forceLimit, wander_radius, path_radius,
              boid_size, color, compassColor, angleDistance, windowWidth, windowHeight);
        this.species = "Deer";

        // Tries to avoid others and predators;
        this.wander_radius = 3;
        this.initializeBehaviors(windowWidth, windowHeight);
        this.path = new Path(path_radius);
            
                }
    public Path updatePath(Grid grid){
       if (this.arrivedEnd()){
          
       }
    }

    public boolean arrivedEnd(){
        if (path != null){
          int taille = path.getTaille();
          Vector_2D lastPoint = path.gettableauPoints().get(taille-1);
          Vector_2D pos = this.getPosition();
          double distance = pos.getdistance(lastPoint);
          if (distance > distanceArrival){
            return false;
          } else{
            return true;
          }
        }else{
            return true;
        }
    }
        
}