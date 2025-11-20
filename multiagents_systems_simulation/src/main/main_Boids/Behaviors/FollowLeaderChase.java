package main.main_Boids.Behaviors;

import java.util.ArrayList;
import java.util.Random;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boids.Species.Wolf;
import main.main_Boids.Boidutils.Grid;
import main.main_Boids.Boidutils.GridType;
import main.main_Boids.Boidutils.Vector2D;



public class FollowLeaderChase extends  Behavior{
   

    public FollowLeaderChase(double forceFactor){
        super(forceFactor);
    }    

    @Override
    public Vector2D behave(Boid b,Grid grid){
        Wolf wolf = (Wolf) b; // upcast to Wolf, as only Wolves have this behavior and submit to behavior is called by Boid reference
        double distanceChase = wolf.getSlowRadius();
        Boid prey = wolf.getPreyLeader();
        if (prey == null) {
            return new Vector2D(); // do nothing this frame
        }
        // if we are close enough to the prey, chase it directly
        double distancetoPrey = wolf.distanceTo(prey);
        if (distancetoPrey < distanceChase){
           Random rand = new Random();
           // pick a random point on a circle around the prey, the goal is to not go directly on the prey but circle around it
           double angle = rand.nextDouble()*Math.PI*2;
           Vector2D posPrey = prey.getPosition();
           Vector2D circlePrey = new Vector2D(distancetoPrey*Math.cos(angle),distancetoPrey*Math.sin(angle));
           // Construct the circle
           circlePrey.add(posPrey);
           return wolf.seek(circlePrey,forceFactor);
        }else{
            int indexLeader = wolf.getindexLeader();
            ArrayList<Wolf> pack = wolf.getPack();
            // seek the leader of the pack
            Vector2D target = pack.get(indexLeader).getPosition();
            return wolf.seek(target,forceFactor);
        }
    }
    
    @Override
    public GridType getGridType() {
        return GridType.TOGETHER;
    }

}