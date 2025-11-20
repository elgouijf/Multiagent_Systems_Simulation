package main.main_Boids.Boidutils;

public enum GridType {
    SEPARATION,  // for behaviors that require separation
    TOGETHER, // for alignment, cohesion, follow path, flee from predator, follow leader etc..
    PREDATOR_DETECTION // for behaviors that need to detect predators specifically
}
