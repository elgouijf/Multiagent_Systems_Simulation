package main.Boids;
import main.Boidutils.*;
import main.Boidutils.GridType ;
import java.util.ArrayList;
import java.util.HashMap;

public class Boids{
    private ArrayList<Boid> listBoids = new ArrayList<>();
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