package  main;
import java.util.ArrayList;

public class Path{
    private int taille = 0;
    private ArrayList<Vector_2D> tableauPoints = new ArrayList<>();
    private double path_radius;

    public Path(double path_radius){
        this.path_radius = path_radius;
    }
    public void add(Vector_2D p){
       tableauPoints.add(p);
       taille++;
    }
}