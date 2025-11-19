package main.main_Boids.Behaviors;

import java.util.ArrayList;
import java.util.Random;
import main.main_Boids.Boids.Boid;
import main.main_Boids.Boids.Species.Wolf;
import main.main_Boids.Boidutils.Grid;
import main.main_Boids.Boidutils.GridType;
import main.main_Boids.Boidutils.Vector_2D;



public class FollowLeaderChase implements Behavior{
   
    private double forceFactor;

    public FollowLeaderChase(double forceFactor){
        this.forceFactor = forceFactor;
    }    

    @Override
    public Vector_2D behave(Boid b,Grid grid){
        Wolf wolf = (Wolf) b; // nécessaire afin de compiler
        double distanceChase = wolf.getSlowRadius();
        Boid prey = wolf.getPreyLeader();
        if (prey == null) {
            return new Vector_2D(); // do nothing this frame
        }
        double distancetoPrey = wolf.distance_to(prey);
        if (distancetoPrey < distanceChase){
           Random rand = new Random();
           double angle = rand.nextDouble()*Math.PI*2;
           Vector_2D posPrey = prey.getPosition();
           Vector_2D circlePrey = new Vector_2D(distancetoPrey*Math.cos(angle),distancetoPrey*Math.sin(angle));
           circlePrey.add(posPrey);
           return wolf.seek(circlePrey,forceFactor);
        }else{
            int indexLeader = wolf.getindexLeader();
            ArrayList<Wolf> pack = wolf.getPack();
            Vector_2D target = pack.get(indexLeader).getPosition();
            return wolf.seek(target,forceFactor);
        }
    }
    
    @Override
    public void updateGrid(Boid b, Grid grid) {
        grid.updateBoidCell(b);
    }

    @Override
    public GridType getGridType() {
        return GridType.TOGETHER;
    }

}