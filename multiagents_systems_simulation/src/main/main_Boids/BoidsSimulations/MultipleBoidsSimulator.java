package main.main_Boids.BoidsSimulations;
import main.main_Boids.Boidutils.*;
import main.EventManaging.EventManager;
import main.EventManaging.EventBoidsMultiple;
import main.main_Boids.Behaviors.*;
import main.main_Boids.Boids.*;
import main.main_Boids.Boids.Species.Eagle;
import gui.Simulable;
import gui.GUISimulator;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import gui.Oval;


public class MultipleBoidsSimulator implements Simulable {
    private FlowField windField = null; 
    private GUISimulator gui;
    private ArrayList<Boids> boidsLists; // plusieurs listes de boids
    private int width;
    private int height;
    private Vector_2D target;
    private EventManager manager;

    public void setWindField(FlowField field) {
        this.windField = field;
    }

    public void removeWindField() {
        this.windField = null;
    }

    public FlowField getWindField() {
        return windField;
    }
    public MultipleBoidsSimulator(GUISimulator gui, ArrayList<Boids> boidsLists, Vector_2D target) {
        this.gui = gui;
        this.boidsLists = boidsLists;
        this.width = gui.getWidth();
        this.height = gui.getHeight();
        this.target = target;
        this.manager = new EventManager();
        this.manager.addEvent(new EventBoidsMultiple(0, this, this.manager));
        this.reDisplay();
    }

    public MultipleBoidsSimulator(GUISimulator gui, Vector_2D target) {
        this.gui = gui;
        this.target = target;
        this.width = gui.getWidth();
        this.height = gui.getHeight();
        this.boidsLists = new ArrayList<>(); // liste vide pour commencer
        this.manager = new EventManager();
    }

    public void addBoidsContainer(Boids boids) {
        this.boidsLists.add(boids);
    }

    @Override
    public void next() {
        manager.next();
    }

    public void moveBoids() {
        long start = System.nanoTime();

        for (Boids boids : boidsLists) {
            ArrayList<Boid> listeBoids = boids.getlisteBoids();
            HashMap<GridType, Grid> grids = boids.getGrids();

            for (Boid b : listeBoids) {
                b.submittoGroupBehavior(grids, windField);
            }

            // Collecter les birds attrapés
        ArrayList<Boid> toRemove = new ArrayList<>();

        for (Boids boids_ : boidsLists) {
            for (Boid b : boids_.getlisteBoids()) {
                if (b instanceof Eagle) {
                    // Eagle chasse dans les autres boids lists
                    for (Boids preyGroup : boidsLists) {
                        if (preyGroup == boids_) continue; // pas ses propres congénères
                        ArrayList<Boid> caught = ((Eagle)b).hunt(preyGroup.getlisteBoids());
                        toRemove.addAll(caught);
                    }
                }
            }
        }

        // Supprimer les birds attrapés des listes et grids
        for (Boid prey : toRemove) {
            for (Boids group : boidsLists) {
                if (group.getlisteBoids().remove(prey)) {
                    for (Grid grid : group.getGrids().values()) {
                        grid.removeBoid(prey);
                    }
                    break;
                }
            }
        }

            // Update all boids
            for (Boid b : listeBoids) {
                b.updatestate();
                for (Behavior behavior : b.getBehaviors()) {
                    Grid grid = grids.get(behavior.getGridType());
                    if (grid != null) {
                        behavior.updateGrid(b, grid);
                    }
                }
                handleBorderBounce(b);
            }
        }

        long end = System.nanoTime();
        double time_per_frame = (end - start) / 1e6; // ms
        System.out.println("Frame time: " + time_per_frame + " ms");
        this.reDisplay();
    }

    @Override
    public void restart() {
        for (Boids boids : boidsLists) {
            for (Boid b : boids.getlisteBoids()) {
                b.reInit();
            }
        }
        manager.restart();
        manager.addEvent(new EventBoidsMultiple(0, this, manager));
        this.reDisplay();
    }

    /** Gestion des rebonds aux bords */
    private void handleBorderBounce(Boid boid) {
        int r = boid.getSize();
        Vector_2D pos = boid.getPosition();
        Vector_2D vel = boid.getVelocity();

        if (pos.getX() < 0) {
            pos.setX(0);
            vel.setX(Math.abs(vel.getX()));
        } else if (pos.getX() + 2*r > width) {
            pos.setX(width - 2*r);
            vel.setX(-Math.abs(vel.getX()));
        }

        if (pos.getY() < 0) {
            pos.setY(0);
            vel.setY(Math.abs(vel.getY()));
        } else if (pos.getY() + 2*r > height) {
            pos.setY(height - 2*r);
            vel.setY(-Math.abs(vel.getY()));
        }
    }

    public void reDisplay() {
        gui.reset();

        for (Boids boids : boidsLists) {
            for (Boid b : boids.getlisteBoids()) {
                double x = b.getPosition().getX();
                double y = b.getPosition().getY();
                int size = b.getSize();

                double orientation = b.getVelocity().heading();
                Vector_2D tip = new Vector_2D(2*size,0);
                Vector_2D left = new Vector_2D(-size,size);
                Vector_2D right = new Vector_2D(-size,-size);

                tip.rotate(orientation); left.rotate(orientation); right.rotate(orientation);
                tip.add(new Vector_2D(x,y)); left.add(new Vector_2D(x,y)); right.add(new Vector_2D(x,y));

                int[] xs = { (int)Math.round(tip.getX()), (int)Math.round(left.getX()), (int)Math.round(right.getX()) };
                int[] ys = { (int)Math.round(tip.getY()), (int)Math.round(left.getY()), (int)Math.round(right.getY()) };
                PolygonGraphics triangle = new PolygonGraphics(xs, ys, 3, b.getColor());
                gui.addGraphicalElement(triangle);
            }
        }

        // Draw target
        Oval target_oval = new Oval((int)target.getX(), (int)target.getY(), Color.GREEN, Color.GREEN, 4, 4);
        gui.addGraphicalElement(target_oval);
    }
}
