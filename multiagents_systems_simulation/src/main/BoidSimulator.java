package main;
import java.awt.Color;
import gui.Simulable;
import gui.GUISimulator;
import java.awt.Point;
import java.util.List;


public class BoidSimulator implements Simulable {
    private GUISimulator guis;
    private Boid boid;
    private Color color;
    private int width;
    private int height;
    private Vector_2D target;
    int radius = 7;


    // Constructor
    public BoidSimulator(GUISimulator guis, Boid boid, Color color, Vector_2D target){ 
        this.guis = guis;
        this.boid = boid;
        this.width = guis.getWidth();
        this.height = guis.getHeight();
        this.color = color;
        this.target = target;

        this.reDisplay();
    }
    
    @Override
    public void next() {
        // Update dimensions in case of window resize
        this.width = guis.getWidth();
        this.height = guis.getHeight();

        this.boid.seek(target);
        this.boid.updatestate();
        // Bounce horizontally
        if (boid.getPosition().getX() < 0 || boid.getPosition().getX() + 2*radius > width) {
            boid.getVelocity().setX(-boid.getVelocity().getX());
            boid.getPosition().setX(
                Math.max(0, Math.min(boid.getPosition().getX(), width - 2*radius))
            );
        }
        // Bounce vertically
        if (boid.getPosition().getY() < 0 || boid.getPosition().getY() + 2*radius > height) {
            boid.getVelocity().setY(-boid.getVelocity().getY());
            boid.getPosition().setY(
                Math.max(0, Math.min(boid.getPosition().getY(), height - 2*radius))
            );
        }
        this.reDisplay();
    }

    @Override
    public void restart() {
        boid.reInit();
        this.reDisplay();
    }

    private void reDisplay() {
        guis.reset();
        double x = boid.getPosition().getX();
        double y = boid.getPosition().getY();
        double vx = boid.getVelocity().getX();
        double vy = boid.getVelocity().getY();
        double x_off = x + vx;
        double y_off = y + vy;
        guis.addGraphicalElement(new gui.Oval((int)x, (int)y, Color.YELLOW, Color.BLACK, 8));
        guis.addGraphicalElement(new gui.Oval((int)x_off, (int)y_off, Color.BLACK, Color.BLACK, 3));
    }
}

