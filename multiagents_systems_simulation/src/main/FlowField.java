package main;

public class FlowField{
    private int resolution;
    private Vector_2D[][] field;
    private int lignes;
    private int colonnes;
    public FlowField(int width, int height,Vector_2D[][] field_array){
        lignes = height / resolution;
        colonnes = width / resolution;
        for (int i = 0; i < lignes; i++){
            for (int j = 0; j < colonnes; j++){
                Vector_2D v = field_array[i][j];
                field[i][j] = new Vector_2D(v.getX(),v.getY());
            }
        }
    }
    public Vector_2D get_vector(Vector_2D position){
        int i = (int) position.getX() / resolution;
        int j = (int) position.getY() / resolution;
        return field[i][j];
    }

}