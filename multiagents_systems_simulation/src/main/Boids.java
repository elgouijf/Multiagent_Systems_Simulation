package main;
import java.util.ArrayList;

public class Boids{
    private ArrayList<Boid> listeBoids = new ArrayList<>();
    private int taille = 0;     

    public void add_boid(Boid new_boid){
        this.listeBoids.add(new_boid);
        taille++;
    }
    public ArrayList<Boid> getlisteBoids(){
        return this.listeBoids;
    }
    public int getTaille(){
        return this.taille;
    }

}