package main.main_Boids.Boids;
import main.main_Boids.Boidutils.*;
import main.main_Boids.Boidutils.GridType ;
import java.util.ArrayList;
import java.util.HashMap;

public class Boids{
    // List of boids in the simulation (usually it is constructed in Tests)
    private ArrayList<Boid> listBoids = new ArrayList<>();
    // The different grids used for spatial partitioning based on behavior types (please check Boid.submitBehavior method to understand how it works)
    private HashMap<GridType,Grid> grids = new HashMap<>();
    private int size = 0;
    

    public Boids(ArrayList<Boid> listBoids, HashMap<GridType,Grid> grids){
        this.listBoids = listBoids;
        this.size = listBoids.size();
        this.grids = grids;
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

    public HashMap<GridType,Grid> getGrids(){
        return this.grids;
    }



    
}