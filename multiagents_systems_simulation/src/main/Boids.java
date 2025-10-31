package main;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Boids{
    private ArrayList<Boid> listBoids = new ArrayList<>();
    private int size = 0;
    

    public Boids(ArrayList<Boid> listBoids){
        this.listBoids = listBoids;
        this.size = listBoids.size();
    }

    public void add_boid(Boid new_boid){
        this.listBoids.add(new_boid);
        this.size++;
    }

    public ArrayList<Boid> getlisteBoids(){
        return this.listBoids;
    }

    public int getTaille(){
        return this.size;
    }



    public Boid getBoid(int index){
        if (index >= 0 && index < this.size){
            return this.listBoids.get(index);
        }
        else{
            System.out.println("Index out of bounds");
            return null;
        }
    }

    public void clearBoids(){
        this.listBoids.clear();
        this.size = 0;
    }

    public void removeBoid(int index){
        if (index >= 0 && index < this.size){
            this.listBoids.remove(index);
            this.size--;
        }
        else{
            System.out.println("Index out of bounds");
        }
    }

    public void separation(Boid boid){
        int n_close_boids = 0;
        Vector_2D average_flee = new Vector_2D(); // intiialize to an empty vector

        for (Boid otherboid : this.listBoids){
            
            double dist = boid.distance_to(otherboid);
            if ((otherboid != boid) && 
            (dist <= boid.getClose_distance())){
                // update n_close_boids
                n_close_boids += 1;
                Vector_2D from_me_to_you = new Vector_2D();
                from_me_to_you.subtract2New(otherboid.getPosition(), boid.getPosition());
                // the closer boid is to other the more it is urging to flee away
                from_me_to_you.updateMagnitude(1/dist);

                // update average_flee
                average_flee.add(from_me_to_you);
                
            }
        }
        
        if (n_close_boids >= 1){
            average_flee.divide(n_close_boids);
            // the boid wants to flee as fast as possible in the direction of the average_flee vector
            average_flee.updateMagnitude(boid.getSpeedlimit());
            Vector_2D flee_force = boid.getSteeringForce(average_flee);
            flee_force.limit(boid.getForceLimit());
            boid.applyForce(flee_force);
        }
    }
}