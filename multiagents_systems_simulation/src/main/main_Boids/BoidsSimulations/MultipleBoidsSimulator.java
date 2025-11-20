package main.main_Boids.BoidsSimulations;
import main.main_Boids.Boidutils.*;
import main.EventManaging.EventManager;
import main.EventManaging.EventBoidsMultiple;
import main.main_Boids.Behaviors.*;
import main.main_Boids.Boids.*;
import main.main_Boids.Boids.Species.Bird;
import main.main_Boids.Boids.Species.Deer;
import main.main_Boids.Boids.Species.Eagle;
import main.main_Boids.Boids.Species.Wolf;
import gui.Simulable;
import gui.GUISimulator;
import java.util.ArrayList;
import java.util.HashMap;

import gui.Rectangle;

public class MultipleBoidsSimulator implements Simulable {
    /*The ultimate Simulator, allows for multiple species to coexist(if being eaten counts as 
    coexisting of course) in the same environment */
    private FlowField windField = null; 
    private GUISimulator gui;
    private ArrayList<Boids> boidsLists; // plusieurs listes de boids
    private int width;
    private int height;
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
    public MultipleBoidsSimulator(GUISimulator gui, ArrayList<Boids> boidsLists) {
        this.gui = gui;
        this.boidsLists = boidsLists;
        this.width = gui.getWidth();
        this.height = gui.getHeight();
        this.manager = new EventManager();
        this.manager.addEvent(new EventBoidsMultiple(0, this, this.manager));
        this.reDisplay();
    }

    public MultipleBoidsSimulator(GUISimulator gui) {
        this.gui = gui;
        this.width = gui.getWidth();
        this.height = gui.getHeight();
        this.boidsLists = new ArrayList<>(); // empty list of boids containers
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

        for (Boids boids : boidsLists) {
            ArrayList<Boid> listeBoids = boids.getlisteBoids();
            HashMap<GridType, Grid> grids = boids.getGrids();
            
            for (Boid b : listeBoids) {
                // Wolf-specific leader update
                if (b instanceof Wolf) {
                    Wolf wolf = (Wolf) b;
                    if (wolf.changePrey()) {
                        Grid grid = grids.get(GridType.PREDATOR_DETECTION);
                        wolf.updateLeader(grid);
                    }
                }

                b.submittoGroupBehavior(grids, windField);
            }

            // ======= Handling Eagles catching Birds =======
            ArrayList<Boid> toRemoveBird = new ArrayList<>();
            for (Boids boids_ : boidsLists) {
                for (Boid b : boids_.getlisteBoids()) {
                    if (b instanceof Eagle) {
                        for (Boids preyGroup : boidsLists) {
                            if (preyGroup == boids_) continue;
                            if (!preyGroup.getlisteBoids().isEmpty() &&
                                preyGroup.getlisteBoids().get(0) instanceof Bird) {
                                toRemoveBird.addAll(((Eagle)b).hunt(preyGroup.getlisteBoids()));
                            }
                        }
                    }
                }
            }

            // Collect all Deer caught by Wolves
            ArrayList<Deer> toRemoveDeer = new ArrayList<>();

            for (Boids boids_ : boidsLists) {
                for (Boid b : boids_.getlisteBoids()) {
                    if (b instanceof Wolf) {
                        Wolf wolf = (Wolf) b;
                        for (Boids preyGroup : boidsLists) {
                            if (preyGroup == boids_) continue; // skip own pack

                            // Only consider Deer as prey
                            if (!preyGroup.getlisteBoids().isEmpty() && preyGroup.getlisteBoids().get(0) instanceof Deer) {
                                ArrayList<Boid> caught = wolf.hunt(preyGroup.getlisteBoids());
                                for (Boid prey : caught) {
                                    toRemoveDeer.add((Deer) prey); // cast safely
                                }
                            }
                        }
                    }
                }
            }

            // Remove caught Deer from their groups and grids
            for (Deer prey : toRemoveDeer) {
                for (Boids group : boidsLists) {
                    if (group.getlisteBoids().remove(prey)) {
                        for (Grid grid : group.getGrids().values()) {
                            grid.removeBoid(prey);
                        }
                        break; // Deer removed, stop searching
                    }
                }
            }

            //  Wolves dispersion: steer slightly if no prey nearby (exploring)
            for (Wolf wolf : Wolf.getPack()) {
                boolean preyNearby = false;
                for (Boid b : boidsLists.get(0).getlisteBoids()) { // Deer group assumed at index 0
                    if (wolf.distanceTo(b) < wolf.getpathRadius()) {
                        preyNearby = true;
                        break;
                    }
                }
                if (!preyNearby) {
                    // add a small random steering vector to make wolves explore
                    Vector2D dispersion = new Vector2D(Math.random()-0.5, Math.random()-0.5);
                    wolf.getAcceleration().add(dispersion);
                }
            }

        


            // Remove all caught prey (Birds or Deer) from lists and grids
            for (Boid prey : toRemoveBird) {
                for (Boids group : boidsLists) {
                    if (group.getlisteBoids().remove(prey)) {
                        for (Grid grid : group.getGrids().values()) {
                            grid.removeBoid(prey);
                        }
                        break;
                    }
                }
            }

            // ======= Update all boids =======
            for (Boid b : listeBoids) {
                b.updatestate();
                for (Behavior behavior : b.getBehaviors()) {
                    Grid grid = grids.get(behavior.getGridType());
                    if (grid != null) {
                        behavior.updateGrid(b, grid);
                    }
                }
                handleBordersSteering(b);
            }
        }

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

    private void handleBordersSteering(Boid b) {
        /* handle border steering, boids steer away when they get close to the border,
        Here we don't use Border Bounce as (through pure experimentation) forces like chase 
        and fleefrompredator become too great that when combined with bounce give a weird behavior*/
        int margin = 80;       // distance from border to start steering
        double steerStrength = 0.5;

        Vector2D pos = b.getPosition();
        Vector2D steer = new Vector2D();

        // Left
        if (pos.getX() < margin) {
            steer.add(new Vector2D(1, 0));
        }
        // Right
        if (pos.getX() > width - margin) {
            steer.add(new Vector2D(-1, 0));
        }
        // Top
        if (pos.getY() < margin) {
            steer.add(new Vector2D(0, 1));
        }
        // Bottom
        if (pos.getY() > height - margin) {
            steer.add(new Vector2D(0, -1));
        }

        if (!steer.isZero()) {
            steer.normalize();
            steer.multiply(steerStrength * b.getforceLimit());
            b.getAcceleration().add(steer);
        }
    }


    public void reDisplay() {
        gui.reset();

        for (Boids boids : boidsLists) {
            for (Boid b : boids.getlisteBoids()) {
                double x = b.getPosition().getX();
                double y = b.getPosition().getY();
                int size = b.getSize();
            if (b instanceof Deer){ // Draw Deer as rectangles
               Rectangle rectangle = new Rectangle((int) x,(int) y,b.getColor(),b.getColor(),size);
               gui.addGraphicalElement(rectangle);
            }else if (b instanceof Wolf){ // Draw Wolf as rectangles
               Rectangle rectangle = new Rectangle((int) x,(int) y,b.getColor(),b.getColor(),size);
               gui.addGraphicalElement(rectangle);
            } else{
                double orientation = b.getVelocity().heading();
                Vector2D tip = new Vector2D(2*size,0);
                Vector2D left = new Vector2D(-size,size);
                Vector2D right = new Vector2D(-size,-size);

                tip.rotate(orientation); left.rotate(orientation); right.rotate(orientation);
                tip.add(new Vector2D(x,y)); left.add(new Vector2D(x,y)); right.add(new Vector2D(x,y));

                int[] xs = { (int)Math.round(tip.getX()), (int)Math.round(left.getX()), (int)Math.round(right.getX()) };
                int[] ys = { (int)Math.round(tip.getY()), (int)Math.round(left.getY()), (int)Math.round(right.getY()) };
                // Draw the triangle for eagles and birds
                PolygonGraphics triangle = new PolygonGraphics(xs, ys, 3, b.getColor());
                gui.addGraphicalElement(triangle);
            }
            }
        }
    }
}
