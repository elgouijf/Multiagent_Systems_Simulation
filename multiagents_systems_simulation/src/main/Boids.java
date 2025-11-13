package main;
import java.util.ArrayList;

public class Boids{
    private ArrayList<Boid> listBoids = new ArrayList<>();
    private Grid grid_separation;
    private Grid grid_together;
    private int size = 0;
    

    public Boids(ArrayList<Boid> listBoids, Grid grid_separation, Grid grid_together){
        this.listBoids = listBoids;
        this.size = listBoids.size();
        this.grid_separation = grid_separation;
        this.grid_together = grid_together;
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

    public Grid getGridSeparation(){
        return this.grid_separation;
    }

    public Grid getGridTogether(){
        return this.grid_together;
    }



    
}