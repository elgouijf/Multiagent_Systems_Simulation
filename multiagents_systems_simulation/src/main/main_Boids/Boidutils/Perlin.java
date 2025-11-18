package main.main_Boids.Boidutils;

public class Perlin { 
    // Returns a value between 0 and 1
    public static double noise(double x, double y, double z) {
        return ImprovedNoise.noise(x, y, z) * 0.5 + 0.5;
    }
}
