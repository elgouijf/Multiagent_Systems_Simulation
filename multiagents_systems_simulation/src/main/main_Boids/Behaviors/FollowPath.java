package main.main_Boids.Behaviors;
import java.util.Random;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boidutils.Grid;
import main.main_Boids.Boidutils.GridType;
import main.main_Boids.Boidutils.Path;
import main.main_Boids.Boidutils.Vector2D;

public class FollowPath extends  Behavior{
  /* This behavior tries to simulate miration behavior by constructing a path from West to 
  East that Deers are supposed to follow*/
    private double distancetoArrival;
    private int nPoints;
    
    public FollowPath(double forceFactor,double distancetoArrival,int nPoints){
        super(forceFactor);
        this.distancetoArrival = distancetoArrival; // distance to consider arrival at the end of the path (because we cant be exactly on the point)
        this.nPoints = nPoints;
    }

    @Override
    public Vector2D behave(Boid b,Grid grid){
        updatePath(b,grid);
        Vector2D force = b.followPath(forceFactor);
        System.out.println("FollowPath force: " + force);
        return force;
    }


    public void updatePath(Boid b,Grid grid){
        // If the boid has arrived at the end of the path, generate a new random path
        Path path = b.getPath();
        if (arrivedEnd(b)){ // generate a new random path in case of arrival or empty path
          path.clear();
          Random rand = new Random();
          for (int i = 0; i < nPoints; i++) {
              double x = rand.nextDouble() * grid.getScreenWidth();
              double y = rand.nextDouble() * grid.getScreenHeight();
              path.add(new Vector2D(x, y));
            }
        }
    }

    public boolean arrivedEnd(Boid b){
      // If the boid arrives at the end of the path we 
        Path path = b.getPath();
        int size = path.getsize();
        if ( size > 0){
          Vector2D lastPoint = path.gettableauPoints().get(size-1);
          Vector2D pos = b.getPosition();
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