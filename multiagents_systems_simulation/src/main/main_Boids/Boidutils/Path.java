package  main.main_Boids.Boidutils;
import java.util.ArrayList;

public class Path{
    private int size = 0;
    private ArrayList<Vector2D> tableauPoints = new ArrayList<>();
    private double pathRadius;

    public Path(double pathRadius){
        this.pathRadius = pathRadius;
    }
    public void add(Vector2D p){
       tableauPoints.add(p);
       size++;
    }
    public int getsize(){
        return this.size;
    }
    public double getPathRadius(){
        return this.pathRadius;
    }
    public ArrayList<Vector2D> gettableauPoints(){
        return this.tableauPoints;
    }
    public void clear(){
        tableauPoints.clear();
        size = 0;
    }
}