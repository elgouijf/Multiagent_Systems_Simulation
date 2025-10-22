package main;
import java.util.*;

public class Boids{
    public LinkedHashMap<Integer, Boid> Liste_boids = new LinkedHashMap<>();
    public int taille = 0;     

    public void add_boid(Boid new_boid){
        Liste_boids.put(taille,new_boid);
        taille++;
    }
}