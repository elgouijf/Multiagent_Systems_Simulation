package main.main_Boids.Behaviors;
import java.util.Random;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.Grid;
import main.main_Boids.Boidutils.GridType;
import main.main_Boids.Boidutils.Path;
import main.main_Boids.Boidutils.Vector_2D;

public class FollowPath implements Behavior{
    private double forceFactor;
    private double distancetoArrival;
    private int nPoints;
    
    public FollowPath(double forceFactor,double distancetoArrival,int nPoints){
        this.forceFactor = forceFactor;
        this.distancetoArrival = distancetoArrival; // distance to consider arrival at the end of the path (because we cant be exactly on the point)
        this.nPoints = nPoints;
    }

    @Override
    public Vector_2D behave(Boid b,Grid grid){
        updatePath(b,grid);
        Vector_2D force = b.followPath(forceFactor);
        System.out.println("FollowPath force: " + force);
        return force;
    }


    public void updatePath(Boid b,Grid grid){
        Path path = b.getPath();
        if (arrivedEnd(b)){
          path.clear();
          Random rand = new Random();
          for (int i = 0; i < nPoints; i++) {
              double x = rand.nextDouble() * grid.getScreenWidth();
              double y = rand.nextDouble() * grid.getScreenHeight();
              path.add(new Vector_2D(x, y));
            }
        }
    }

    public boolean arrivedEnd(Boid b){
      // If the boid arrives at the end of the path we 
        Path path = b.getPath();
        int taille = path.getTaille();
        if (taille > 0){
          Vector_2D lastPoint = path.gettableauPoints().get(taille-1);
          Vector_2D pos = b.getPosition();
          double distance = pos.getdistance(lastPoint);
          if (distance > distancetoArrival){
            return false;
          }
          else{
            return true;
          }
        }else{
            return true;
        }
    }

    @Override
    public void updateGrid(Boid b, Grid grid) {
        grid.updateBoidCell(b);
    }

    @Override
    public GridType getGridType() {
        return GridType.TOGETHER; // utilise neighbor_distance
    }
}