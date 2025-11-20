package main.main_Boids.BoidsSimulations;

import main.main_Boids.Boidutils.*;
import main.EventManaging.EventManager;
import main.EventManaging.EventBoids;
import main.main_Boids.Behaviors.*;
import main.main_Boids.Boids.*;

import gui.Simulable;
import gui.GUISimulator;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import gui.Oval;

public class BoidsSimulatorWithWind extends BoidsSimulator {

    private int resolution = 30;
    private double strength = 1;
    private double scale = 0.1;
    private boolean showWind = true;

    public BoidsSimulatorWithWind(GUISimulator gui, Boids boids) {
        super(gui, boids);  // Use parent constructor
    }

    public void setWindField(FlowField field) {
        this.windField = field;
    }

    public void removeWindField() {
        this.windField = null;
    }

    public FlowField getWindField() {
        return windField;
    }

    public void setShowWind(boolean show) {
        this.showWind = show;
    }

    @Override
    public void moveBoids() {
        // Récupérer la force du vent à la position du boid
        double time = System.currentTimeMillis() * 0.0001;
        this.windField = FlowFieldGenerator.generateNoiseField(
                resolution, this.width, this.height, time, strength, scale
            );
        // Use parent logic for boid movement
        super.moveBoids();
    }

    @Override
    public void reDisplay() {


        // Draw boids using parent logic
        super.reDisplay();
        if (showWind && windField != null) {
            FlowFieldDraw.draw(this.gui, windField);
        }
    }

}
