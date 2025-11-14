package  main.Boidutils;
import java.util.ArrayList;

public class Path{
    private int taille = 0;
    private ArrayList<Vector_2D> tableauPoints = new ArrayList<>();
    private double pathRadius;

    public Path(double pathRadius){
        this.pathRadius = pathRadius;
    }
    public void add(Vector_2D p){
       tableauPoints.add(p);
       taille++;
    }
    public int getTaille(){
        return this.taille;
    }
    public double getPathRadius(){
        return this.pathRadius;
    }
    public ArrayList<Vector_2D> gettableauPoints(){
        return this.tableauPoints;
    }
}