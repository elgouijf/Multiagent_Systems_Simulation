package main.main_Boids.Boidutils;

public class FlowField {

    private final int resolution;
    private final int lignes;
    private final int colonnes;
    private final Vector_2D[][] field;


    public FlowField(int resolution, int width, int height, Vector_2D[][] field_array) {

        this.resolution = resolution;
        this.lignes = height / resolution;
        this.colonnes = width / resolution;

        this.field = new Vector_2D[lignes][colonnes];

        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                Vector_2D v = field_array[i][j];
                this.field[i][j] = new Vector_2D(v.getX(), v.getY());
            }
        }
    }

    public Vector_2D getVector(Vector_2D position) {

        int j = Math.max(0, Math.min((int)(position.getX() / resolution), colonnes - 1));
        int i = Math.max(0, Math.min((int)(position.getY() / resolution), lignes - 1));

        return field[i][j];
    }

    public int getResolution() { return resolution; }
    public int getRows()     { return lignes; }
    public int getColumns()   { return colonnes; }

    public Vector_2D[][] getFieldArray() { return field; }
}
