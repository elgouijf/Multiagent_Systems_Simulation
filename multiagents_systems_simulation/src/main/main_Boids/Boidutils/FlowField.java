package main.main_Boids.Boidutils;

public class FlowField {

    private final int resolution;
    private final int lignes;
    private final int colonnes;
    private final Vector2D[][] field;


    public FlowField(int resolution, int width, int height, Vector2D[][] field_array) {
        /* Generates a flow field that can have an affect on boids (wind, water stream etc..) */
        this.resolution = resolution;
        this.lignes = height / resolution;
        this.colonnes = width / resolution;

        this.field = new Vector2D[lignes][colonnes];

        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                Vector2D v = field_array[i][j];
                this.field[i][j] = new Vector2D(v.getX(), v.getY());
            }
        }
    }

    public Vector2D getVector(Vector2D position) {

        int j = Math.max(0, Math.min((int)(position.getX() / resolution), colonnes - 1));
        int i = Math.max(0, Math.min((int)(position.getY() / resolution), lignes - 1));

        return field[i][j];
    }

    public int getResolution() { return resolution; }
    public int getRows()     { return lignes; }
    public int getColumns()   { return colonnes; }

    public Vector2D[][] getFieldArray() { return field; }
}
