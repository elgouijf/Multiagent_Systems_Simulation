package main.main_Boids.Boidutils;

/**
 * Generates FlowFields with Perlin-noise-based wind.
 */
public class FlowFieldGenerator {

    public static FlowField generateNoiseField(int resolution, int width, int height, double time, double strength, double scale) {

        int rows = height / resolution;
        int cols = width / resolution;

        Vector_2D[][] field = new Vector_2D[rows][cols];


        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                double nx = col * scale;
                double ny = row * scale;

                // Angle of wind direction from Perlin noise
                double angle = Perlin.noise(nx, ny, time) * Math.PI * 2.0;

                Vector_2D v = new Vector_2D(Math.cos(angle), Math.sin(angle));
                v.multiply(strength);
                field[row][col] = v;
            }
        }

        return new FlowField(resolution, width, height, field);
    }
}
